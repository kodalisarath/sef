package com.ericsson.raso.sef.logging;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.message.Message;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TimeTicks;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

@Plugin(name = "Snmp", category = "Core", elementType = "Appender", printObject = true)
public class SnmpAppender extends AbstractAppender {

	public final static String		SYS_UPTIME_OID		= "1.3.6.1.2.1.1.3.0";
	public final static String		SNMP_TRAP_OID		= "1.3.6.1.6.3.1.1.4.1.0";

	private String					communityString		= "Ericsson/RASO/CAC";
	private String					applicationTrapOid	= "1.3.6.1.4.1.193.255.2.1.0.1";
	private String					severityOid			= "1.3.6.1.4.1.193.255.2.1.1.1";
	private String					timestampOid		= "1.3.6.1.4.1.193.255.2.1.1.2";
	private String					errorCodeOid		= "1.3.6.1.4.1.193.255.2.1.1.3";
	private String					errorDescriptionOid	= "1.3.6.1.4.1.193.255.2.1.1.4";
	private String					additionalTextOid	= "1.3.6.1.4.1.193.255.2.1.1.5";
	private String					stackTraceOid		= "1.3.6.1.4.1.193.255.2.1.1.6";
	private String					nodeNameOid			= "1.3.6.1.2.1.1.5.0";
	private String					nodeLocationOid		= "1.3.6.1.2.1.1.6.0";

	private String					nodeName			= "localhost";
	private String					nodeLocation		= "127.0.0.1";
	private String					managementHost		= "127.0.0.1";
	private String					managementPort		= "161";
	private String					timestampFormat		= "yyyy-MM-dd HH:mm:ss";
	private String					useClearanceFor		= "all";
	private boolean					isConfigured		= false;

	private static SimpleDateFormat	timestampFormatter	= null;
	private List<Integer>			persistentAlarms	= null;
	
	private DefaultUdpTransportMapping mapping = null;
	private Snmp snmpStack = null;
	private CommunityTarget target = null;
	private InetAddress nmsHost = null;
	private int nmsPort = -1;

	public SnmpAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
		super(name, filter, layout, ignoreExceptions);

		
	}

	@Override
	public void append(LogEvent event) {
		if (!this.isConfigured) {
			return;
		}
		
		Message message = event.getMessage();
		
		SnmpEntity snmpEvent = null;
		if (message.getParameters()[0] instanceof SnmpEntity) 
			snmpEvent = (SnmpEntity) message.getParameters()[0];
		else
			return; // Not an SNMP object to work with....
		
		
		if (isAlreadySent(snmpEvent))
			return;
		
		PDU trapPdu = new PDU();
		VariableBinding variable = null;
		
		// System Uptime
		long systemStartupTime = java.lang.management.ManagementFactory.getRuntimeMXBean().getUptime();
		variable = new VariableBinding(new OID(SYS_UPTIME_OID), new TimeTicks(systemStartupTime/10));
		trapPdu.add(variable);
		
		// Application Trap 
		variable = new VariableBinding(new OID(SNMP_TRAP_OID), new OID(applicationTrapOid));
		trapPdu.add(variable);
		
		// nodeName
		variable = new VariableBinding(new OID(nodeNameOid), new OctetString(nodeName));
		trapPdu.add(variable);
		
		// nodeLocation
		variable = new VariableBinding(new OID(nodeLocationOid), new OctetString(nodeLocation));
		trapPdu.add(variable);
		
		// severity
		variable = new VariableBinding(new OID(severityOid), new OctetString(snmpEvent.getSnmpMessage().getName()));
		trapPdu.add(variable);
		
		// timestamp		
		Date eventTimestamp = new Date(event.getMillis());
		variable = new VariableBinding(new OID(timestampOid), new OctetString(this.timestampFormatter.format(eventTimestamp)));
		trapPdu.add(variable);
		
		// add stack trace element to identify where in codebase this alarm was initiated
		StackTraceElement stack = event.getSource();
		String stackTrace = stack.getFileName() + ":" + stack.getLineNumber() + " - " + stack.getClassName() + "." + stack.getMethodName();
		variable = new VariableBinding(new OID(stackTraceOid), new OctetString(stackTrace));
		trapPdu.add(variable);

		
		//---- Start doing Application specific items...		
		// error Code
		variable = new VariableBinding(new OID(errorCodeOid), new Integer32(snmpEvent.getCode()));
		trapPdu.add(variable);
		
		// event Description
		variable = new VariableBinding(new OID(errorDescriptionOid), new OctetString(snmpEvent.getMessage()));
		trapPdu.add(variable);
		
		// event Description
		if (snmpEvent.getAdditionalMessage() != null && !snmpEvent.getAdditionalMessage().isEmpty()) {
			variable = new VariableBinding(new OID(errorDescriptionOid), new OctetString(snmpEvent.getMessage()));
			trapPdu.add(variable);
		}
		
		
		
		// Time to send this out....
		try {
			snmpStack.send(trapPdu, target);
		} catch (IOException e) {
			LOGGER.fatal("Unable to send SNMP Trap - " + e.getMessage());
		}
		
		
	}

	private boolean isAlreadySent(SnmpEntity event) {
		if (this.persistentAlarms == null) this.persistentAlarms = new ArrayList<Integer>();

		if (this.persistentAlarms.contains(event.getCode())) {
			if (event instanceof Alarm) return true;

			if (event instanceof Clear) {
				this.persistentAlarms.remove(event.getCode());
				return false;
			}
		}

		if (this.useClearanceFor.equalsIgnoreCase("all") || this.useClearanceFor.contains("" + event.getCode())) {
			if (event instanceof Alarm) {
				this.persistentAlarms.add(event.getCode());
				return false;
			}
		}

		return false;
	}

	private boolean isOidString(String oidElement) {
		if (oidElement == null || oidElement.isEmpty()) return false;

		if (!oidElement.contains(".")) return false;

		String elements[] = oidElement.split(".");
		for (String octet : elements) {
			try {
				Byte.parseByte(octet);
			} catch (NumberFormatException e) {
				LOGGER.error("WARNING OID Parsing");
				return false;
			}
		}
		return true;
	}

	@PluginFactory
	public static SnmpAppender createAppender(@PluginAttribute("name") final String name,
			@PluginAttribute("ignoreExceptions") final String ignore,
			@PluginAttribute("managementHost") final String managementHost,
			@PluginAttribute("managementPort") final String managementPort,
			@PluginAttribute("communityString") final String communityString,
			@PluginAttribute("applicationTrapOid") final String applicationTrapOid,
			@PluginAttribute("nodeNameOid") final String nodeNameOid, @PluginAttribute("nodeName") final String nodeName,
			@PluginAttribute("nodeLocationOid") final String nodeLocationOid,
			@PluginAttribute("nodeLocation") final String nodeLocation,
			@PluginAttribute("errorCodeOid") final String errorCodeOid,
			@PluginAttribute("errorDescriptionOid") final String errorDescriptionOid,
			@PluginAttribute("severityOid") final String severityOid,
			@PluginAttribute("timestampOid") final String timestampOid,
			@PluginAttribute("timestampFormat") final String timestampFormat,
			@PluginAttribute("additionalTextOid") final String additionalTextOid,
			@PluginAttribute("stackTraceOid") final String stackTraceOid,
			@PluginAttribute("useClearanceFor") final String useClearanceFor,
			@PluginElement("Layout") Layout<? extends Serializable> layout,
			@PluginElement("Filters") final Filter filter, @PluginAttribute("advertise") final String advertise,
			@PluginConfiguration final Configuration config) {

		SnmpAppender appender = new SnmpAppender(name, filter, layout, Boolean.parseBoolean(ignore));

		if (managementHost != null && !managementHost.isEmpty()) {
			appender.managementHost = managementHost;
			try {
				appender.nmsHost = InetAddress.getByName(managementHost);	
			} catch (UnknownHostException e) {
				LOGGER.error("NMS Integration is not configured properly - managementHost");
				appender.isConfigured = false;
			}
		} else {
			LOGGER.error("NMS Integration is not configured properly - managementHost");
			appender.isConfigured = false;
		}

		if (managementPort != null && !managementPort.isEmpty()) {
			try {
				appender.managementPort = managementPort;
				appender.nmsPort = Integer.parseInt(managementPort);
			} catch (NumberFormatException e) {
				LOGGER.error("NMS Integration is not configured properly - managementPort");
				appender.isConfigured = false;
			}
		} else {
			LOGGER.error("NMS Integration is not configured properly - managementPort");
			appender.isConfigured = false;
		}

		if (communityString != null && !communityString.isEmpty())
			appender.communityString = communityString;
		else {
			LOGGER.error("NMS Integration is not configured properly - communityString");
			appender.isConfigured = false;
		}

		if (applicationTrapOid != null && !applicationTrapOid.isEmpty() && appender.isOidString(applicationTrapOid)) {
			appender.applicationTrapOid = applicationTrapOid;
		}
		else {
			LOGGER.error("NMS Integration is not configured properly - applicationTrapOid");
			appender.isConfigured = false;
		}

		if (nodeNameOid != null && !nodeNameOid.isEmpty() && appender.isOidString(nodeNameOid))
			appender.nodeNameOid = nodeNameOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - nodeNameOid");
			appender.isConfigured = false;
		}

		if (nodeName != null && !nodeName.isEmpty())
			appender.nodeName = nodeName;
		else {
			LOGGER.error("NMS Integration is not configured properly - nodeName");
			appender.isConfigured = false;
		}

		if (nodeLocationOid != null && !nodeLocationOid.isEmpty() && appender.isOidString(nodeLocationOid))
			appender.nodeLocationOid = nodeLocationOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - nodeLocationOid");
			appender.isConfigured = false;
		}

		if (nodeLocation != null && !nodeLocation.isEmpty())
			appender.nodeLocation = nodeLocation;
		else {
			LOGGER.error("NMS Integration is not configured properly - nodeLocation");
			appender.isConfigured = false;
		}

		if (errorCodeOid != null && !errorCodeOid.isEmpty() && appender.isOidString(errorCodeOid))
			appender.errorCodeOid = errorCodeOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - errorCodeOid");
			appender.isConfigured = false;
		}

		if (errorDescriptionOid != null && !errorDescriptionOid.isEmpty() && appender.isOidString(errorDescriptionOid))
			appender.errorDescriptionOid = errorDescriptionOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - errorDescriptionOid");
			appender.isConfigured = false;
		}

		if (severityOid != null && !severityOid.isEmpty() && appender.isOidString(severityOid))
			appender.severityOid = severityOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - severityOid");
			appender.isConfigured = false;
		}

		if (timestampOid != null && !timestampOid.isEmpty() && appender.isOidString(timestampOid))
			appender.timestampOid = timestampOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - timestampOid");
			appender.isConfigured = false;
		}

		if (timestampFormat != null && !timestampFormat.isEmpty())
			appender.timestampFormat = timestampFormat;
		else {
			LOGGER.error("NMS Integration is not configured properly - timestampFormat");
			appender.isConfigured = false;
		}

		if (additionalTextOid != null && !additionalTextOid.isEmpty() && appender.isOidString(additionalTextOid))
			appender.additionalTextOid = additionalTextOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - additionalTextOid");
			appender.isConfigured = false;
		}

		if (stackTraceOid != null && !stackTraceOid.isEmpty() && appender.isOidString(stackTraceOid))
			appender.stackTraceOid = stackTraceOid;
		else {
			LOGGER.error("NMS Integration is not configured properly - stackTraceOid");
			appender.isConfigured = false;
		}

		if (useClearanceFor != null && !useClearanceFor.isEmpty())
			appender.useClearanceFor = useClearanceFor;
		else
			appender.useClearanceFor = "none";

		
		appender.isConfigured = true;
		
		return appender;
	}
	
	

	@Override
	public boolean isStarted() {
		return (super.isStarted() && this.isConfigured);
	}

	@Override
	public void start() {
		super.start();
		
		this.timestampFormatter = new SimpleDateFormat(timestampFormat);
		
		// prepare SNMP Transport
		try {
			this.mapping = new DefaultUdpTransportMapping();
			this.snmpStack = new Snmp(this.mapping);
			
			this.target = new CommunityTarget();
			this.target.setVersion(SnmpConstants.version1);
			this.target.setAddress(new UdpAddress(this.nmsHost, this.nmsPort));
			this.target.setCommunity(new OctetString(this.communityString));
			
		} catch (IOException e) {
			LOGGER.fatal("Unable to initialize SNMP Transport. SNMP Alarms turned off!!!");
			this.isConfigured = false;
		}
		this.isConfigured = true;

	}

	@Override
	public void stop() {
		try {
			this.snmpStack.close();
		} catch (IOException e) {
			LOGGER.info("SNMP Stack failed to shutdown... Ignoring the message since the cleanup will anyway effect in lieu of lifecycle.");
		}
		
		super.stop();
	}

	public String getManagementHost() {
		return managementHost;
	}

	public String getManagementPort() {
		return managementPort;
	}

	public String getCommunityString() {
		return communityString;
	}

	public String getApplicationTrapOid() {
		return applicationTrapOid;
	}

	public String getNodeNameOid() {
		return nodeNameOid;
	}

	public String getNodeName() {
		return nodeName;
	}

	public String getNodeLocationOid() {
		return nodeLocationOid;
	}

	public String getNodeLocation() {
		return nodeLocation;
	}

	public String getErrorCodeOid() {
		return errorCodeOid;
	}

	public String getErrorDescriptionOid() {
		return errorDescriptionOid;
	}

	public String getSeverityOid() {
		return severityOid;
	}

	public String getTimestampOid() {
		return timestampOid;
	}

	public String getTimestampFormat() {
		return timestampFormat;
	}

	public String getAdditionalTextOid() {
		return additionalTextOid;
	}

	public String getUseClearanceFor() {
		return useClearanceFor;
	}

}
