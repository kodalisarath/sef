package com.ericsson.raso.sef.client.air.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RefillResponse extends AbstractAirResponse {

	private static final long serialVersionUID = 1L;

	private String masterAccountNumber;
	private Integer languageIDCurrent;
	private Integer promotionAnnouncementCode;
	private String voucherAgent;
	private String voucherSerialNumber;
	private String voucherGroup;
	private String transactionCurrency;
	private String transactionAmount;
	private String transactionAmountConverted;
	private String currency1;
	private String currency2;
	private RefillInfo[] refillInformation;
	private AccBefAndAfterRef accountBeforeRefill;
	private AccBefAndAfterRef accountAfterRefill;
	private Integer refillFraudCount;
	private Integer selectedOption;
	private String segmentationID;
	private Integer refillType;
	private Integer negotiatedCapabilities;
	private Integer availableServerCapabilities;
	private String accountPrepaidEmptyLimit1;
	private String accountPrepaidEmptyLimit2;
	private Tree[] treeDefinedField;

	public String getMasterAccountNumber() {
		if (masterAccountNumber == null) {
			masterAccountNumber = (String) (((Map<?, ?>) result).get("masterAccountNumber"));
		}
		return masterAccountNumber;
	}

	public Integer getLanguageIDCurrent() {
		if (languageIDCurrent == null) {
			languageIDCurrent = (Integer) (((Map<?, ?>) result).get("languageIDCurrent"));
		}
		return languageIDCurrent;
	}

	public Integer getPromotionAnnouncementCode() {
		if (promotionAnnouncementCode == null) {
			promotionAnnouncementCode = (Integer) (((Map<?, ?>) result).get("promotionAnnouncementCode"));
		}
		return promotionAnnouncementCode;
	}

	public String getVoucherAgent() {
		if (voucherAgent == null) {
			voucherAgent = (String) (((Map<?, ?>) result).get("voucherAgent"));
		}
		return voucherAgent;
	}

	public String getVoucherSerialNumber() {
		if (voucherSerialNumber == null) {
			voucherSerialNumber = (String) (((Map<?, ?>) result).get("voucherSerialNumber"));
		}
		return voucherSerialNumber;
	}

	public String getVoucherGroup() {
		if (voucherGroup == null) {
			voucherGroup = (String) (((Map<?, ?>) result).get("voucherGroup"));
		}
		return voucherGroup;
	}

	public String getTransactionCurrency() {
		if (transactionCurrency == null) {
			transactionCurrency = (String) (((Map<?, ?>) result).get("transactionCurrency"));
		}
		return transactionCurrency;
	}

	public String getTransactionAmount() {
		if (transactionAmount == null) {
			transactionAmount = (String) (((Map<?, ?>) result).get("transactionAmount"));
		}
		return transactionAmount;
	}

	public String getTransactionAmountConverted() {
		if (transactionAmountConverted == null) {
			transactionAmountConverted = (String) (((Map<?, ?>) result).get("transactionAmountConverted"));
		}
		return transactionAmountConverted;
	}

	public String getCurrency1() {
		if (currency1 == null) {
			currency1 = (String) (((Map<?, ?>) result).get("currency1"));
		}
		return currency1;
	}

	public String getCurrency2() {
		if (currency2 == null) {
			currency2 = (String) (((Map<?, ?>) result).get("currency2"));
		}
		return currency2;
	}

	public Integer getRefillFraudCount() {
		if (refillFraudCount == null) {
			refillFraudCount = (Integer) (((Map<?, ?>) result).get("refillFraudCount"));
		}
		return refillFraudCount;
	}

	public Integer getSelectedOption() {
		if (selectedOption == null) {
			selectedOption = (Integer) (((Map<?, ?>) result).get("selectedOption"));
		}
		return selectedOption;
	}

	public String getSegmentationID() {
		if (segmentationID == null) {
			segmentationID = (String) (((Map<?, ?>) result).get("segmentationID"));
		}
		return segmentationID;
	}

	public Integer getRefillType() {
		if (refillType == null) {
			refillType = (Integer) (((Map<?, ?>) result).get("refillType"));
		}
		return refillType;
	}

	public Integer getNegotiatedCapabilities() {
		if (negotiatedCapabilities == null) {
			negotiatedCapabilities = (Integer) (((Map<?, ?>) result).get("negotiatedCapabilities"));
		}
		return negotiatedCapabilities;
	}

	public Integer getAvailableServerCapabilities() {
		if (availableServerCapabilities == null) {
			availableServerCapabilities = (Integer) (((Map<?, ?>) result).get("availableServerCapabilities"));
		}
		return availableServerCapabilities;
	}

	public String getAccountPrepaidEmptyLimit1() {
		if (accountPrepaidEmptyLimit1 == null) {
			accountPrepaidEmptyLimit1 = (String) (((Map<?, ?>) result).get("accountPrepaidEmptyLimit1"));
		}
		return accountPrepaidEmptyLimit1;
	}

	public String getAccountPrepaidEmptyLimit2() {
		if (accountPrepaidEmptyLimit2 == null) {
			accountPrepaidEmptyLimit2 = (String) (((Map<?, ?>) result).get("accountPrepaidEmptyLimit2"));
		}
		return accountPrepaidEmptyLimit2;
	}

	@SuppressWarnings("unchecked")
	public RefillInfo[] getRefillInformation() {
		if (refillInformation == null) {
			Object[] offerInformationRes = (Object[]) (((Map<?, ?>) result).get("refillInformation"));
			List<RefillInfo> oiList = new ArrayList<RefillInfo>();
			for (Object oi : offerInformationRes) {
				oiList.add(new RefillInfo((Map<String, Object>) oi));
			}

			refillInformation = new RefillInfo[oiList.size()];
			refillInformation = oiList.toArray(refillInformation);
		}
		return refillInformation;
	}

	@SuppressWarnings("unchecked")
	public AccBefAndAfterRef getAccountBeforeRefill() {
		if (accountBeforeRefill == null) {
			Object accountBeforeRefillres = (Object) (((Map<?, ?>) result).get("accountBeforeRefill"));
			if (accountBeforeRefillres != null) {
				accountBeforeRefill = new AccBefAndAfterRef((Map<String, Object>) accountBeforeRefillres);
			}
		}
		return accountBeforeRefill;
	}

	@SuppressWarnings("unchecked")
	public AccBefAndAfterRef getAccountAfterRefill() {
		if (accountAfterRefill == null) {
			Object accountAfterRefillres = (Object) (((Map<?, ?>) result).get("accountAfterRefill"));
			if (accountAfterRefillres != null) {
				accountAfterRefill = new AccBefAndAfterRef((Map<String, Object>) accountAfterRefillres);
			}
		}
		return accountAfterRefill;
	}

	@SuppressWarnings("unchecked")
	public Tree[] getTreeDefinedField() {
		if (treeDefinedField == null) {
			Object[] treeRes = (Object[]) (((Map<?, ?>) result).get("treeDefinedField"));
			List<Tree> oiList = new ArrayList<Tree>();
			for (Object oi : treeRes) {
				oiList.add(new Tree((Map<String, Object>) oi));
			}

			treeDefinedField = new Tree[oiList.size()];
			treeDefinedField = oiList.toArray(treeDefinedField);
		}

		return treeDefinedField;
	}

}
