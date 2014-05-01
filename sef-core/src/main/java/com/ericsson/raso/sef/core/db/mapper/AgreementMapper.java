package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;

import com.ericsson.raso.sef.core.db.model.Agreement;
import com.ericsson.raso.sef.core.db.model.AgreementDto;
import com.ericsson.raso.sef.core.db.model.AgreementMeta;

public interface AgreementMapper {
	
	public void insertAgreement(Agreement agreement);
	
	public void insertAgreementMeta(AgreementMeta agreementMeta);
	
	public void updateAgreement(Agreement agreement);
	
	public Agreement getAgreementByAgreementId(String agreementId);
	
	public AgreementMeta getAgreementMetaByAgreementId(String agreementId);
	
	public void deleteAgreement(String agreementId);
	
	public void deleteAgreementMeta(String agreementId);
	
	public void updateAgreementMeta(AgreementMeta agreementMeta);
	
	public Collection<Agreement> getAllAgreements();

	public long agreementSequence();

	public Collection<AgreementDto> getAllAgreementsWithMeta();

	public Collection<AgreementMeta> getAllAgreementMetas();
	
	public String getProviderSla(String providerId);
	
}
