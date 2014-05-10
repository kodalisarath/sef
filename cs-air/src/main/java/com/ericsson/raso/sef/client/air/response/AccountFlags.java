package com.ericsson.raso.sef.client.air.response;

import java.util.Map;

public class AccountFlags extends NativeAirResponse {

	private static final long serialVersionUID = 1L;

	public AccountFlags(Map<String, Object> paramMap) {
		super(paramMap);
	}

	private Boolean activationStatusFlag;
	private Boolean negativeBarringStatusFlag;
	private Boolean supervisionPeriodWarningActiveFlag;
	private Boolean serviceFeePeriodWarningActiveFlag;
	private Boolean supervisionPeriodExpiryFlag;
	private Boolean serviceFeePeriodExpiryFlag;
	private Boolean twoStepActivationFlag;

	public Boolean isActivationStatusFlag() {
		if (activationStatusFlag == null) {
			activationStatusFlag = getParam("activationStatusFlag", Boolean.class);
		}
		return activationStatusFlag;
	}

	public Boolean isNegativeBarringStatusFlag() {
		if (negativeBarringStatusFlag == null) {
			negativeBarringStatusFlag = getParam("negativeBarringStatusFlag", Boolean.class);
		}
		return negativeBarringStatusFlag;
	}

	public Boolean isSupervisionPeriodWarningActiveFlag() {
		if (supervisionPeriodWarningActiveFlag == null) {
			supervisionPeriodWarningActiveFlag = getParam("supervisionPeriodWarningActiveFlag", Boolean.class);
		}
		return supervisionPeriodWarningActiveFlag;
	}

	public Boolean isServiceFeePeriodWarningActiveFlag() {
		if (serviceFeePeriodWarningActiveFlag == null) {
			serviceFeePeriodWarningActiveFlag = getParam("serviceFeePeriodWarningActiveFlag", Boolean.class);
		}
		return serviceFeePeriodWarningActiveFlag;
	}

	public Boolean isSupervisionPeriodExpiryFlag() {
		if (supervisionPeriodExpiryFlag == null) {
			supervisionPeriodExpiryFlag = getParam("supervisionPeriodExpiryFlag", Boolean.class);
		}
		return supervisionPeriodExpiryFlag;
	}

	public Boolean isServiceFeePeriodExpiryFlag() {
		if (serviceFeePeriodExpiryFlag == null) {
			serviceFeePeriodExpiryFlag = getParam("serviceFeePeriodExpiryFlag", Boolean.class);
		}
		return serviceFeePeriodExpiryFlag;
	}

	public Boolean isTwoStepActivationFlag() {
		if (twoStepActivationFlag == null) {
			twoStepActivationFlag = getParam("twoStepActivationFlag", Boolean.class);
		}
		return twoStepActivationFlag;
	}

}
