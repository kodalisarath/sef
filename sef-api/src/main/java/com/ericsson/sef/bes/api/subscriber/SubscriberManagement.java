package com.ericsson.sef.bes.api.subscriber;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface SubscriberManagement {

	@WebMethod(operationName = "createSubscriber")
	void createSubscriber(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "meta") List<Meta> metas,
			@WebParam(name = "passcode") String passcode) throws WSException;

	@WebMethod(operationName = "updateSubscriber")
	void updateSubscriber(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "meta") List<Meta> metas)
			throws WSException;

	@WebMethod(operationName = "changeContractState")
	void changeContractState(
			@WebParam(name = "msisdn") String msisdn,
			@WebParam(name = "contractState") ContractState state,
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

	@WebMethod(operationName = "getSubscriberProfile")
	void getSubscriberProfile(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "key") List<String> keys)
			throws WSException;

	@WebMethod(operationName = "changeMSISDN")
	void changeMSISDN(
			@WebParam(name = "currMsisdn") String currMsisdn, 
			@WebParam(name = "newMsisdn") String newMsisdn,
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

	@WebMethod(operationName = "updateLanguageSettings")
	void updateLanguageSettings(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "lang") String lang,
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

	@WebMethod(operationName = "changeRatePlan")
	void updateRatePlan(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "ratePlan") String ratePlan,
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

	@WebMethod(operationName = "deleteSubscriber")
	void deleteSubscriber(
			@WebParam(name = "msisdn") String msisdn, 
			@WebParam(name = "meta") List<Meta> metas) throws WSException;

}
