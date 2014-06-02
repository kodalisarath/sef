package com.ericsson.raso.sef.smart.processor;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.RequestContextLocalStore;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.smart.ErrorCode;
import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.subscription.response.PurchaseResponse;
import com.ericsson.raso.sef.smart.subscription.response.RequestCorrelationStore;
import com.ericsson.raso.sef.smart.usecase.RechargeRequest;
import com.ericsson.sef.bes.api.entities.Meta;
import com.ericsson.sef.bes.api.entities.Product;
import com.ericsson.sef.bes.api.subscription.ISubscriptionRequest;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResponseData;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.CommandResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.OperationResult;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ParameterList;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.TransactionResult;


public class CARecharge implements Processor {
	
	private static final Logger logger = LoggerFactory.getLogger(CARecharge.class);

	@Override
	public void process(Exchange arg0) throws SmException {
		
		try{
			
		
		RechargeRequest rechargeRequest = (RechargeRequest) arg0.getIn().getBody();
		
		if (rechargeRequest.getEventClass() == null) {
			throw new SmException(new ResponseCode(500, "Recharge Type is not defined"));
		}
		
		// Prepare parameters for transaction engine purchase
		String msisdn = rechargeRequest.getCustomerId();
		String offerid= null;
		String requestId = null;
		Map<String, String> metas = null;
		
		if (msisdn == null) {
			throw new SmException(new ResponseCode(8002, "CustomerId or AccesKey is not defined in input parameter"));
		}
		
		
		//TODO: Subscriber validation/caching goes here
		
		String eventClass = rechargeRequest.getEventClass();
		if (eventClass.equals("predefined") || eventClass.equals("unli")) {
			metas = prepareRecharge(rechargeRequest);
			offerid = rechargeRequest.getEventName();
		} else if (eventClass.equals("flexible")) {
			metas = prepareFlexibleRecharge(rechargeRequest);
			offerid = rechargeRequest.getRatingInput1();
		} else if (eventClass.equals("pasaload")) {
			rechargeRequest.setRatingInput0("pasaload");
			metas = prepareRecharge(rechargeRequest);
			offerid = rechargeRequest.getEventName();
		} else if (eventClass.equals("reversal")) {
			metas = prepareReversalRecharge(rechargeRequest);
			offerid = rechargeRequest.getEventName();;
		} else {
			throw new SmException(new ResponseCode(500, "Recharge Type is not defined"));
		}
		
		requestId = RequestContextLocalStore.get().getRequestId();
		ISubscriptionRequest subscriptionRequest = SmartServiceResolver.getSubscriptionRequest();
		PurchaseResponse response = new PurchaseResponse();
		List<Meta> listMeta=convertToList(metas);
		String correlationId = subscriptionRequest.purchase(requestId, offerid, msisdn, true, listMeta);
		
		try {
			synchronized (response) {
				RequestCorrelationStore.put(correlationId, response);
				response.wait(10000L);
			}
		} catch(InterruptedException e) {
			logger.debug("sleep interrupted. may be response arrived!!!");
		}
		
		logger.debug("Awake from sleep.. going to check response in store with id: " +  correlationId);
		
		PurchaseResponse purchaseResponse = (PurchaseResponse) RequestCorrelationStore.get(correlationId);
		
		if(purchaseResponse == null) {
			//request timed out but no response. possible request missing from correlation store
			// there is no response time out error code in smart interface and hence throw internal server error
			logger.debug("No response arrived???");
			throw new SmException(ErrorCode.internalServerError);
		}
		
		logger.debug("Response purchase received.. now creating front end response");
		CommandResponseData responseData = createResponse(rechargeRequest.isTransactional(),purchaseResponse);
		arg0.getOut().setBody(responseData);
		
		} catch(Exception e) {
			e.printStackTrace();
			throw new SmException(ErrorCode.internalSystemError);
		}
		
	}
	
	
	private Map<String,String> prepareRecharge(RechargeRequest rechargeRequest) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());

		if (rechargeRequest.getRatingInput0() != null) {
			map.put(Constants.CHANNEL_NAME, rechargeRequest.getRatingInput0());
			map.put(Constants.EX_DATA3, rechargeRequest.getRatingInput0());
		}

		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		map.put(SmartConstants.USECASE, "recharge");

		return map;

	}

	private Map<String, String> prepareReversalRecharge(RechargeRequest rechargeRequest) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());

		if (rechargeRequest.getRatingInput0() != null) {
			map.put(Constants.CHANNEL_NAME, rechargeRequest.getRatingInput0());
			map.put(Constants.EX_DATA3, rechargeRequest.getRatingInput0());
		}
		
		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		map.put(SmartConstants.USECASE, "reversal");

		return map;
	}


	private Map<String, String> prepareFlexibleRecharge(RechargeRequest rechargeRequest) {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put(Constants.TX_AMOUNT, rechargeRequest.getAmountOfUnits().toString());
		map.put(Constants.CHANNEL_NAME, rechargeRequest.getRatingInput0());
		map.put(Constants.EX_DATA3, rechargeRequest.getRatingInput0());
		map.put(SmartConstants.EXPIRY_POLICY, rechargeRequest.getRatingInput2());

		int expiryDatePolicy = Integer.valueOf(rechargeRequest.getRatingInput2());
		switch (expiryDatePolicy) {
		case 0://absolute
			map.put(SmartConstants.EXPIRY_DATE, rechargeRequest.getRatingInput4());
			break;
		case 2://No change to expiry date. days of extension = 0
			map.put(SmartConstants.EXPIRY_DAYS_OF_EXTENSION, String.valueOf(0));
			break;
		case 3://releative to current date (make it absolute) 
		case 1://releative to expiry date
		case 4:// 
			map.put(SmartConstants.EXPIRY_DAYS_OF_EXTENSION, rechargeRequest.getRatingInput3());
			break;
		default:
			break;
		}

		map.put(Constants.EX_DATA1, rechargeRequest.getEventName());
		if(rechargeRequest.getEventInfo() != null) {
			map.put(Constants.EX_DATA2, rechargeRequest.getEventInfo());
		}
		map.put(SmartConstants.USECASE, "recharge");

		return map;
	}
	
	
	private CommandResponseData createResponse(boolean isTransactional, PurchaseResponse response) throws SmException {
		CommandResponseData responseData = new CommandResponseData();
		CommandResult result = new CommandResult();
		responseData.setCommandResult(result);
		OperationResult operationResult = new OperationResult();
		Operation operation = new Operation();
		operation.setName("Recharge");
		operationResult.getOperation().add(operation);
		ParameterList parameterList = new ParameterList();
		operation.setParameterList(parameterList);
		ListParameter listParameter = new ListParameter();
		listParameter.setName("RechargedBalances");
		parameterList.getParameterOrBooleanParameterOrByteParameter().add(listParameter);

		if(isTransactional) {
			TransactionResult transactionResult = new TransactionResult();
			result.setTransactionResult(transactionResult);
			transactionResult.getOperationResult().add(operationResult);
		} else {
			result.setOperationResult(operationResult);
		}
		
		//convert resulted products to SMART response
		
		List<Product> products = response.getProducts();
		if(products != null) {
		for (Product product: products) {
			StringElement stringElement = new StringElement();
			String offer = product.getResourceName();
			int offerId = Integer.parseInt(offer);
			String name = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offer);
			String walletName = name;
			logger.debug("OfferID: " + offerId + "WalletName: " + name);
			
			long delta = product.getQuotaConsumed() - product.getQuotaDefined();
			long curr = product.getQuotaConsumed();
			long validity = product.getValidity();
			logger.debug("Current bal: " + curr + "Delta: " + delta + "Validity: " + validity);
			if(offerId != SmartConstants.AIRTIME_OFFER_ID && offerId != SmartConstants.ALKANSYA_OFFER_ID) {
				name += ":s_PeriodicBonus";
			}
			
			
			if(offerId >= SmartConstants.UNLI_OFFER_START_ID) {
				delta = 1;
				curr = 1;
			} else {
				String conversionFactor = SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletConversionFactor", walletName);
				logger.debug("Conversion factor for this offer: " + conversionFactor);
				long confec= Long.parseLong(conversionFactor); 
				delta = delta/confec;
				curr = curr/confec;
			}
			
			String val = name + ";" + delta + ";" + curr + ";" + getMillisToDate(validity);
			
			logger.debug("Balance String: " + val);
			
			stringElement.setValue(val);
			listParameter.getElementOrBooleanElementOrByteElement().add(stringElement);
		}
		} else if(response.getFault().getCode() > 0) {
			logger.info("No products found in the response");
			ResponseCode responseCode=new ResponseCode(response.getFault().getCode(), response.getFault().getDescription());
			throw new SmException(responseCode);
		}
		
		return responseData;
	}
	
	private String getMillisToDate(long millis) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		return df.format(calendar.getTime());
	}
	
	/*Method to convert a map to a list*/
	private List<Meta> convertToList(Map<String,String> metas){
		List<Meta> metaList = new ArrayList<Meta>();
		for(String metaKey:metas.keySet()){
			Meta meta=new Meta();
			meta.setKey(metaKey);
			meta.setValue(metas.get(metaKey));
			metaList.add(meta);
		}
		return metaList;
		
	}
}
