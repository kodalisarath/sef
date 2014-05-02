package com.ericsson.raso.sef.core.db.service;

import java.util.Collection;
import java.util.Map;

import com.ericsson.raso.sef.core.db.model.Agreement;
import com.ericsson.raso.sef.core.db.model.AgreementDto;
import com.ericsson.raso.sef.core.db.model.AgreementMeta;
import com.ericsson.raso.sef.core.db.model.Provider;
import com.ericsson.raso.sef.core.db.model.ProviderMeta;

public interface UserManagementService {

	public void insertProvider(Provider provider);

	public void insertProviderMeta(ProviderMeta meta);

	public void updateProvider(Provider provider);

	public Provider getProviderByProviderId(String providerId);

	public void deleteProvider(String providerId);

	public void deleteProviderMeta(String providerId);
	
	public ProviderMeta getProviderMetaByProviderId(String providerId);

	public void updateProviderMeta(ProviderMeta meta);

	Collection<ProviderMeta> getMetas(String userId, String... metaKeys);

	public Collection<Provider> getAllProvider();

	public void insertAgreement(Agreement agreement);

	public void updateAgreement(Agreement agreement);

	public void deleteAgreement(String agreementId);
	
	public Agreement getAgreementByAgreementId(String agreementId);
	
	public AgreementMeta getAgreementMetaByAgreementId(String agreementId);

	public Collection<Agreement> getAllAgreements();
	
	public long agreementSequence();

	public Collection<AgreementDto> getAllAgreementsWithMeta();

	public Collection<AgreementMeta> getAllAgreementMetas();
	
	public void updateProviderPassword(Provider provider);
		 
	public void updateProviderStatus(Provider provider);
	 
	public Provider getProviderLoginCount(String providerId);
	
	public String getProviderSla(Map<String, String> map);
}
