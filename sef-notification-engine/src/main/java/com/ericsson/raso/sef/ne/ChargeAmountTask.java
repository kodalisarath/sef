package com.ericsson.raso.sef.ne;

import java.math.BigDecimal;

import org.csapi.schema.parlayx.common.v2_1.ChargingInformation;
import org.csapi.wsdl.parlayx.payment.amount_charging.v2_1._interface.AmountCharging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.db.model.ChargeAmount;

public class ChargeAmountTask  {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private String msisdn;
	private ChargeAmount chargeAmount;
	
	public ChargeAmountTask(String msisdn, ChargeAmount chargeAmount) {
		this.msisdn = msisdn;
		this.chargeAmount = chargeAmount;
	}

	
	public Void execute() throws Exception {
		StringBuilder builder = new StringBuilder();
		builder.append(Constants .CHANNEL_NAME + ";" + "INVALID_CALL_ATTAMPT");
		
		AmountCharging amountCharging = null;//NotificationEngineContext.getBean(AmountCharging.class);
		
		ChargingInformation chargingInformation = new ChargingInformation();
		chargingInformation.setDescription(builder.toString());
		chargingInformation.setAmount(new BigDecimal(chargeAmount.getAmount()));
		chargingInformation.setCurrency(chargeAmount.getCurrencyCode().name());
		
		try {
			amountCharging.chargeAmount(msisdn, chargingInformation, String.valueOf(System.currentTimeMillis()));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new SmException(e);
		}
		return null;
	}

}
