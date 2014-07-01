package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.ibatis.exceptions.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.FrameworkException;
import com.ericsson.raso.sef.core.ResponseCode;
import com.ericsson.raso.sef.core.SecureSerializationHelper;
import com.ericsson.raso.sef.core.UniqueIdGenerator;
import com.ericsson.raso.sef.core.db.mapper.smart.CallingCircleMapper;
import com.ericsson.raso.sef.core.db.model.smart.CallingCircle;
import com.ericsson.raso.sef.core.db.service.PersistenceError;
import com.ericsson.raso.sef.core.db.service.smart.CallingCircleService;

public class CallingCircleServiceImpl implements CallingCircleService {
	private static final Logger logger = LoggerFactory.getLogger(CallingCircleServiceImpl.class);
	
	private static final SecureSerializationHelper encryptor = new SecureSerializationHelper();

	static final int ApplicationContextError = 9000;
	static final int FunctionalDataError = 9500;
	static final int InfrastructureError = 9600;
	static final int ConnectionError = 9700;
	static final int TransientError = 9950;
	static final int PersistentError = 9970;
	static final int CriticalError = 9990;
	static final int SubscriberStateError = 9100;

	
	
	private CallingCircleMapper callingCircleMapper;
	
	public void setCallingCircleMapper(CallingCircleMapper callingCircleMapper) {
		this.callingCircleMapper = callingCircleMapper;
	}

	@Override
	public void createCallingCircleMemberMapping(String nbCorrelator, CallingCircle callingCircle) throws PersistenceError {
		if (callingCircle == null) 
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		
		logger.debug("Check before insert: " + callingCircle);
		if (callingCircle.getOwner() == null ||
				callingCircle.getMemberA() == null ||
				callingCircle.getMemberB() == null ||
				callingCircle.getProdcatOffer() == null ||
				callingCircle.getRelationship() == null ||
				callingCircle.getExpiryTime() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with all attributes"));
		}
		
		try {
			callingCircle.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(callingCircle.getOwner()))));
			callingCircle.setMemberA(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(callingCircle.getMemberA()))));
			callingCircle.setMemberB(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(callingCircle.getMemberB()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}
		
		try {
			logger.debug("About to insert... Calling Circle: " + callingCircle);
			callingCircleMapper.createCallingCircleAndMembership(callingCircle);
			logger.debug("DB inserted with Calling Circle...");
		} catch (PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to insert Calling Circle entity!!"), e);
		}
		
	}

	@Override
	public CallingCircle fetchCallingCircleMemberMapping(String nbCorrelator, CallingCircle query) throws PersistenceError {
		if (query == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (query.getOwner() == null ||
				query.getProdcatOffer() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with query attributes"));
		}
		
		
		try {
			query.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getOwner()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}

		
		try {
			CallingCircle returned = callingCircleMapper.fetchCallingCircle(query.getOwner(), query.getProdcatOffer());
			
			if (returned != null) {
				returned.setOwner(query.getOwner());
				returned.setMemberA((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(returned.getMemberA())));
				returned.setMemberB((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(returned.getMemberB())));
			}
			return returned;
			
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not process entity after query persistence. Cause: Decrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to decrypt CallingCircle identities!!"), e);
		}
	}
	
	@Override
	public int fetchCallingCircleMemberCountForOwner(String nbCorrelator, CallingCircle query) throws PersistenceError {
		if (query == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		logger.debug("Query attributes check: " + query);
		if (query.getOwner() == null ||
				query.getProdcatOffer() == null ||
				query.getMemberB() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with query attributes"));
		}
		
		try {
			query.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getOwner()))));
			query.setMemberB(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getMemberB()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}

		try {
			Integer memberCount = callingCircleMapper.fetchCallingCircleMemberCountForOwner(nbCorrelator, query.getOwner(), query.getProdcatOffer(), query.getMemberB());
			
			if (memberCount == null) {
				return 0;
			}
			return memberCount.intValue();
			
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		}

	}
	
	
	
	@Override
	public int fetchCallingCircleMemberCountForMember(String nbCorrelator, CallingCircle query) throws PersistenceError {
		if (query == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (query.getOwner() == null ||
				query.getProdcatOffer() == null ||
				query.getMemberB() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with query attributes"));
		}
		
		try {
			query.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getOwner()))));
			query.setMemberB(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getMemberB()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}

		try {
			Integer memberCount = callingCircleMapper.fetchCallingCircleMemberCountForMember(nbCorrelator, query.getOwner(), query.getProdcatOffer(), query.getMemberB());
			
			if (memberCount == null) {
				return 0;
			}
			return memberCount.intValue();
			
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		}
	}

	@Override
	public Collection<CallingCircle> fetchCallingCircleMembersForOwner(String nbCorrelator, CallingCircle query) throws PersistenceError {
		if (query == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (query.getOwner() == null ||
				query.getProdcatOffer() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with query attributes"));
		}
		
		try {
			query.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getOwner()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}

		try {
			Collection<CallingCircle> members = callingCircleMapper.fetchCallingCircleMembersForOwner(nbCorrelator, query);
			for (CallingCircle member: members) {
				member.setOwner((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(member.getOwner())));
				member.setMemberA((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(member.getMemberA())));
				member.setMemberB((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(member.getMemberB())));
						
			}
			return members;
			
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		} catch (FrameworkException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to decrypt the persistence result"), e);
		}

	}
	
	@Override
	public Collection<String> fetchAllCallingCircleMembers(String nbCorrelator, CallingCircle query) throws PersistenceError {
		if (query == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (query.getOwner() == null ||
				query.getProdcatOffer() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with query attributes"));
		}
		
		try {
			query.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getOwner()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}

		try {
			Collection<String> members = callingCircleMapper.fetchAllCallingCircleMembers(nbCorrelator, query.getOwner(), query.getProdcatOffer());
			Collection<String> membersClear = new ArrayList<String>();
			for (String member: members)
				membersClear.add((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(member)));
			
			return membersClear;
			
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		} catch (FrameworkException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to decrypt the persistence result"), e);
		}

	}

		

	@Override
	public Collection<String> fetchCallingCircleMembersOnly(String nbCorrelator, CallingCircle query) throws PersistenceError {
		if (query == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (query.getOwner() == null ||
				query.getProdcatOffer() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with query attributes"));
		}
		
		try {
			query.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(query.getOwner()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}

		try {
			Collection<String> members = callingCircleMapper.fetchCallingCircleMembersOnly(nbCorrelator, query.getOwner(), query.getProdcatOffer());
			Collection<String> membersClear = new ArrayList<String>();
			for (String member: members)
				membersClear.add((String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(member)));
			
			return membersClear;
			
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		} catch (FrameworkException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to decrypt the persistence result"), e);
		}
	}

	@Override
	public void deleteCallingCircleMemberMapping(String nbCorrelator, CallingCircle delete) throws PersistenceError {
		if (delete == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (delete.getOwner() == null ||
				delete.getProdcatOffer() == null ||
				(delete.getMemberA() == null && delete.getMemberB() != null) || 
				(delete.getMemberA() != null && delete.getMemberB() == null)) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with all attributes"));
		}
		
		try {
			delete.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(delete.getOwner()))));
			if (delete.getMemberA() != null)
				delete.setMemberA(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(delete.getMemberA()))));
			if (delete.getMemberB() != null)
				delete.setMemberB(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(delete.getMemberB()))));
				
				
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}
		
		try {
			 callingCircleMapper.deleteCallingCircle(delete.getOwner(), delete.getProdcatOffer());
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		}
	}


	
	@Override
	public void deleteCallingCircle(String nbCorrelator, CallingCircle delete) throws PersistenceError {
		if (delete == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(ApplicationContextError, "The given value for Calling Circle was null"));
		}
		
		if (delete.getOwner() == null ||
				delete.getProdcatOffer() == null) {
			throw new PersistenceError(nbCorrelator, "Calling Circle", new ResponseCode(FunctionalDataError, "The given value for Calling Circle was empty with all attributes"));
		}
		
		try {
			delete.setOwner(new String(org.apache.commons.codec.binary.Base64.encodeBase64(encryptor.encrypt(delete.getOwner()))));
		} catch (FrameworkException e) {
			logger.error(nbCorrelator,"Could not prepare entity for persistence. Cause: Encrypting Identities", e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to encrypt CallingCircle identities!!"), e);
		}
		
		try {
			 callingCircleMapper.deleteCallingCircle(delete.getOwner(), delete.getProdcatOffer());
		} catch(PersistenceException e) {
			logger.error("Encountered Persistence Error. Cause: "+ e.getCause().getClass().getCanonicalName(), e);
			throw new PersistenceError(nbCorrelator, this.getClass().getName(),new ResponseCode(InfrastructureError,"Failed to get calling circle with the provided query params"), e);
		}
	}

	
	
	
}
