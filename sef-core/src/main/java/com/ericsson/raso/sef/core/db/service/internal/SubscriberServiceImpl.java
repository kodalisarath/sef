package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.Meta;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SecureSerializationHelper;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.db.mapper.SubscriberMapper;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.core.db.model.Subscriber;
import com.ericsson.raso.sef.core.db.model.SubscriberAuditTrial;
import com.ericsson.raso.sef.core.db.model.SubscriberMeta;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.SubscriberService;

public class SubscriberServiceImpl implements SubscriberService {
	private static final Logger logger = LoggerFactory.getLogger(SubscriberServiceImpl.class);

	private static final SecureSerializationHelper encryptor = new SecureSerializationHelper();
	// private static final Encoder encoder = Base64.getEncoder();
	// private static final Decoder decoder = Base64.getDecoder();

	// constants section
	static final int ApplicationContextError = 9000;
	static final int FunctionalDataError = 9500;
	static final int InfrastructureError = 9600;
	static final int ConnectionError = 9700;
	static final int TransientError = 9950;
	static final int PersistentError = 9970;
	static final int CriticalError = 9990;
	static final int SubscriberStateError = 9100;
	private SubscriberMapper subscriberMapper;

	public void setSubscriberMapper(SubscriberMapper subscriberMapper) {
		this.subscriberMapper = subscriberMapper;
	}

	@Override
	public boolean isSubscriberExist(String subscriber) throws PersistenceError {
		Boolean returnValue;
		Integer countSubscriber;
		if (subscriber == null) {
			logger.error("The 'msisdn' provided was null!!");
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The 'msisdn' provided was null!!"));
		} else if (subscriber.isEmpty()) {
			logger.error("The 'msisdn' provided was empty!!");
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The 'msisdn' provided was empty!!"));
		}
		String msisdn = subscriber;
		// String msisdn = null;
		// try {
		// msisdn = new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber)));
		// } catch (FrameworkException e) {
		// logger.error("null","Could not prepare entity for persistence. Cause: Encrypting Identities",e);
		// throw new PersistenceError(null, this.getClass().getName(),new
		// ResponseCode(InfrastructureError,"Failed to encrypt Subscriber identities!!"), e);
		// }
		try {
			countSubscriber = subscriberMapper.isSubscriberExists(msisdn);

		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause:", e);
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(InfrastructureError, e.getMessage()), e);
		}
		if (countSubscriber != null) {
			returnValue = Boolean.TRUE;
		} else {
			returnValue = Boolean.FALSE;
		}
		return returnValue;
	}

	@Override
	public boolean createSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError {

		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The subscriber entity provided was null!!"));

		if (subscriber.getMsisdn() == null || subscriber.getMsisdn().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'msisdn' attribute was null and it is PK/CK in 'Subscriber' entity!!"));

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

		subscriber.setContractState(ContractState.PREACTIVE);

		// Perform encryption of identities...
		// try {
		// subscriber.setAccountId(new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getAccountId()))));
		// subscriber.setContractId(new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getContractId()))));
		// subscriber.setCustomerId(new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getCustomerId()))));
		// subscriber.setEmail(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getEmail()))));
		// subscriber.setImeiSv(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getImeiSv()))));
		// subscriber.setImsi(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getImsi()))));
		// subscriber.setMsisdn(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getMsisdn()))));
		// subscriber.setPaymentParent(new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPaymentParent()))));
		// subscriber.setPaymentResponsible(new
		// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPaymentResponsible()))));
		// subscriber.setPin(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getPin()))));
		// subscriber.setUserId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getUserId()))));
		// subscriber.setContractState(ContractState.PREACTIVE);
		//
		// } catch (FrameworkException e) {
		// logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities",e);
		// throw new PersistenceError(nbCorrelator, this.getClass().getName(),new
		// ResponseCode(InfrastructureError,"Failed to encrypt Subscriber identities!!"), e);
		// }
		
		SqlSession oracle = null;
		
		try {
			logger.debug("Overrridng SPRING DM to secure the SQL Factory directly!!");
			SqlSessionFactory springSqlFactory = SefCoreServiceResolver.getSqlSessionFactory();
			logger.debug("Attemptng to secure Oracle Session directly. Hopefully from DBCP and wont fail....");
			oracle = springSqlFactory.openSession(true);
			logger.debug("Only for this method, bypass default mapper to link the instance of mapper to connect with the local session...");
			SubscriberMapper subscriberPersistence = oracle.getMapper(SubscriberMapper.class);

			
			logger.debug("Crossing fingers... About to insert subscriber:" + subscriber);
			subscriberPersistence.createSubscriber(subscriber);
			List<Meta> metaList=(List<Meta>) subscriber.getMetas();
			if(metaList != null){
				if(!metaList.isEmpty()){
					logger.debug("Metas gonna insert are of size:"+metaList.size());
					for(Meta meta:metaList){
						SubscriberMeta subscriberMeta=new SubscriberMeta();
						subscriberMeta.setKey(meta.getKey());
						subscriberMeta.setValue(meta.getValue());
						subscriberMeta.setSubscriberId(subscriber.getMsisdn());
						subscriberPersistence.insertSubscriberMeta(subscriberMeta);
					}
				}
			}
			// }

			// this.createMetas(nbCorellator, subscriber.getUserId(),subscriber.getMetas());
			logger.debug("Here is where we force the oracle to commit!!");
			oracle.commit(true);

		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to insert Subscriber entity!!"), e);
		} finally {
			logger.debug("Here is where we relinquish and cleanup...");
			oracle.close();
		}
		return true;
	}

	@Override
	public boolean updateSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError {

		logger.debug("Sanity checks on the data...");
		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The subscriber entity provided was null!!"));

		if (subscriber.getMsisdn() == null || subscriber.getMsisdn().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'msisdn' attribute is null and it is PK/CK in 'Subscriber' entity!!"));

		if (subscriber.getPin() == null || subscriber.getPin().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'pin' attribute is null!!"));

		if (subscriber.getUserId() == null || subscriber.getUserId().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'userId' attribute is null!!"));

		if (subscriber.getAccountId() == null || subscriber.getAccountId().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'accountId' attribute is null!!"));

		if (subscriber.getCustomerId() == null || subscriber.getCustomerId().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'customerId' attribute is null!!"));

		if (subscriber.getContractId() == null || subscriber.getContractId().isEmpty())
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(FunctionalDataError,
					"The 'contractId' attribute is null!!"));

		String msisdn = subscriber.getMsisdn();
		Subscriber subscriberDB = null;
		Collection<SubscriberMeta> metas = null;
		try {
			// subscriberDB=subscriberMapper.getSubscriber(new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getMsisdn()))));
			// metas = subscriberMapper.getSubscriberMetas(new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getMsisdn()))));

			subscriberMapper.updateSubscriber(subscriber);
			subscriberDB = subscriberMapper.getSubscriber(subscriber.getMsisdn());
			metas = subscriberMapper.getSubscriberMetas(subscriber.getMsisdn());
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to get Subscriber !!"), e);
		}
		// catch(FrameworkException e1){
		// logger.error(nbCorrelator,"Could not prepare msisdn for persistence. in updating subscriber Cause: Encrypting msisdn",e1);
		// throw new PersistenceError(nbCorrelator, this.getClass().getCanonicalName(),new
		// ResponseCode(InfrastructureError,"Failed to encrypt msisdn identities!!"), e1);
		// }
		if (subscriberDB != null) {
			List<Meta> metasFromDB = convertToMetaList(metas);
			for (Meta meta : subscriber.getMetas()) {
				if (meta != null && meta.getKey() != null) {
					if (metasFromDB.contains(meta)) {
						try {
							updateMeta(nbCorrelator, msisdn, meta);
						} catch (PersistenceError e) {
							logger.error("Error in the updatemeta at Service impl. Value: " + meta);
						}
					} else {
						try {
							createMeta(nbCorrelator, msisdn, meta);
						} catch (PersistenceError e) {
							logger.error("Error in the createmeta at Service Impl. Value: " + meta);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean deleteSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError {
		logger.debug("In the delete subscriber method ");
		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The subscriber entity provided was null!!"));
		try {
			// subscriber.setUserId(new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getUserId()))));
			subscriber.setContractState(ContractState.READY_TO_DELETE);
			subscriberMapper.changeContractStatus(subscriber);
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to delete subscriber meta with the provided userid"), e);
		}
		// catch(FrameworkException e1){
		// logger.error(nbCorrelator,"Could not prepare msisdn for persistence. in updating subscriber Cause: Encrypting msisdn",e1);
		// throw new PersistenceError(nbCorrelator, this.getClass().getCanonicalName(),new
		// ResponseCode(InfrastructureError,"Failed to encrypt msisdn identities!!"), e1);
		// }
		return true;
	}

	@Override
	public boolean fulldeleteSubscriber(String nbCorrelator, Subscriber subscriber) throws PersistenceError {
		logger.debug("In the delete subscriber method ");
		if (subscriber == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The subscriber entity provided was null!!"));
		try {
			// subscriber.setUserId(new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(subscriber.getUserId()))));
			subscriber.setContractState(ContractState.DUNNING);
			subscriberMapper.changeContractStatus(subscriber);
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to delete subscriber meta with the provided userid"), e);
		}
		// catch(FrameworkException e1){
		// logger.error(nbCorrelator,"Could not prepare msisdn for persistence. in updating subscriber Cause: Encrypting msisdn",e1);
		// throw new PersistenceError(nbCorrelator, this.getClass().getCanonicalName(),new
		// ResponseCode(InfrastructureError,"Failed to encrypt msisdn identities!!"), e1);
		// }
		return true;
	}

	@Override
	public List<SubscriberAuditTrial> getSubscriberHistory(String nbCorrelator, String userId, List<String> metaKeys)
			throws PersistenceError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscriber getSubscriber(String nbCorrelator, String msisdn, List<String> metaKeys) throws PersistenceError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Subscriber getSubscriberByUserId(String nbCorrelator, String userId) throws PersistenceError {
		// TODO Auto-generated method stub
		return null;
	}

	// This is the method called for update subscriber to check if subscriber exists or not
	@Override
	public Subscriber getSubscriber(String nbCorrelator, String msisdn) throws PersistenceError {
		logger.debug("In the Method getSubscriber to  retrieve subscriber while updating ");

		// profiling on SMART's request..
		long startTime = System.currentTimeMillis();
		if (msisdn == null) {
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The subscriber entity provided was null!!"));
		}
		Subscriber subscriber = null;
		try {
			// try {
			logger.debug("Parameter msisdn" + msisdn);
			// logger.debug("Parameter msisdn after encryption"+ new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(msisdn))));
			// subscriber=subscriberMapper.getSubscriber(new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(msisdn))));
			subscriber = subscriberMapper.getSubscriber(msisdn);
			// } catch (FrameworkException e) {
			// logger.error(nbCorrelator,"Could not prepare msisdn for persistence. Cause: Encrypting msisdn",e);
			// throw new PersistenceError(nbCorrelator, this.getClass().getName(),new
			// ResponseCode(InfrastructureError,"Failed to encrypt msisdn identities!!"), e);
			// }
			if (subscriber != null) {
				Collection<SubscriberMeta> metas = subscriberMapper.getSubscriberMetas(subscriber.getMsisdn());
				List<Meta> metaList = convertToMetaList(metas);
				subscriber.setMetas(metaList);
			}

		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to get subscriber with the provided msisdn"), e);
		}
		if (subscriber != null) {
			subscriber.setMsisdn(msisdn);
			subscriber.setAccountId(msisdn);
			subscriber.setContractId(msisdn);
			subscriber.setCustomerId(msisdn);
			subscriber.setUserId(msisdn);
			subscriber.setPin("1234");
		}

		long endTime = System.currentTimeMillis();
		logger.error("DB LATENCY ON READ SUBSCRIBER: " + (endTime - startTime));
		return subscriber;
	}

	private Subscriber getSubscriber(String msisdn) throws PersistenceError {
		if (msisdn == null)
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The msisdn entity provided was null!!"));
		Subscriber readSubscriber = null;
		try {
			readSubscriber = subscriberMapper.getSubscriber(msisdn);
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to get subscriber with the provided msisdn in getSubscriber"), e);
		}
		return readSubscriber;

	}

	@Override
	public boolean createMetas(String nbCorrelator, String userId, Collection<Meta> metas) throws PersistenceError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateMetas(String nbCorrelator, String userId, Collection<Meta> metas) throws PersistenceError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Meta> getMetas(String nbCorrelator, String userId, List<String> metaKeys) throws PersistenceError {
		if (userId == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"UserId  provided was null!!"));
		Collection<SubscriberMeta> metas = new ArrayList<SubscriberMeta>();
		List<Meta> metaList = new ArrayList<Meta>();
		try {
			// metas= (List<SubscriberMeta>) subscriberMapper.getSubscriberMetas(new
			// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))));
			metas = (List<SubscriberMeta>) subscriberMapper.getSubscriberMetas(userId);
			if (!metas.isEmpty()) {
				metaList = convertToMetaList(metas);
			}

		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(null, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to get subscriber metas with the  provided userid in getSubscriber"), e);
			// } catch (FrameworkException e) {
			// logger.error(nbCorrelator,"Could not prepare userid for querying in updating subscriber Cause: Encrypting msisdn",e);
			// throw new PersistenceError(nbCorrelator, this.getClass().getCanonicalName(),new
			// ResponseCode(InfrastructureError,"Failed to encrypt msisdn identities!!"), e);
		}
		return metaList;
	}

	private List<Meta> convertToMetaList(Collection<SubscriberMeta> subscriberMetas) {
		List<Meta> metaList = new ArrayList<Meta>();
		for (SubscriberMeta metaSubscriber : subscriberMetas) {
			Meta meta = new Meta();
			meta.setKey(metaSubscriber.getKey());
			meta.setValue(metaSubscriber.getValue());
			metaList.add(meta);
		}
		return metaList;
	}

	@Override
	public boolean createMeta(String nbCorrelator, String userId, Meta meta) throws PersistenceError {
		if(meta == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(ApplicationContextError,"The meta provided was null!!"));

		if(userId == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(ApplicationContextError,"The userid  provided was null!!"));
		try {
			SubscriberMeta susbscriberMeta=new SubscriberMeta();
			susbscriberMeta.setSubscriberId(userId);
			if(meta.getKey() != null && meta.getValue() != null){
				susbscriberMeta.setKey(meta.getKey());
				susbscriberMeta.setValue(meta.getValue());
				//			try {
				//				susbscriberMeta.setSubscriberId(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))));
				//			} catch (FrameworkException e) {
				//				logger.error(nbCorrelator,"Could not prepare userid for querying in updating subscriber Cause: Encrypting userid",e);
				//				throw new PersistenceError(nbCorrelator, this.getClass().getCanonicalName(),new ResponseCode(InfrastructureError,"Failed to encrypt userid identities!!"), e);
				//			}
				subscriberMapper.insertSubscriberMeta(susbscriberMeta);
			}

		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to insert subscriber meta with the provided msisdn"), e);
		}

		return true;
	}

	@Override
	public boolean updateMeta(String nbCorrelator, String userId, Meta meta) throws PersistenceError {
		if (userId == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The meta  provided was null!!"));
		if (meta == null)
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(ApplicationContextError,
					"The userid  provided was null!!"));
		try {
			SubscriberMeta susbscriberMeta = new SubscriberMeta();
			susbscriberMeta.setSubscriberId(userId);
			if (meta.getKey() != null && meta.getValue() != null) {
				susbscriberMeta.setKey(meta.getKey());
				susbscriberMeta.setValue(meta.getValue());
				logger.debug(meta.getKey() + " " + meta.getValue() + "ANOTHER SK");
				// try {
				// susbscriberMeta.setSubscriberId(new
				// String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(userId))));
				// } catch (FrameworkException e) {
				// logger.error(nbCorrelator,"Could not prepare userid for querying in updating subscriber Cause: Encrypting userid",e);
				// throw new PersistenceError(nbCorrelator, this.getClass().getCanonicalName(),new
				// ResponseCode(InfrastructureError,"Failed to encrypt userid identities!!"), e);
				// }
				subscriberMapper.updateSubscriberMeta(susbscriberMeta);
			}
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: " + e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(), new ResponseCode(InfrastructureError,
					"Failed to insert subscriber meta with the provided msisdn"), e);
		}
		return true;
	}

}
