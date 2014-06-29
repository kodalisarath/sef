package com.ericsson.raso.sef.core.db.service.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.ericsson.raso.sef.core.db.mapper.AgreementMapper;
import com.ericsson.raso.sef.core.db.mapper.ProviderMapper;
import com.ericsson.raso.sef.core.db.model.Agreement;
import com.ericsson.raso.sef.core.db.model.AgreementDto;
import com.ericsson.raso.sef.core.db.model.AgreementMeta;
import com.ericsson.raso.sef.core.db.model.Provider;
import com.ericsson.raso.sef.core.db.model.ProviderMeta;
import com.ericsson.raso.sef.core.db.service.UserManagementService;

public class UserManagemetServiceImpl implements UserManagementService {

	private ProviderMapper providerMapper;
	private AgreementMapper agreementMapper;

	public void setProviderMapper(ProviderMapper providerMapper) {
		this.providerMapper = providerMapper;
	}

	public void setAgreementMapper(AgreementMapper agreementMapper) {
		this.agreementMapper = agreementMapper;
	}

	@Override
	@Transactional
	public void insertProvider(Provider provider) {
		provider.setPasswordModified(new Date());
		provider.setCreatedTime(new Date());
		provider.setLoginCount(0);
		provider.setStatus("open");
		provider.setState("0");
		providerMapper.insertProvider(provider);
	}

	@Override
	@Transactional
	public void insertProviderMeta(ProviderMeta meta) {
		providerMapper.insertProviderMeta(meta);
	}

	@Override
	@Transactional
	public void updateProvider(Provider provider) {
		providerMapper.updateProvider(provider);
	}

	@Override
	@Transactional
	public void deleteProvider(String providerId) {
		providerMapper.deleteProvider(providerId);
	}

	@Override
	@Transactional
	public void updateProviderMeta(ProviderMeta meta) {
		providerMapper.updateProviderMeta(meta);
	}

	@Override
	public Collection<ProviderMeta> getMetas(String userId, String... metaKeys) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Provider getProviderByProviderId(String providerId) {
		return providerMapper.getProviderByProviderId(providerId);
	}

	@Override
	public ProviderMeta getProviderMetaByProviderId(String providerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Provider> getAllProvider() {
		return providerMapper.getAllProvider();
	}

	@Override
	@Transactional
	public void insertAgreement(Agreement agreement) {
		agreementMapper.insertAgreement(agreement);
		for(AgreementMeta agreementMeta : agreement.getAgreementMetas()){	
			agreementMapper.insertAgreementMeta(agreementMeta);
		}
	}

	@Override
	@Transactional
	public void updateAgreement(Agreement agreement) {
		agreementMapper.updateAgreement(agreement);
		for(AgreementMeta agreementMeta : agreement.getAgreementMetas()){	
			agreementMapper.updateAgreementMeta(agreementMeta);
		}
	}

	@Override
	public Agreement getAgreementByAgreementId(String agreementId) {
		return agreementMapper.getAgreementByAgreementId(agreementId);
	}

	@Override
	public AgreementMeta getAgreementMetaByAgreementId(String agreementId) {
		return agreementMapper.getAgreementMetaByAgreementId(agreementId);
	}

	@Override
	@Transactional
	public void deleteAgreement(String agreementId) {
		agreementMapper.deleteAgreementMeta(agreementId);
		agreementMapper.deleteAgreement(agreementId);
	}

	@Override
	public Collection<Agreement> getAllAgreements() {
		// TODO Auto-generated method stub
		return agreementMapper.getAllAgreements();
	}

	@Override
	public long agreementSequence() {
		return 	agreementMapper.agreementSequence();
	}

	@Override
	public Collection<AgreementDto> getAllAgreementsWithMeta() {
		List<AgreementDto> agreementDtos = new ArrayList<AgreementDto>();  
		for(AgreementDto dto : agreementMapper.getAllAgreementsWithMeta()){
			String [] slaPeriod = dto.getSla().split("-");
			dto.setSla(slaPeriod[0]);
			dto.setPeriod(slaPeriod[1]);
			agreementDtos.add(dto);
		}
		return agreementDtos;
	}

	@Override
	public void deleteProviderMeta(String providerId) {
		providerMapper.deleteProviderMeta(providerId);
	}

	@Override
	public Collection<AgreementMeta> getAllAgreementMetas() {
		return agreementMapper.getAllAgreementMetas();
	}
	@Transactional
	@Override
	public void updateProviderPassword(Provider provider) {
		 providerMapper.updateProviderPassword(provider);		
	}

	@Override
	public void updateProviderStatus(Provider provider) {
		providerMapper.updateProviderStatus(provider);		
	}

	@Override
	public Provider getProviderLoginCount(String  providerId) {
		return providerMapper.getProviderLoginCount(providerId);
	}

	@Override
	public String getProviderSla(Map<String, String> map) {
		return agreementMapper.getProviderSla(map);
	}
}