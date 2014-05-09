package com.ericsson.raso.sef.bes.prodcat.service;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public interface IResourceGroupManager {

	public abstract Map<String, Byte> getResourceGroup(String resourceGroup) throws CatalogException;

	public abstract void setResourceGroup(String resourceGroup, Map<String, Byte> resources) throws CatalogException;

	public abstract void defineResourceGroupAndPriority(String groupName, String resourceName, byte priority) throws CatalogException;

	public abstract void unsetResourceGroupAndPriority(String groupName, String resourceName) throws CatalogException;

	public abstract Byte getResourcePriority(String groupName, String resourceName) throws CatalogException;

}