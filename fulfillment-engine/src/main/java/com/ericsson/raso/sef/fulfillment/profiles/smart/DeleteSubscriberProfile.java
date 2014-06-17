package com.ericsson.raso.sef.fulfillment.profiles.smart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.ericsson.raso.sef.client.af.command.AddDnsCommand;
import com.ericsson.raso.sef.client.af.command.DeleteDnsCommand;
import com.ericsson.raso.sef.client.af.request.AddDnsRequest;
import com.ericsson.raso.sef.client.af.request.DeleteDnsRequest;
import com.ericsson.raso.sef.client.air.command.DeleteSubscriberCommand;
import com.ericsson.raso.sef.client.air.command.InstallSubscriberCommand;
import com.ericsson.raso.sef.client.air.request.DeleteSubscriberRequest;
import com.ericsson.raso.sef.client.air.request.InstallSubscriberRequest;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.raso.sef.fulfillment.profiles.BlockingFulfillment;
import com.ericsson.raso.sef.fulfillment.profiles.PamInformationList;
import com.ericsson.sef.bes.api.entities.Product;
import com.hazelcast.logging.LoggerFactory;

public class DeleteSubscriberProfile extends BlockingFulfillment<Product> {

	private static final long serialVersionUID = -6336329642133594137L;
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DeleteSubscriberProfile.class);
	
	// CS-AF specific
	private String zname;
	private String rdata;
	private int dtype;
	private int dclass;
	private int ttl;
	private boolean isInsert = true;


	// ACIP specific
	private Boolean messageCapabilityFlag;
	private Integer promotionPlanId;
	private Integer serviceClassNew;
	private Integer languageIDNew;
	private Boolean temporaryBlockedFlag;
	private Integer ussdEndOfCallNotificationID;
	
	private PamInformationList pamInformationList;
	
	public DeleteSubscriberProfile(String name) {
		super(name);
	}

	
	private String originOperatorId;
	
	
	public String getOriginOperatorId() {
		return originOperatorId;
	}

	public void setOriginOperatorId(String originOperatorId) {
		this.originOperatorId = originOperatorId;
	}

	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		logger.debug("Entering fulfill of DeleteSubscriberProfile...");
		DeleteSubscriberProfile deleteSubscriberProfile = new DeleteSubscriberProfile(originOperatorId);
	
		List<Product> returned = new ArrayList<Product>();
		
		
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		
		logger.debug("Configred defaultServiceClass: " + defaultServiceClass);
		
		DeleteSubscriberRequest request = new DeleteSubscriberRequest();
		
		//request.
		if(deleteSubscriberProfile.getOriginOperatorId() != null) 
			request.setOriginOperatorId(deleteSubscriberProfile.getOriginOperatorId());
	
		request.setSubscriberNumber(map.get("SUBSCRIBER_ID"));
		request.setSubscriberNumberNAI(1);
		//request.setDeleteReasonCode(2);
		logger.info("THIS IS IT");
		request.setDeleteReasonCode(2);
		request.setOriginOperatorID("originOperatorID");
		logger.debug("THE DELETED REASON CODE IS " + request.getDeleteReasonCode());
		DeleteSubscriberCommand deleteCommand = new DeleteSubscriberCommand(request);
		logger.debug("Packed the request for execution..." + map.get("SUBSCRIBER_ID"));
		
		try {
			deleteCommand.execute();
			logger.debug("Subscriber installed in CS-AIR");
		} catch (SmException e1) {
			logger.error("Installing new subscriber failed!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		
		
		// AF Delete - function here
		logger.debug("Preparing request pojo for CS-AF....");
		DeleteDnsRequest dnsRequest = new DeleteDnsRequest();
		dnsRequest.setMsisdn(map.get("SUBSCRIBER_ID"));
		dnsRequest.setDclass(this.getDclass());
		dnsRequest.setDtype(this.getDtype());
		dnsRequest.setTtl(this.getTtl());
		dnsRequest.setZname(this.getZname());
		dnsRequest.setSiteId(SefCoreServiceResolver.getConfigService().getValue("af1","site"));

		logger.debug("Fetching the sdpId from Router");
		String sdpId = (String) RequestContextLocalStore.get().getInProcess().get("sdpId");

		try {
			logger.debug("Shall I fire the command to CS-AF DNS?");
			new DeleteDnsCommand(dnsRequest).execute();
			logger.debug("Deleted SUbscriber in CS-AF DNS");
		} catch (SmException e1) {
			logger.error("Failed AddDnsCommand execute" + e1.getMessage(), e);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode()
					.getMessage()));
		}
		
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((originOperatorId == null) ? 0 : originOperatorId.hashCode());
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
		DeleteSubscriberProfile other = (DeleteSubscriberProfile) obj;
		if (originOperatorId == null) {
			if (other.originOperatorId != null)
				return false;
		} else if (!originOperatorId.equals(other.originOperatorId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeleteSubscriberProfile [originOperatorId=" + originOperatorId
				+ "]";
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

	public boolean isInsert() {
		return isInsert;
	}

	public void setInsert(boolean isInsert) {
		this.isInsert = isInsert;
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

	public void setUssdEndOfCallNotificationID(
			Integer ussdEndOfCallNotificationID) {
		this.ussdEndOfCallNotificationID = ussdEndOfCallNotificationID;
	}

	public PamInformationList getPamInformationList() {
		return pamInformationList;
	}

	public void setPamInformationList(PamInformationList pamInformationList) {
		this.pamInformationList = pamInformationList;
	}
	
}
