package com.ericsson.raso.sef.fulfillment.profiles;

import java.util.Date;
import java.util.Map;

import com.ericsson.raso.sef.bes.prodcat.entities.BlockingFulfillment;
import com.ericsson.sef.bes.api.entities.Product;

public class OfferProfile extends BlockingFulfillment<Product> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer offerID;
	private Integer offerType;
	private Date startDateTime;
	private Date expiryDateTime;
	private Date expiryDate;
	private Integer expiryDateRelative;
	private String expiryDateTimeRelative;
	

	public Integer getOfferID() {
		return offerID;
	}

	public void setOfferID(Integer offerID) {
		this.offerID = offerID;
	}

	public Integer getOfferType() {
		return offerType;
	}

	public void setOfferType(Integer offerType) {
		this.offerType = offerType;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public Integer getExpiryDateRelative() {
		return expiryDateRelative;
	}

	public void setExpiryDateRelative(Integer expiryDateRelative) {
		this.expiryDateRelative = expiryDateRelative;
	}

	public String getExpiryDateTimeRelative() {
		return expiryDateTimeRelative;
	}

	public void setExpiryDateTimeRelative(String expiryDateTimeRelative) {
		this.expiryDateTimeRelative = expiryDateTimeRelative;
	}

	public OfferProfile(String name) {
		super(name);
	}


	@Override
	public void fulfill(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void prepare(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void query(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void revert(Product e, Map<String, String> map) {
		// TODO Auto-generated method stub
		
	}

	
}
