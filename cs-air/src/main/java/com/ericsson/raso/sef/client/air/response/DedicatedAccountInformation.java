package com.ericsson.raso.sef.client.air.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DedicatedAccountInformation extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public DedicatedAccountInformation(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Integer dedicatedAccountID;
	private String dedicatedAccountValue1;
	private String dedicatedAccountValue2;
	private Date expiryDate;
	private Date startDate;
	private Integer pamServiceID;
	private Integer offerID;
	private Integer productID;
	private boolean dedicatedAccountRealMoneyFlag;
	private Date closestExpiryDate;
	private String closestExpiryValue1;
	private String closestExpiryValue2;
	private Date closestAccessibleDate;
	private String closestAccessibleValue1;
	private String closestAccessibleValue2;
	private List<SubDedicatedInfo> subDedicatedAccountInformation;
	private String dedicatedAccountActiveValue1;
	private String dedicatedAccountActiveValue2;
	private Integer dedicatedAccountUnitType;
	private boolean compositeDedicatedAccountFlag;

	public Integer getDedicatedAccountID() {
		if (dedicatedAccountID == null) {
			dedicatedAccountID = getParam("dedicatedAccountID", Integer.class);
		}
		return dedicatedAccountID;
	}

	public String getDedicatedAccountValue1() {
		if (dedicatedAccountValue1 == null) {
			dedicatedAccountValue1 = getParam("dedicatedAccountValue1", String.class);
		}
		return dedicatedAccountValue1;
	}

	public String getDedicatedAccountValue2() {
		if (dedicatedAccountValue2 == null) {
			dedicatedAccountValue2 = getParam("dedicatedAccountValue2", String.class);
		}
		return dedicatedAccountValue2;
	}

	public Date getExpiryDate() {
		if (expiryDate == null) {
			expiryDate = getParam("expiryDate", Date.class);
		}
		return expiryDate;
	}

	public Date getStartDate() {
		if (startDate == null) {
			startDate = getParam("startDate", Date.class);
		}
		return startDate;
	}

	public Integer getPamServiceID() {
		if (pamServiceID == null) {
			pamServiceID = getParam("pamServiceID", Integer.class);
		}
		return pamServiceID;
	}

	public Integer getOfferID() {
		if (offerID == null) {
			offerID = getParam("offerID", Integer.class);
		}
		return offerID;
	}

	public Integer getProductID() {
		if (productID == null) {
			productID = getParam("productID", Integer.class);
		}
		return productID;
	}

	public boolean isDedicatedAccountRealMoneyFlag() {
		if (dedicatedAccountRealMoneyFlag) {
			dedicatedAccountRealMoneyFlag = getParam("dedicatedAccountRealMoneyFlag", boolean.class);
		}
		return dedicatedAccountRealMoneyFlag;
	}

	public Date getClosestExpiryDate() {
		if (closestExpiryDate == null) {
			closestExpiryDate = getParam("closestExpiryDate", Date.class);
		}
		return closestExpiryDate;
	}

	public String getClosestExpiryValue1() {
		if (closestExpiryValue1 == null) {
			closestExpiryValue1 = getParam("closestExpiryValue1", String.class);
		}
		return closestExpiryValue1;
	}

	public String getClosestExpiryValue2() {
		if (closestExpiryValue2 == null) {
			closestExpiryValue2 = getParam("closestExpiryValue2", String.class);
		}
		return closestExpiryValue2;
	}

	public Date getClosestAccessibleDate() {
		if (closestAccessibleDate == null) {
			closestAccessibleDate = getParam("closestAccessibleDate", Date.class);
		}
		return closestAccessibleDate;
	}

	public String getClosestAccessibleValue1() {
		if (closestAccessibleValue1 == null) {
			closestAccessibleValue1 = getParam("closestAccessibleValue1", String.class);
		}
		return closestAccessibleValue1;
	}

	public String getClosestAccessibleValue2() {
		if (closestAccessibleValue2 == null) {
			closestAccessibleValue2 = getParam("closestAccessibleValue2", String.class);
		}
		return closestAccessibleValue2;
	}

	public List<SubDedicatedInfo> getSubDedicatedAccountInformation() {
		return subDedicatedAccountInformation;
	}

	public String getDedicatedAccountActiveValue1() {
		if (dedicatedAccountActiveValue1 == null) {
			dedicatedAccountActiveValue1 = getParam("dedicatedAccountActiveValue1", String.class);
		}
		return dedicatedAccountActiveValue1;
	}

	public String getDedicatedAccountActiveValue2() {
		if (dedicatedAccountActiveValue2 == null) {
			dedicatedAccountActiveValue2 = getParam("dedicatedAccountActiveValue2", String.class);
		}
		return dedicatedAccountActiveValue2;
	}

	public Integer getDedicatedAccountUnitType() {
		if (dedicatedAccountUnitType == null) {
			dedicatedAccountUnitType = getParam("dedicatedAccountUnitType", Integer.class);
		}
		return dedicatedAccountUnitType;
	}

	public boolean isCompositeDedicatedAccountFlag() {
		if (compositeDedicatedAccountFlag) {
			compositeDedicatedAccountFlag = getParam("compositeDedicatedAccountFlag", boolean.class);
		}
		return compositeDedicatedAccountFlag;
	}

}
