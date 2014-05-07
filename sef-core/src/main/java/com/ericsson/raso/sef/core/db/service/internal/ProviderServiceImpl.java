package com.ericsson.raso.sef.core.db.service.internal;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.ericsson.raso.sef.core.db.mapper.AgreementMapper;
import com.ericsson.raso.sef.core.db.model.AgreementDto;
import com.ericsson.raso.sef.core.db.service.ProviderService;

public class ProviderServiceImpl implements ProviderService {

	AgreementMapper agreementMapper;
	
	public void setAgreementMapper(AgreementMapper agreementMapper) {
		this.agreementMapper = agreementMapper;
	}
	
	@Override
	public Map<String, Integer> getSLAAllowedRequestsForProvider() {
		Map<String, Integer> providerRequestsMap = new TreeMap<String, Integer>();
		Collection<AgreementDto> agreements = agreementMapper.getAllAgreementsWithMeta();
		for (AgreementDto agreementDto : agreements) {
			try {
				providerRequestsMap.put(agreementDto.getProviderId(), Integer.parseInt(agreementDto.getSla()));
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return providerRequestsMap;
	}
}
