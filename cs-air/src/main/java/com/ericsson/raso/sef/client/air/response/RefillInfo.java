package com.ericsson.raso.sef.client.air.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class RefillInfo extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public RefillInfo(Map<String, Object> paramMap) {
		super(paramMap);

	}

	private List<RefillTotalAndPromotionInfo> refillValueTotal;
	private List<RefillTotalAndPromotionInfo> refillValuePromotion;
	private Integer serviceClassCurrent;
	private Date serviceClassTemporaryExpiryDate = new Date();
	private boolean promotionPlanProgressed;
	private Integer supervisionDaysSurplus;
	private Date serviceFeeDaysSurplus = new Date();
	private String promotionRefillAccumulatedValue1;
	private String promotionRefillAccumulatedValue2;
	private Integer promotionRefillCounter;
	private String progressionRefillValue1;
	private String progressionRefillValue2;
	private Integer progressionRefillCounter;

	public List<RefillTotalAndPromotionInfo> getRefillValueTotal() {

		return refillValueTotal;
	}

	public List<RefillTotalAndPromotionInfo> getRefillValuePromotion() {
		return refillValuePromotion;
	}

	public Integer getServiceClassCurrent() {
		if (serviceClassCurrent == null) {
			serviceClassCurrent = getParam("serviceClassCurrent", Integer.class);
		}
		return serviceClassCurrent;
	}

	public Date getServiceClassTemporaryExpiryDate() {
		if (serviceClassTemporaryExpiryDate == null) {
			serviceClassTemporaryExpiryDate = getParam("serviceClassTemporaryExpiryDate", Date.class);
		}
		return serviceClassTemporaryExpiryDate;
	}

	public boolean isPromotionPlanProgressed() {
		if (promotionPlanProgressed) {
			promotionPlanProgressed = getParam("promotionPlanProgressed", boolean.class);
		}
		return promotionPlanProgressed;
	}

	public Integer getSupervisionDaysSurplus() {
		if (supervisionDaysSurplus == null) {
			supervisionDaysSurplus = getParam("supervisionDaysSurplus", Integer.class);
		}
		return supervisionDaysSurplus;
	}

	public Date getServiceFeeDaysSurplus() {
		if (serviceFeeDaysSurplus == null) {
			serviceFeeDaysSurplus = getParam("serviceFeeDaysSurplus", Date.class);
		}
		return serviceFeeDaysSurplus;
	}

	public String getPromotionRefillAccumulatedValue1() {
		if (promotionRefillAccumulatedValue1 == null) {
			promotionRefillAccumulatedValue1 = getParam("promotionRefillAccumulatedValue1", String.class);
		}
		return promotionRefillAccumulatedValue1;
	}

	public String getPromotionRefillAccumulatedValue2() {
		if (promotionRefillAccumulatedValue2 == null) {
			promotionRefillAccumulatedValue2 = getParam("promotionRefillAccumulatedValue2", String.class);
		}
		return promotionRefillAccumulatedValue2;
	}

	public Integer getPromotionRefillCounter() {
		if (promotionRefillCounter == null) {
			promotionRefillCounter = getParam("promotionRefillCounter", Integer.class);
		}
		return promotionRefillCounter;
	}

	public String getProgressionRefillValue1() {
		if (progressionRefillValue1 == null) {
			progressionRefillValue1 = getParam("progressionRefillValue1", String.class);
		}
		return progressionRefillValue1;
	}

	public String getProgressionRefillValue2() {
		if (progressionRefillValue2 == null) {
			progressionRefillValue2 = getParam("progressionRefillValue2", String.class);
		}
		return progressionRefillValue2;
	}

	public Integer getProgressionRefillCounter() {
		if (progressionRefillCounter == null) {
			progressionRefillCounter = getParam("progressionRefillCounter", Integer.class);
		}
		return progressionRefillCounter;
	}

}
