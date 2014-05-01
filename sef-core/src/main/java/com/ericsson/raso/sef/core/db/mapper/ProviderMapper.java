package com.ericsson.raso.sef.core.db.mapper;

import java.util.Collection;
import java.util.List;

import com.ericsson.raso.sef.core.db.model.Provider;
import com.ericsson.raso.sef.core.db.model.ProviderMeta;
import com.ericsson.raso.sef.core.db.model.ProviderSLARequest;


public interface ProviderMapper {
	
	public List<ProviderSLARequest> getSLAAllowedRequestsForProvider();

	public String getSLATotalAllowedRequests();
	
	public void insertProvider(Provider provider);
	
	public void insertProviderMeta(ProviderMeta meta);
	
	public void updateProvider(Provider provider);
	
	public Provider getProviderByProviderId(String providerId);
	
	public void deleteProvider(String providerId);
	
	public void deleteProviderMeta(String providerId);
	
	public ProviderMeta getProviderMetaByProviderId(String providerId);
	
	public void updateProviderMeta(ProviderMeta meta);
	
	public Collection<Provider> getAllProvider();
	
	public void updateProviderPassword(Provider provider);
	 
	public void updateProviderStatus(Provider provider);
	 
	public Provider getProviderLoginCount(String providerId);
	
}
