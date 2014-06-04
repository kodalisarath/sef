package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.client.air.command.DeleteSubscriberCommand;
import com.ericsson.raso.sef.client.air.command.InstallSubscriberCommand;
import com.ericsson.raso.sef.client.air.request.DeleteSubscriberRequest;
import com.ericsson.raso.sef.client.air.request.InstallSubscriberRequest;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.fulfillment.commons.FulfillmentException;
import com.ericsson.sef.bes.api.entities.Product;

public class CreateSubscriberProfile extends BlockingFulfillment<Product> {
	private static final long	serialVersionUID	= 4571643634014244954L;
	private static final Logger LOGGER = LoggerFactory.getLogger(CreateSubscriberProfile.class);

	private Boolean messageCapabilityFlag;
	private Integer promotionPlanId;
	private Integer serviceClassNew;
	private Integer languageIDNew;
	private Boolean temporaryBlockedFlag;
	private Integer ussdEndOfCallNotificationID;
	private String originOperatorId;
	
	private PamInformationList pamInformationList;
	
	protected CreateSubscriberProfile(String name) {
		super(name);
	}
	
	
	@Override
	public List<Product> fulfill(Product e, Map<String, String> map) throws FulfillmentException {
		LOGGER.debug("Entering fulfill of CreateSubscriberProfile...");
		CreateSubscriberProfile createSubscriberProfile= new CreateSubscriberProfile(originOperatorId);
	
		/*
		 * The following piece of code is commented since DnsUpdateProfile is available and the same is expected to be wired as a 
		 * Fulfillment Profile attached to a resource under a Workflow Offer.
		 */
		
//		AddDnsRequest dnsRequest = new AddDnsRequest();
//		dnsRequest.setMsisdn(map.get("msisdn"));		
//		
//		
//		RequestContext requestContext = new RequestContext();
//		
//		String sdpId = requestContext.getInterfaceName();
//		dnsRequest.setSdpId(sdpId);
//		
//		try {
//			new AddDnsCommand(dnsRequest).execute();
//		} catch (SmException e1) {
//			LOGGER.error("Adding DNS Entry for the new subscriber failed!!", e1);
//			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
//		}
		
				
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		LOGGER.debug("Configred defaultServiceClass: " + defaultServiceClass);
		
		InstallSubscriberRequest request = new InstallSubscriberRequest();
		if(createSubscriberProfile.getPromotionPlanId() != null) 
			request.setPromotionPlanId(createSubscriberProfile.getPromotionPlanId());
		
		if(createSubscriberProfile.getLanguageIDNew() !=null) 
			request.setLanguageIDNew(createSubscriberProfile.getLanguageIDNew());
		
		if(createSubscriberProfile.getMessageCapabilityFlag() != null) 
			request.setMessageCapabilityFlag(createSubscriberProfile.getMessageCapabilityFlag());

		if(request.getPamInformationList() != null) 
			request.setPamInformationList(createSubscriberProfile.getPamInformationList().getPamInfolist());
		
		
		request.setServiceClassNew(Integer.valueOf(defaultServiceClass));
		
		if(request.getTemporaryBlockedFlag() !=null) 
			request.setTemporaryBlockedFlag(createSubscriberProfile.getTemporaryBlockedFlag());
		
		
		if(request.getUssdEndOfCallNotificationID() !=null) 
			request.setUssdEndOfCallNotificationID(createSubscriberProfile.getUssdEndOfCallNotificationID());
		
		request.setSubscriberNumber(map.get("msisdn"));
		InstallSubscriberCommand instlCommand = new InstallSubscriberCommand(request);
		LOGGER.debug("Packed the request for execution...");
		
		try {
			instlCommand.execute();
		} catch (SmException e1) {
			LOGGER.error("Installing new subscriber failed!!", e1);
			throw new FulfillmentException(e1.getComponent(), new ResponseCode(e1.getStatusCode().getCode(), e1.getStatusCode().getMessage()));
		}
		LOGGER.debug("Subscriber installed in CS-AIR");
		
		List<Product> returned = new ArrayList<Product>();
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
		CreateSubscriberProfile createSubscriberProfile= new CreateSubscriberProfile(originOperatorId);
		DeleteSubscriberRequest request = new DeleteSubscriberRequest();
		request.setSubscriberNumber(map.get("msisdn"));
		request.setOriginOperatorId(createSubscriberProfile.getOriginOperatorId());
		try {
			new DeleteSubscriberCommand(request).execute();
		} catch (SmException e1) {
			LOGGER.error("Exception when calling execute" + e1);
		}
		
		LOGGER.debug("Subscriber install rolledback in CS-AIR");
		
		List<Product> returned = new ArrayList<Product>();
		returned.add(e);
		return returned;	
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
		result = prime * result
				+ ((languageIDNew == null) ? 0 : languageIDNew.hashCode());
		result = prime
				* result
				+ ((messageCapabilityFlag == null) ? 0 : messageCapabilityFlag
						.hashCode());
		result = prime
				* result
				+ ((originOperatorId == null) ? 0 : originOperatorId.hashCode());
		result = prime
				* result
				+ ((pamInformationList == null) ? 0 : pamInformationList
						.hashCode());
		result = prime * result
				+ ((promotionPlanId == null) ? 0 : promotionPlanId.hashCode());
		result = prime * result
				+ ((serviceClassNew == null) ? 0 : serviceClassNew.hashCode());
		result = prime
				* result
				+ ((temporaryBlockedFlag == null) ? 0 : temporaryBlockedFlag
						.hashCode());
		result = prime
				* result
				+ ((ussdEndOfCallNotificationID == null) ? 0
						: ussdEndOfCallNotificationID.hashCode());
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
		if (ussdEndOfCallNotificationID == null) {
			if (other.ussdEndOfCallNotificationID != null)
				return false;
		} else if (!ussdEndOfCallNotificationID
				.equals(other.ussdEndOfCallNotificationID))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "CreateSubscriberProfile [messageCapabilityFlag="
				+ messageCapabilityFlag + ", promotionPlanId="
				+ promotionPlanId + ", serviceClassNew=" + serviceClassNew
				+ ", languageIDNew=" + languageIDNew
				+ ", temporaryBlockedFlag=" + temporaryBlockedFlag
				+ ", ussdEndOfCallNotificationID="
				+ ussdEndOfCallNotificationID + ", originOperatorId="
				+ originOperatorId + ", pamInformationList="
				+ pamInformationList + "]";
	}

}
