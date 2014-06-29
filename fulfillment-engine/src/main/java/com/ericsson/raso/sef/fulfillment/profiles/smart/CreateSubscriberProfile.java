package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.af.command.AddDnsCommand;
import com.ericsson.raso.sef.client.af.command.DeleteDnsCommand;
import com.ericsson.raso.sef.client.af.request.AddDnsRequest;
import com.ericsson.raso.sef.client.af.request.DeleteDnsRequest;
import com.ericsson.raso.sef.client.air.command.DeleteSubscriberCommand;
import com.ericsson.raso.sef.client.air.command.InstallSubscriberCommand;
import com.ericsson.raso.sef.client.air.request.DeleteSubscriberRequest;
import com.ericsson.raso.sef.client.air.request.InstallSubscriberRequest;
import com.ericsson.raso.sef.core.RangeRouter;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.config.Value;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.raso.sef.fulfillment.profiles.DnsUpdateProfile;
import com.ericsson.raso.sef.fulfillment.profiles.PamInformationList;
import com.ericsson.sef.bes.api.entities.Product;

public class CreateSubscriberProfile extends BlockingFulfillment<Product> {
	private static final long	serialVersionUID	= 4571643634014244954L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateSubscriberProfile.class);

	// CS-AF specific
	private String zname;
	private String rdata;
	private int dtype;
	private int dclass;
	private int ttl;

	// ACIP specific
	private Boolean messageCapabilityFlag;
	private Integer promotionPlanId;
	private Integer serviceClassNew;
	private Integer languageIDNew;
	private Boolean temporaryBlockedFlag;
	private Integer ussdEndOfCallNotificationID;
	private String originOperatorId;
	
	private PamInformationList pamInformationList;
	
	public CreateSubscriberProfile(String name) {
		super(name);
	}
	
	
	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		LOGGER.debug("Entering fulfill of CreateSubscriberProfile...");
		CreateSubscriberProfile createSubscriberProfile= new CreateSubscriberProfile(originOperatorId);
	
		List<Product> returned = new ArrayList<Product>();
		
		//TODO: After the requisite Router implementation, move this to smfe and cleanup....
		LOGGER.debug("Fetching the sdpId from Router");
		Value route = null;
		try {
			route = RangeRouter.getInstance().getRoute("SMART_router", Long.parseLong(map.get("SUBSCRIBER_ID")));
		} catch (IllegalStateException e1) {
			LOGGER.error("Seems like unknown range for installed subscriber. Better stop now!!");
			throw new FulfillmentException("ffe", new ResponseCode(4011, "Target SDP identification not configured. Check Router!!"), e1);
		}
		String sdpId = null;
		String siteName = null;
		
		  
		if (route == null) {
			sdpId = (String) SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "sdpId");
			siteName =  (String) SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultSite");
		} else {
			sdpId = route.getSdpId();
			siteName = route.getSiteId();
		}

		
		// AF Install - function here
		LOGGER.debug("Preparing request pojo for CS-AF....");
		AddDnsRequest dnsRequest = new AddDnsRequest();
		dnsRequest.setMsisdn(map.get("SUBSCRIBER_ID"));
		dnsRequest.setDclass(this.getDclass());
		dnsRequest.setDtype(this.getDtype());
		dnsRequest.setRdata(this.getRdata());
		dnsRequest.setTtl(this.getTtl());
		dnsRequest.setZname(this.getZname());
		dnsRequest.setSdpId(sdpId);
		dnsRequest.setSiteId(siteName);
		
		try {
			LOGGER.debug("Shall I fire the command to CS-AF DNS?");
			new AddDnsCommand(dnsRequest).execute();
			LOGGER.debug("Installed new subscriber in CS-AF DNS");
		} catch (SmException e1) {
			LOGGER.error("Failed AddDnsCommand execute" + e1.getMessage(), e);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
				
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		LOGGER.debug("Configred defaultServiceClass: " + defaultServiceClass);
		
		InstallSubscriberRequest request = new InstallSubscriberRequest();
		if(createSubscriberProfile.getPromotionPlanId() != null) 
			request.setPromotionPlanId(createSubscriberProfile.getPromotionPlanId());
		LOGGER.debug("Requesting for Promotion Plan: " + createSubscriberProfile.getPromotionPlanId());
		
		if(createSubscriberProfile.getLanguageIDNew() !=null) 
			request.setLanguageIDNew(createSubscriberProfile.getLanguageIDNew());
		LOGGER.debug("Requesting for Language ID: " + createSubscriberProfile.getLanguageIDNew());
		
		if(createSubscriberProfile.getMessageCapabilityFlag() != null) 
			request.setMessageCapabilityFlag(createSubscriberProfile.getMessageCapabilityFlag());
		LOGGER.debug("Requesting for MessageCapablityFlag: " + createSubscriberProfile.getMessageCapabilityFlag());
		
		if(request.getPamInformationList() != null) 
			request.setPamInformationList(createSubscriberProfile.getPamInformationList().getPamInfolist());
		LOGGER.debug("Requesting for Pam Info: " + createSubscriberProfile.getPamInformationList());
		
		
		request.setServiceClassNew(Integer.valueOf(defaultServiceClass));
		LOGGER.debug("Requesting for Service Class: " + defaultServiceClass);
		
		if(request.getTemporaryBlockedFlag() !=null) 
			request.setTemporaryBlockedFlag(createSubscriberProfile.getTemporaryBlockedFlag());
		LOGGER.debug("Requesting for Temporary Blocked Flag: " + createSubscriberProfile.getTemporaryBlockedFlag());
		
		
		if(request.getUssdEndOfCallNotificationID() !=null) 
			request.setUssdEndOfCallNotificationID(createSubscriberProfile.getUssdEndOfCallNotificationID());
		LOGGER.debug("Requesting for USSD End of Call Notification: " + createSubscriberProfile.getUssdEndOfCallNotificationID());
		
		request.setSubscriberNumber(map.get("SUBSCRIBER_ID"));
		InstallSubscriberCommand instlCommand = new InstallSubscriberCommand(request);
		LOGGER.debug("Packed the request for execution..." + map.get("SUBSCRIBER_ID"));
		
		try {
			instlCommand.execute();
			LOGGER.debug("Subscriber installed in CS-AIR");
		} catch (SmException e1) {
			LOGGER.error("Installing new subscriber failed!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
		e.setMetas(map);
		returned.add(e);
		return returned;	
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		LOGGER.debug("Starting rollback of CS-AF installed subscriber...");
		
		List<Product> returned = new ArrayList<Product>();
		
		DnsUpdateProfile dnsUpdateProfile=new DnsUpdateProfile(zname);
		DeleteDnsRequest deleteDnsRequest = new DeleteDnsRequest();
		deleteDnsRequest.setMsisdn(map.get("SUBSCRIBER_ID"));
		deleteDnsRequest.setDclass(dnsUpdateProfile.getDclass());
		deleteDnsRequest.setDtype(dnsUpdateProfile.getDtype());
		deleteDnsRequest.setSiteId(null);
		deleteDnsRequest.setTtl(dnsUpdateProfile.getTtl());
		deleteDnsRequest.setZname(dnsUpdateProfile.getZname());
		
		try {
			new DeleteDnsCommand(deleteDnsRequest).execute();
			LOGGER.debug("Installed subcriber in CS-AF is now rolledback...");
		} catch (SmException e1) {
			LOGGER.error("SmException while calling DeleteDnsCommand execute" + e1);
		}
		
		
		LOGGER.debug("Starting rollback of CS-AIR installed subscriber...");
		
		CreateSubscriberProfile createSubscriberProfile= new CreateSubscriberProfile(originOperatorId);
		DeleteSubscriberRequest request = new DeleteSubscriberRequest();
		request.setSubscriberNumber(map.get("SUBSCRIBER_ID"));
		request.setOriginOperatorId(createSubscriberProfile.getOriginOperatorId());
		try {
			new DeleteSubscriberCommand(request).execute();
			LOGGER.debug("Subscriber install rolledback in CS-AIR");
		} catch (SmException e1) {
			LOGGER.error("Exception when calling execute" + e1);
		}
		
		
		returned.add(e);
		return returned;	
	}
	
	public String getZname() {
		return zname;
	}


	public void setZname(String zname) {
		this.zname = zname;
	}


	public String getRdata() {
		return rdata;
	}


	public void setRdata(String rdata) {
		this.rdata = rdata;
	}


	public int getDtype() {
		return dtype;
	}


	public void setDtype(int dtype) {
		this.dtype = dtype;
	}


	public int getDclass() {
		return dclass;
	}


	public void setDclass(int dclass) {
		this.dclass = dclass;
	}


	public int getTtl() {
		return ttl;
	}


	public void setTtl(int ttl) {
		this.ttl = ttl;
	}


	public Boolean getMessageCapabilityFlag() {
		return messageCapabilityFlag;
	}

	public void setMessageCapabilityFlag(Boolean messageCapabilityFlag) {
		this.messageCapabilityFlag = messageCapabilityFlag;
	}

	public Integer getPromotionPlanId() {
		return promotionPlanId;
	}

	public void setPromotionPlanId(Integer promotionPlanId) {
		this.promotionPlanId = promotionPlanId;
	}

	public Integer getServiceClassNew() {
		return serviceClassNew;
	}

	public void setServiceClassNew(Integer serviceClassNew) {
		this.serviceClassNew = serviceClassNew;
	}

	public Integer getLanguageIDNew() {
		return languageIDNew;
	}

	public void setLanguageIDNew(Integer languageIDNew) {
		this.languageIDNew = languageIDNew;
	}

	public Boolean getTemporaryBlockedFlag() {
		return temporaryBlockedFlag;
	}

	public void setTemporaryBlockedFlag(Boolean temporaryBlockedFlag) {
		this.temporaryBlockedFlag = temporaryBlockedFlag;
	}

	public Integer getUssdEndOfCallNotificationID() {
		return ussdEndOfCallNotificationID;
	}

	public void setUssdEndOfCallNotificationID(Integer ussdEndOfCallNotificationID) {
		this.ussdEndOfCallNotificationID = ussdEndOfCallNotificationID;
	}

	public String getOriginOperatorId() {
		return originOperatorId;
	}

	public void setOriginOperatorId(String originOperatorId) {
		this.originOperatorId = originOperatorId;
	}

	public PamInformationList getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(PamInformationList pamInformationList) {
		this.pamInformationList = pamInformationList;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dclass;
		result = prime * result + dtype;
		result = prime * result + ((languageIDNew == null) ? 0 : languageIDNew.hashCode());
		result = prime * result + ((messageCapabilityFlag == null) ? 0 : messageCapabilityFlag.hashCode());
		result = prime * result + ((originOperatorId == null) ? 0 : originOperatorId.hashCode());
		result = prime * result + ((pamInformationList == null) ? 0 : pamInformationList.hashCode());
		result = prime * result + ((promotionPlanId == null) ? 0 : promotionPlanId.hashCode());
		result = prime * result + ((rdata == null) ? 0 : rdata.hashCode());
		result = prime * result + ((serviceClassNew == null) ? 0 : serviceClassNew.hashCode());
		result = prime * result + ((temporaryBlockedFlag == null) ? 0 : temporaryBlockedFlag.hashCode());
		result = prime * result + ttl;
		result = prime * result + ((ussdEndOfCallNotificationID == null) ? 0 : ussdEndOfCallNotificationID.hashCode());
		result = prime * result + ((zname == null) ? 0 : zname.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CreateSubscriberProfile other = (CreateSubscriberProfile) obj;
		if (dclass != other.dclass)
			return false;
		if (dtype != other.dtype)
			return false;
		if (languageIDNew == null) {
			if (other.languageIDNew != null)
				return false;
		} else if (!languageIDNew.equals(other.languageIDNew))
			return false;
		if (messageCapabilityFlag == null) {
			if (other.messageCapabilityFlag != null)
				return false;
		} else if (!messageCapabilityFlag.equals(other.messageCapabilityFlag))
			return false;
		if (originOperatorId == null) {
			if (other.originOperatorId != null)
				return false;
		} else if (!originOperatorId.equals(other.originOperatorId))
			return false;
		if (pamInformationList == null) {
			if (other.pamInformationList != null)
				return false;
		} else if (!pamInformationList.equals(other.pamInformationList))
			return false;
		if (promotionPlanId == null) {
			if (other.promotionPlanId != null)
				return false;
		} else if (!promotionPlanId.equals(other.promotionPlanId))
			return false;
		if (rdata == null) {
			if (other.rdata != null)
				return false;
		} else if (!rdata.equals(other.rdata))
			return false;
		if (serviceClassNew == null) {
			if (other.serviceClassNew != null)
				return false;
		} else if (!serviceClassNew.equals(other.serviceClassNew))
			return false;
		if (temporaryBlockedFlag == null) {
			if (other.temporaryBlockedFlag != null)
				return false;
		} else if (!temporaryBlockedFlag.equals(other.temporaryBlockedFlag))
			return false;
		if (ttl != other.ttl)
			return false;
		if (ussdEndOfCallNotificationID == null) {
			if (other.ussdEndOfCallNotificationID != null)
				return false;
		} else if (!ussdEndOfCallNotificationID.equals(other.ussdEndOfCallNotificationID))
			return false;
		if (zname == null) {
			if (other.zname != null)
				return false;
		} else if (!zname.equals(other.zname))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "CreateSubscriberProfile [zname=" + zname + ", rdata=" + rdata + ", dtype=" + dtype + ", dclass=" + dclass + ", ttl=" + ttl
				+ ", messageCapabilityFlag=" + messageCapabilityFlag + ", promotionPlanId=" + promotionPlanId + ", serviceClassNew="
				+ serviceClassNew + ", languageIDNew=" + languageIDNew + ", temporaryBlockedFlag=" + temporaryBlockedFlag
				+ ", ussdEndOfCallNotificationID=" + ussdEndOfCallNotificationID + ", originOperatorId=" + originOperatorId
				+ ", pamInformationList=" + pamInformationList + "]";
	}

	


}
