package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.List;
import java.util.Map;

import com.ericsson.raso.sef.client.af.command.AddDnsCommand;
import com.ericsson.raso.sef.client.af.request.AddDnsRequest;
import com.ericsson.raso.sef.client.air.command.DeleteSubscriberCommand;
import com.ericsson.raso.sef.client.air.command.InstallSubscriberCommand;
import com.ericsson.raso.sef.client.air.request.DeleteSubscriberRequest;
import com.ericsson.raso.sef.client.air.request.InstallSubscriberRequest;
import com.ericsson.raso.sef.core.RequestContext;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.sef.bes.api.entities.Product;

public class CreateSubscriberProfile extends BlockingFulfillment<Product> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public List<Product> fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
		CreateSubscriberProfile createSubscriberProfile= new CreateSubscriberProfile(originOperatorId);
		
		AddDnsRequest dnsRequest = new AddDnsRequest();
		dnsRequest.setMsisdn(map.get("msisdn"));		
		
		
		RequestContext requestContext = new RequestContext();
		
		String sdpId = requestContext.getInterfaceName();
		dnsRequest.setSdpId(sdpId);
		
		try {
			new AddDnsCommand(dnsRequest).execute();
		} catch (SmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
				
		String defaultServiceClass = SefCoreServiceResolver.getConfigService().getValue("GLOBAL", "defaultServiceClass");
		
		//String defaultServiceClass = FederationProfileContext.getProperty(FederationProfileContext.DEFAULT_SERVICE_CLASS);
		
		InstallSubscriberRequest request = new InstallSubscriberRequest();
		if(createSubscriberProfile.getPromotionPlanId() != null) {
			request.setPromotionPlanId(createSubscriberProfile.getPromotionPlanId());
		}
		
		if(createSubscriberProfile.getLanguageIDNew() !=null) {
			request.setLanguageIDNew(createSubscriberProfile.getLanguageIDNew());
		}
		
		if(createSubscriberProfile.getMessageCapabilityFlag() != null) {
			request.setMessageCapabilityFlag(createSubscriberProfile.getMessageCapabilityFlag());
		}
		if(request.getPamInformationList() != null) {
			request.setPamInformationList(createSubscriberProfile.getPamInformationList().getPamInfolist());
		}
		
		request.setServiceClassNew(Integer.valueOf(defaultServiceClass));
		
		if(request.getTemporaryBlockedFlag() !=null) {
			request.setTemporaryBlockedFlag(createSubscriberProfile.getTemporaryBlockedFlag());
		}
		
		if(request.getUssdEndOfCallNotificationID() !=null) {
			request.setUssdEndOfCallNotificationID(createSubscriberProfile.getUssdEndOfCallNotificationID());
		}
		request.setSubscriberNumber(map.get("msisdn"));
		InstallSubscriberCommand instlCommand = new InstallSubscriberCommand(request);
		try {
			instlCommand.execute();
		} catch (SmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Product> prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		CreateSubscriberProfile createSubscriberProfile= new CreateSubscriberProfile(originOperatorId);
		DeleteSubscriberRequest request = new DeleteSubscriberRequest();
		request.setSubscriberNumber(map.get("msisdn"));
		request.setOriginOperatorId(createSubscriberProfile.getOriginOperatorId());
		try {
			new DeleteSubscriberCommand(request).execute();
		} catch (SmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
