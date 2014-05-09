package com.ericsson.raso.sef.bes.prodcat.service;

import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.CatalogException;

public interface IOfferGroupManager {

	public abstract Map<String, Byte> getOfferGroup(String offerGroup) throws CatalogException;

	public abstract void setOfferGroup(String offerGroup, Map<String, Byte> offers) throws CatalogException;

	public abstract void defineOfferGroupAndPriority(String groupName, String offerName, byte priority) throws CatalogException;

	public abstract void unsetOfferGroupAndPriority(String groupName, String offerName) throws CatalogException;

	public abstract Byte getOfferPriority(String groupName, String offerName) throws CatalogException;

}