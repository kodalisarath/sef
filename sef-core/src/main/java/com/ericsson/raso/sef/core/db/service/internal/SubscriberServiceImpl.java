package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Arrays;
//import java.util.Base64;
//import java.util.Base64.Decoder;
//import java.util.Base64.Encoder;
//import org.apache.commons.codec.binary.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SecureSerializationHelper;
import com.ericsson.raso.sef.core.db.mapper.SubscriberMapper;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class SubscriberServiceImpl implements SubscriberService {
	private static final Logger logger = LoggerFactory.getLogger(SubscriberServiceImpl.class);
	private static final SecureSerializationHelper encryptor = new SecureSerializationHelper();
	//private static final Encoder encoder = Base64.getEncoder();
	//private static final Decoder decoder = Base64.getDecoder();
	
	// constants section
	static final int ApplicationContextError = 9000;
	static final int FunctionalDataError = 9500;
	static final int InfrastructureError = 9600;
	static final int ConnectionError = 9700;
	static final int TransientError = 9950;
	static final int PersistentError = 9970;
	static final int CriticalError = 9990;

	private SubscriberMapper subscriberMapper;
	
	
	public void setSubscriberMapper(SubscriberMapper subscriberMapper) {
		this.subscriberMapper = subscriberMapper;
	}
	
	@Override
	@Transactional
	public boolean createSubscriber(String nbCorellator, Subscriber subscriber) throws PersistenceError {
       
		if (subscriber == null)
			throw new PersistenceError(nbCorellator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The subscriber entity provided was null!!"));
		
		if (subscriber.getMsisdn() == null || subscriber.getMsisdn().isEmpty())
			throw new PersistenceError(nbCorellator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'msisdn' attribute was null and it is PK/CK in 'Subscriber' entity!!"));
			
		if (subscriber.getPin() == null || subscriber.getPin().isEmpty()) {
			logger.warn("The 'pin' attribute was null. Assuming to default '1234'");
			subscriber.setPin("1234");
		}
		
		if (subscriber.getUserId() == null || subscriber.getUserId().isEmpty()) {
			logger.warn("The 'userId' attribute was null. Assuming to default 'msisdn'");
			subscriber.setUserId(subscriber.getMsisdn());
		}
		
		if (subscriber.getAccountId() == null || subscriber.getAccountId().isEmpty()) {
			logger.warn("The 'accountId' attribute was null. Assuming to default 'msisdn'");
			subscriber.setAccountId(subscriber.getMsisdn());
		}
		
		if (subscriber.getCustomerId() == null || subscriber.getCustomerId().isEmpty()) {
			logger.warn("The 'customerId' attribute was null. Assuming to default 'msisdn'");
			subscriber.setCustomerId(subscriber.getMsisdn());
		}
		
		if (subscriber.getContractId() == null || subscriber.getContractId().isEmpty()) {
			logger.warn("The 'contractId' attribute was null. Assuming to default 'msisdn'");
			subscriber.setContractId(subscriber.getMsisdn());
		}
		
		// Perform encryption of identities...
		try {
			subscriber.setAccountId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getAccountId()))));
			subscriber.setContractId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getContractId()))));
			subscriber.setCustomerId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getCustomerId()))));
			subscriber.setEmail(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getEmail()))));
			subscriber.setImeiSv(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getImeiSv()))));
			subscriber.setImsi(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getImsi()))));
			subscriber.setMsisdn(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getMsisdn()))));
			subscriber.setPaymentParent(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPaymentParent()))));
			subscriber.setPaymentResponsible(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPaymentResponsible()))));
			subscriber.setPin(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPin()))));
			subscriber.setUserId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getUserId()))));

		} catch (FrameworkException e) {
			logger.error(nbCorellator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorellator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		
		try {
			logger.debug("Crossing fingers... About to insert subscriber:" + subscriber);
			subscriberMapper.createSubscriber(subscriber);
			this.createMetas(nbCorellator, subscriber.getUserId(), subscriber.getMetas());
			
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorellator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to insert Subscriber entity!!"), e);
		}
		return true;
	}

	
	@Override
	@Transactional
	public boolean deleteSubscriber(String nbCorrelator, String userId) throws PersistenceError {
		logger.debug(nbCorrelator, "Quick scan on persitent entity: " + userId);
		
		String subscriberId = null;
		try {
			subscriberId = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId)));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		
		try {
			subscriberMapper.deleteSubscriber(subscriberId);
			logger.debug(nbCorrelator, "Subscriber: " + userId + " was successfully deleted!!");
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to delete Subscriber entity!!"), e);
		}
		
		return true;
	}

	
	@Override
	@Transactional
	public boolean updateSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError {
		
		logger.debug("Sanity checks on the data...");
		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The subscriber entity provided was null!!"));
		
		if (subscriber.getMsisdn() == null || subscriber.getMsisdn().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'msisdn' attribute is null and it is PK/CK in 'Subscriber' entity!!"));
			
		if (subscriber.getPin() == null || subscriber.getPin().isEmpty()) 
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'pin' attribute is null!!"));
				
		if (subscriber.getUserId() == null || subscriber.getUserId().isEmpty()) 
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'userId' attribute is null!!"));
		
		if (subscriber.getAccountId() == null || subscriber.getAccountId().isEmpty()) 
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'accountId' attribute is null!!"));
		
		if (subscriber.getCustomerId() == null || subscriber.getCustomerId().isEmpty()) 
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'customerId' attribute is null!!"));
		
		if (subscriber.getContractId() == null || subscriber.getContractId().isEmpty()) 
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError, "The 'contractId' attribute is null!!"));
		
		
		// Perform encryption of identities...
		try {
			logger.debug(nbCorrelator, "Encrypting Identities now...");
			subscriber.setAccountId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getAccountId()))));
			subscriber.setContractId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getContractId()))));
			subscriber.setCustomerId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getCustomerId()))));
			subscriber.setEmail(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getEmail()))));
			subscriber.setImeiSv(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getImeiSv()))));
			subscriber.setImsi(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getImsi()))));
			subscriber.setMsisdn(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getMsisdn()))));
			subscriber.setPaymentParent(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPaymentParent()))));
			subscriber.setPaymentResponsible(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPaymentResponsible()))));
			subscriber.setPin(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPin()))));
			subscriber.setUserId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getUserId()))));
			logger.debug(nbCorrelator, "Encrypted Identities to ensure info security");
		} catch (FrameworkException e) {
			logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		
		
		// fetch the current entity...
		Subscriber currentEntity = null;
		try {
			currentEntity = subscriberMapper.getSubscriberByUserId(subscriber.getUserId());
			subscriberMapper.updateSubscriber(subscriber);
			this.updateMetas(nbCorrelator, subscriber.getUserId(), currentEntity.getMetas());
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);
		}
		
		// select the list of changes to audit trail...
//		Collection<SubscriberAuditTrial> newAuditTrails = new ArrayList<SubscriberAuditTrial>();
		
//		if(currentEntity.getContractState() != null && currentEntity.getContractState() != subscriber.getContractState()) {
//			SubscriberAuditTrial historyEvent = new SubscriberAuditTrial(subscriber.getUserId(), 
//																		new Date(), 
//																		Subscriber.CONTRACT_STATE, 
//																		subscriber.getContractState().name(),
//																		"system-user");
//			newAuditTrails.add(historyEvent);
//		}
	
//		try {
//			subscriberMapper.updateSubscriber(subscriber);
//			for (SubscriberAuditTrial subscriberHistory : newAuditTrails) {
//				subscriberMapper.insertSubscriberHistory(subscriberHistory);
//			}
//			List<Meta> listMetas=new ArrayList<Meta>(subscriber.getMetas());
//			boolean isMetasSet=setMeta(nbCorrelator,subscriber.getUserId(),listMetas);
//		} catch (PersistenceException e) {
//			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
//			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);
//		}
		return true;
	}
//Made this method as a private method to invoke when a subscriber is created or updated
	
	
	private boolean setMeta(String nbCorrelator, String userId, List<Meta> metas) throws PersistenceError {
		logger.debug("Method setMetas is  called");

		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		if(metas == null || metas.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'metaKeys' provided was null!!"));

		
		Collection<SubscriberAuditTrial> newAuditTrail = new ArrayList<SubscriberAuditTrial>();
		List<String> metaKeys = this.getKeysAsList(metas);
		Collection<Meta> currentMetas = this.getMetas(nbCorrelator, userId, metaKeys);
		logger.debug("current metas values are of size " + currentMetas.size());

		for(Meta meta: metas) {
			logger.debug("Processing meta: " + meta);
			if(meta.getValue() == null || meta.getValue().trim().length() == 0) 
				continue;

			boolean isUpdate = false;
			for(Meta currentMeta: currentMetas) {
				if(meta.getKey().equals(currentMeta.getKey())) {
					/**
					 * Audit Trail will be ignored temporarily until all core requisite functionalit is out...
					 */
//					SubscriberAuditTrial history = new SubscriberAuditTrial(userId, 
//																			new Date(), 
//																			meta.getKey(), 
//																			meta.getValue(), 
//																			"system-user");
					try {
						SubscriberMeta subscriberMeta = new SubscriberMeta(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))),
														meta.getKey(), meta.getValue());
						subscriberMapper.updateSubscriberMeta(subscriberMeta);
					} catch (PersistenceException e) {
						logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
						throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);					
					} catch (FrameworkException e) {
						logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
						throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
					}
				}
			}
		}
		return true;
	}
	
	//this is being called from some methods,need some clarity to refactor this
	@Override
	@Transactional
	public boolean updateMetas(String nbCorrelator, String userId, Collection<Meta> metas) throws PersistenceError {
		logger.debug("Method setMetas is  called");

		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		if(metas == null || metas.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'metaKeys' provided was null!!"));

		
		Collection<SubscriberAuditTrial> newAuditTrail = new ArrayList<SubscriberAuditTrial>();
		List<String> metaKeys = this.getKeysAsList(metas);
		Collection<Meta> currentMetas = this.getMetas(nbCorrelator, userId, metaKeys);
		logger.debug("current metas values are of size " + currentMetas.size());

		for(Meta meta: metas) {
			logger.debug("Processing meta: " + meta);
			if(meta.getValue() == null || meta.getValue().trim().length() == 0) 
				continue;

			boolean isUpdate = false;
			for(Meta currentMeta: currentMetas) {
				if(meta.getKey().equals(currentMeta.getKey())) {
					/**
					 * Audit Trail is ignored until core functionality is thru...
					 */
//					SubscriberAuditTrial history = new SubscriberAuditTrial(userId, 
//																			new Date(), 
//																			meta.getKey(), 
//																			meta.getValue(), 
//																			"system-user");
					try {
						SubscriberMeta subscriberMeta = new SubscriberMeta(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))),
																			meta.getKey(), meta.getValue());
						subscriberMapper.updateSubscriberMeta(subscriberMeta);
						logger.debug("Meta updated: " + subscriberMeta);
					} catch (PersistenceException e) {
						logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
						throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);					
					} catch (FrameworkException e) {
						logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
						throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
					}
				}
			}
		}
		return true;
	}

	@Override
	@Transactional
	public boolean createMetas(String nbCorrelator, String userId, Collection<Meta> metas) throws PersistenceError {
		logger.debug("Method setMetas is  called");

		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		if(metas == null || metas.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'metaKeys' provided was null!!"));

		
//		Collection<SubscriberAuditTrial> newAuditTrail = new ArrayList<SubscriberAuditTrial>();
		List<String> metaKeys = this.getKeysAsList(metas);
		Collection<Meta> currentMetas = this.getMetas(nbCorrelator, userId, metaKeys);
		logger.debug("current metas values are of size " + currentMetas.size());

		for(Meta meta: metas) {
			logger.debug("Processing meta: " + meta);
			if(meta == null || meta.getKey() == null || meta.getValue() == null || meta.getValue().trim().length() == 0) 
				continue;

			boolean isUpdate = false;
			for(Meta currentMeta: currentMetas) {
				if(meta.getKey().equals(currentMeta.getKey())) {
					/**
					 * Audit Trail is ignored until core functionality is thru...
					 */
//					SubscriberAuditTrial history = new SubscriberAuditTrial(userId, 
//																			new Date(), 
//																			meta.getKey(), 
//																			meta.getValue(), 
//																			"system-user");
					try {
						SubscriberMeta subscriberMeta = new SubscriberMeta(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))),
																			meta.getKey(), meta.getValue());
						subscriberMapper.insertSubscriberMeta(subscriberMeta);
						logger.debug("Meta updated: " + subscriberMeta);
					} catch (PersistenceException e) {
						logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
						throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);					
					} catch (FrameworkException e) {
						logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
						throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public List<Meta> getMetas(String nbCorrelator, String userId, List<String> metaKeys) throws PersistenceError {
		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		if(metaKeys == null || metaKeys.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'metaKeys' provided was null!!"));

		Subscriber subscriber = this.fetchSubscriberByUserId(nbCorrelator, userId);
		if (subscriber == null) 
			subscriber = this.fetchSubscriberByMsisdn(nbCorrelator, userId);
		
		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "A subscriber meta cannot be found for the given 'userId'!!"));
			
		
		return this.translateMetas(subscriber.getMetas());
	}

	@Override
	public Subscriber getSubscriber(String nbCorrelator, String userId, List<String> metaKeys) throws PersistenceError {
		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		if(metaKeys == null || metaKeys.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'metaKeys' provided was null!!"));

		Subscriber subscriber = this.fetchSubscriberByUserId(nbCorrelator, userId);
		if (subscriber == null) 
			subscriber = this.fetchSubscriberByMsisdn(nbCorrelator, userId);
		
		/*if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "A subscriber cannot be found for the given 'userId'!!"));*/
		
		return subscriber;
	}
	
	@Override
	public List<SubscriberAuditTrial> getSubscriberHistory(String nbCorrelator, String userId, List<String> metaKeys) throws PersistenceError {
		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		if(metaKeys == null || metaKeys.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'metaKeys' provided was null!!"));

		String subscriberId = null;
		try {
			subscriberId = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId)));
		} catch(FrameworkException e) {
			logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", subscriberId);
		map.put("keys", Arrays.asList(metaKeys));
		
		Collection<SubscriberAuditTrial> subscriberHistory = subscriberMapper.getSubscriberHistory(map);
		if (map.containsKey("userId"))
			map.put("userId", userId);
		
		return this.translateHistory(subscriberHistory);
	}
	
	@Override
	public Subscriber getSubscriberByUserId(String nbCorrelator, String userId) throws PersistenceError {
		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));

		Subscriber subscriber = this.fetchSubscriberByUserId(nbCorrelator, userId);
		if (subscriber == null) 
			subscriber = this.fetchSubscriberByMsisdn(nbCorrelator, userId);
		
		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "A subscriber cannot be found for the given 'userId'!!"));
		
		return subscriber;
	}
	
	
	private Subscriber fetchSubscriberByMsisdn(String nbCorrelator, String msisdn) throws PersistenceError {
		if(msisdn == null || msisdn.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'msisdn' provided was null!!"));

		String subscriberId = null;
		try {
			subscriberId = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(msisdn)));
		} catch(FrameworkException e) {
			logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		
		Subscriber subscriber = null;
		try { 
			subscriber = subscriberMapper.getSubscriber(subscriberId);
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);					
		}
		
		if(subscriber != null) {
			Collection<SubscriberMeta> subscriberMetas = this.fetchMetas(msisdn);
			for (SubscriberMeta metaSubscriber : subscriberMetas) {
				Meta meta=new Meta();
				meta.setKey(metaSubscriber.getKey());
				meta.setValue(metaSubscriber.getValue());
				subscriber.getMetas().add(meta);
			}
			subscriber.getMetas().add(new Meta("SUBSCRIBER_ID", subscriber.getUserId()));
		}
		return subscriber;
	}
	
	private Subscriber fetchSubscriberByUserId(String nbCorrelator, String userId) throws PersistenceError {
		if(userId == null || userId.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'userId' provided was null!!"));
		
		String subscriberId = null;
		try {
			subscriberId = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId)));
		} catch(FrameworkException e) {
			logger.error(nbCorrelator, "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		
		Subscriber subscriber = null;
		try { 
			subscriber = subscriberMapper.getSubscriberByUserId(subscriberId);
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);					
		}
		
		if(subscriber != null) {
			Collection<SubscriberMeta> subscriberMetas = this.fetchMetas(userId);
			for (SubscriberMeta metaSubscriber : subscriberMetas) {
				Meta meta=new Meta();
				meta.setKey(metaSubscriber.getKey());
				meta.setValue(metaSubscriber.getValue());
				subscriber.getMetas().add(meta);
			}
			subscriber.getMetas().add(new Meta("SUBSCRIBER_ID", subscriber.getUserId()));
		}
		return subscriber;
	}
	
	private Collection<SubscriberMeta> fetchMetas(String userId) {
		logger.debug("Inside the method to fetch metas for subscriber: " + userId);
		
		Collection<SubscriberMeta> metas = null;
		try {
		metas = subscriberMapper.getAllSubscriberMetas(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))));
		} catch (PersistenceException e) {
			logger.error("DB execution failed fetching metas. Cause: " + e.getMessage(), e);
		} catch (FrameworkException e) {
			logger.error("DB preparation failed fetching metas. Cause: " + e.getMessage(), e);
			return new ArrayList<SubscriberMeta>();			
		}
		
		if (metas == null)
			return new ArrayList<SubscriberMeta>();
		else
			return metas;
	}

	@Override
	public Subscriber getSubscriber(String nbCorrelator, String msisdn) throws PersistenceError {
		if(msisdn == null || msisdn.isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'msisdn' provided was null!!"));

		
		Subscriber subscriber = this.fetchSubscriberByUserId(nbCorrelator, msisdn);
		if (subscriber == null) 
			subscriber = this.fetchSubscriberByMsisdn(nbCorrelator, msisdn);
		
//		if (subscriber == null)
//			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError, "A subscriber cannot be found for the given 'userId'!!"));
		
		return subscriber;
	}
	


	private List<String> getKeysAsList(Collection<Meta> metas) {
		List<String> keys = new ArrayList<String>();
		for (String key: keys)
			keys.add(key);
		return keys;
	}
	
	private Collection<String> getKeysAsCollection(Collection<Meta> metas) {
		Collection<String> keys = new ArrayList<String>();
		for (String key: keys)
			keys.add(key);
		return keys;
	}
	
	private List<Meta> translateMetas(Collection<Meta> metas) {
		List<Meta> returned = new ArrayList<Meta>();
		returned.addAll(metas);
		return returned;
	}

	private List<SubscriberAuditTrial> translateHistory(Collection<SubscriberAuditTrial> history) {
		List<SubscriberAuditTrial> returned = new ArrayList<SubscriberAuditTrial>();
		returned.addAll(history);
		return returned;
	}

	@Override
	public boolean isSubscriberExist(String subscriber) throws PersistenceError {
		
		Boolean returnValue;
		Integer countSubscriber;
		if(subscriber == null){
	logger.error("The 'msisdn' provided was null!!");
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'msisdn' provided was null!!"));
		}else if(subscriber.isEmpty()){
			logger.error("The 'msisdn' provided was empty!!");
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(ApplicationContextError, "The 'msisdn' provided was empty!!"));
		}
		String msisdn=null;
		try {
			msisdn = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber)));
		} catch(FrameworkException e) {
			logger.error("null", "Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(InfrastructureError, "Failed to encrypt Subscriber identities!!"), e);
		}
		try {
		countSubscriber=subscriberMapper.isSubscriberExists(msisdn);
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause:",e);
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);		
		}
		if(countSubscriber != null){
			  returnValue=Boolean.TRUE;
		}
		else{
			returnValue=Boolean.FALSE;
		}
		return returnValue;
	}


}
