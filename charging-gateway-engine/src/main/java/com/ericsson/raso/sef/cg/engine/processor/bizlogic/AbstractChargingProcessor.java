package com.ericsson.raso.sef.cg.engine.processor.bizlogic;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.dccapi.avp.CCMoneyAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestNumberAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCRequestTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.CCServiceSpecificUnitsAvp;
import com.ericsson.pps.diameter.dccapi.avp.CostInformationAvp;
import com.ericsson.pps.diameter.dccapi.avp.CurrencyCodeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ExponentAvp;
import com.ericsson.pps.diameter.dccapi.avp.GrantedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.MultipleServicesCreditControlAvp;
import com.ericsson.pps.diameter.dccapi.avp.MultipleServicesIndicatorAvp;
import com.ericsson.pps.diameter.dccapi.avp.RequestedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceIdentifierAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterInfoAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterTypeAvp;
import com.ericsson.pps.diameter.dccapi.avp.ServiceParameterValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdAvp;
import com.ericsson.pps.diameter.dccapi.avp.UnitValueAvp;
import com.ericsson.pps.diameter.dccapi.avp.UsedServiceUnitAvp;
import com.ericsson.pps.diameter.dccapi.avp.ValueDigitsAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.pps.diameter.rfcapi.base.avp.EventTimestampAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ExperimentalResultAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ExperimentalResultCodeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.ResultCodeAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.SessionIdAvp;
import com.ericsson.pps.diameter.rfcapi.base.avp.VendorIdAvp;
import com.ericsson.pps.diameter.scapv2.avp.ResultCodeExtensionAvp;
import com.ericsson.pps.diameter.scapv2.avp.TimeZoneAvp;
import com.ericsson.pps.diameter.scapv2.avp.TrafficCaseAvp;
import com.ericsson.raso.sef.cg.engine.CgEngineContext;
import com.ericsson.raso.sef.cg.engine.ChargingRequest;
import com.ericsson.raso.sef.cg.engine.Operation;
import com.ericsson.raso.sef.cg.engine.Operation.Type;
import com.ericsson.raso.sef.cg.engine.ResponseCodeUtil;
import com.ericsson.raso.sef.core.PerformanceStatsLogger;
import com.ericsson.raso.sef.core.SmException;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.diameter.DiameterErrorCode;
import com.ericsson.raso.sef.core.cg.nsn.avp.AccessFrontendIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.CalculatedAmountAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ConsumerAccountIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ErrorIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ErrorInfoAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ErrorItemAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ErrorTextAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.FunctionalUnitIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.MerchantIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.PPIInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ProductIdAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.PurposeAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ServiceInfoAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.TransparentDataAvp;

public abstract class AbstractChargingProcessor implements Processor {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void process(Exchange exchange) throws Exception {
		ChargingRequest request = (ChargingRequest) exchange.getIn().getBody();

		Ccr sourceCcr = request.getSourceCcr();
		Ccr scapCcr = toScapCcr(sourceCcr, request);

		preProcess(request, scapCcr);

		long startTime = System.currentTimeMillis();
		Cca cca = scapCcr.send();
		PerformanceStatsLogger.log("CCN", System.currentTimeMillis()
				- startTime);

		ChargingInfo response = new ChargingInfo();
		response.setUniqueMessageId(request.getMessageId());
		response.setSessionId(request.getSessionId());

		List<Avp> resultAvps = toNsnAnswer(cca, response, request);

		response.setAvpList(resultAvps);

		postProcess(request, response, cca);

		exchange.getOut().setBody(response);
	}

	protected Ccr toScapCcr(Ccr sourceCcr, ChargingRequest request)
			throws AvpDataException, SmException {
		Ccr scapCcr = CgEngineContext.getChargingApi().createScapCcr(
				sourceCcr.getSessionId(), request.getHostId());

		String handle = "";
		scapCcr.addAvp(new CCRequestNumberAvp(getRequestNumber()));
		scapCcr.setCCRequestType(sourceCcr.getCCRequestType());
		scapCcr.addAvp(sourceCcr.getAvp(EventTimestampAvp.AVP_CODE));

		if (sourceCcr.getRequestedAction() != null)
			scapCcr.setRequestedAction(sourceCcr.getRequestedAction());

		if (sourceCcr.getRequestedAction() != null
				&& sourceCcr.getRequestedAction() != 0) {
			if (sourceCcr.getMultipleServicesIndicator() != null) {
				scapCcr.addAvp(new MultipleServicesIndicatorAvp(sourceCcr
						.getMultipleServicesIndicator()));
			}
		}

		Avp subscriptionIdAvp = sourceCcr.getAvp(SubscriptionIdAvp.AVP_CODE);
		if (subscriptionIdAvp != null) {
			scapCcr.addAvp(subscriptionIdAvp);
		}

		scapCcr.addAvp(new TrafficCaseAvp(21));
		scapCcr.addAvp(new TimeZoneAvp((byte) 11, (byte) 0, (byte) 0));

		MerchantIdAvp merchantIdAvp = null;
		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp(
				sourceCcr.getAvp(ServiceInfoAvp.AVP_CODE));
		if (serviceInfoAvp != null) {
			PPIInformationAvp ppi = serviceInfoAvp.getPpiInformationAvp();
			if (ppi != null) {
				AccessFrontendIdAvp accessFrontendIdAvp = ppi
						.getAccessFrontendIdAvp();
				if (accessFrontendIdAvp != null) {
					scapCcr.addAvp(accessFrontendIdAvp
							.convertToServiceParameterInfoAvp());
				}

				TransparentDataAvp transparentDataAvp = ppi
						.getTransparentData();
				if (transparentDataAvp != null) {
					scapCcr.addAvp(transparentDataAvp
							.convertToServiceParameterInfoAvp());
				}

				ConsumerAccountIdAvp consumerAccountIdAvp = ppi
						.getConsumerAccountIdAvp();
				if (consumerAccountIdAvp != null) {
					scapCcr.addAvp(consumerAccountIdAvp
							.convertToServiceParameterInfoAvp());
				}

				merchantIdAvp = ppi.getMerchantIdAvp();
				if (merchantIdAvp != null) {
					scapCcr.addAvp(merchantIdAvp
							.convertToServiceParameterInfoAvp());
				}

				ProductIdAvp productIdAvp = ppi.getProductdIdAvp();
				if (productIdAvp != null) {
					scapCcr.addAvp(productIdAvp
							.convertToServiceParameterInfoAvp());
					handle = productIdAvp.getValue();
					
					log.debug("ProductIdAvp Value:" + productIdAvp.getValue()
							+ " ProductIdAvp Code " + productIdAvp.getAvpCode()
							+ " ProductIdAvp name " + productIdAvp.getName()
							+ " ProductIdAvp  " + productIdAvp);
				}
				

				PurposeAvp purposeAvp = ppi.getPurposeAvp();
				if (purposeAvp != null) {
					scapCcr.addAvp(purposeAvp
							.convertToServiceParameterInfoAvp());
					if (purposeAvp.getAsUTF8String().contains("predefined")) {
						scapCcr.addAvp(new ServiceIdentifierAvp(7001));
					} else {
						if (request.getOperation().getType() == Type.NO_TRANSACTION) {
							scapCcr.addAvp(new ServiceIdentifierAvp(7002));
						} else {
							scapCcr.addAvp(new ServiceIdentifierAvp(7006));
						}
					}
				}
			}
		}

		Avp multipleServiceCreditControlAvp = sourceCcr
				.getAvp(MultipleServicesCreditControlAvp.AVP_CODE);
		CreditControl control = new CreditControl();
		if (multipleServiceCreditControlAvp != null) {
			control = createCreditControlAvp(multipleServiceCreditControlAvp,
					scapCcr, request);
			scapCcr.addAvp(control.getAvp().getSubAvp(
					RequestedServiceUnitAvp.AVP_CODE));
		}

		if (merchantIdAvp != null
				&& merchantIdAvp.getAsUTF8String().equalsIgnoreCase("pasaload")) {
			PasaloadValidationTask pasaloadValidationTask = new PasaloadValidationTask(
					request.getMsisdn(), control.getMoney(), handle);
			pasaloadValidationTask.execute();
		}

		return scapCcr;
	}

	protected List<Avp> toNsnAnswer(Cca cca, ChargingInfo response,
			ChargingRequest chargingRequest) throws AvpDataException {
		List<Avp> answerAvp = new ArrayList<Avp>();

		try {
			answerAvp.add(new CCRequestNumberAvp(getRequestNumber()));
			answerAvp.add(new CCRequestTypeAvp(cca.getCCRequestType()));

			Long resultCode = ResponseCodeUtil.getMappedResultCode(cca
					.getResultCode());
			ResultCodeAvp resultCodeAvp = new ResultCodeAvp(resultCode);
			response.setResultCodeAvp(resultCodeAvp);
			answerAvp.add(resultCodeAvp);

			answerAvp.add(new SessionIdAvp(cca.getSessionId()));

			int extCode = 0;
			if (cca.getAvp(ResultCodeExtensionAvp.AVP_CODE) != null) {
				ResultCodeExtensionAvp codeExtensionAvp = new ResultCodeExtensionAvp(
						cca.getAvp(ResultCodeExtensionAvp.AVP_CODE));

				if (codeExtensionAvp != null) {
					extCode = codeExtensionAvp.getAsInt();
				}
			}
			ExperimentalResultAvp experimentalResultAvp = new ExperimentalResultAvp();
			experimentalResultAvp.addSubAvp(new VendorIdAvp(28458));
			Long experimentalResultCode = ResponseCodeUtil
					.getMappedExperimentalResultCode(resultCode, extCode);
			if (experimentalResultCode != null) {
				experimentalResultAvp.addSubAvp(new ExperimentalResultCodeAvp(
						experimentalResultCode));
				answerAvp.add(experimentalResultAvp);
			}

			PPIInformationAvp ppi = new PPIInformationAvp();
			TransparentDataAvp transparentDataAvp = new TransparentDataAvp();
			ppi.addSubAvp(transparentDataAvp);

			ppi.addCurrency("Php");

			if (resultCode != DiameterErrorCode.DIAMETER_SUCCESS.getCode()) {
				ErrorItemAvp errorItemAvp = new ErrorItemAvp();
				errorItemAvp.addSubAvp(new FunctionalUnitIdAvp(28));
				errorItemAvp.addSubAvp(new ErrorIdAvp(resultCode));
				errorItemAvp.addSubAvp(new ErrorTextAvp(""));

				ErrorInfoAvp errorInfoAvp = new ErrorInfoAvp();
				errorInfoAvp.addSubAvp(errorItemAvp);
				ppi.addSubAvp(errorInfoAvp);
				transparentDataAvp.setValue(String.valueOf(resultCode));
			}

			if (cca.getAvp(CostInformationAvp.AVP_CODE) != null) {
				CostInformationAvp costInfoAvp = new CostInformationAvp(
						cca.getAvp(CostInformationAvp.AVP_CODE));

				if (costInfoAvp != null) {
					UnitValueAvp unitValAvp = new UnitValueAvp(
							costInfoAvp.getSubAvp(UnitValueAvp.AVP_CODE));
					ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(
							unitValAvp.getSubAvp(ValueDigitsAvp.AVP_CODE));

					ExponentAvp exponentAvp = new ExponentAvp(
							unitValAvp.getSubAvp(ExponentAvp.AVP_CODE));

					double val = valueDigitsAvp.getAsLong()
							* (Math.pow(10, exponentAvp.getAsInt()));
					long pesoInCents = (long) (val * 100);

					CalculatedAmountAvp calculatedAmountAvp = new CalculatedAmountAvp(
							pesoInCents);

					ppi.addSubAvp(calculatedAmountAvp);
				}
			} else {
				if (chargingRequest.getOperation() == Operation.DIRECT_DEBIT) {

					Avp avp = cca.getAvp(GrantedServiceUnitAvp.AVP_CODE);
					if (avp != null) {
						GrantedServiceUnitAvp grantedSerUnitAvp = new GrantedServiceUnitAvp(
								avp);
						if (grantedSerUnitAvp != null) {
							if (grantedSerUnitAvp
									.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
								CCMoneyAvp ccMoneyAvp = new CCMoneyAvp(
										grantedSerUnitAvp
												.getSubAvp(CCMoneyAvp.AVP_CODE));
								UnitValueAvp unitValAvp = new UnitValueAvp(
										ccMoneyAvp
												.getSubAvp(UnitValueAvp.AVP_CODE));
								ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(
										unitValAvp
												.getSubAvp(ValueDigitsAvp.AVP_CODE));
								ExponentAvp exponentAvp = new ExponentAvp(
										unitValAvp
												.getSubAvp(ExponentAvp.AVP_CODE));

								double val = valueDigitsAvp.getAsLong()
										* (Math.pow(10, exponentAvp.getAsInt()));
								long pesoInCents = (long) (val * 100);
								CalculatedAmountAvp calculatedAmountAvp = new CalculatedAmountAvp(
										pesoInCents);
								ppi.addSubAvp(calculatedAmountAvp);
							} else {
								CCServiceSpecificUnitsAvp ccSerSpecUnitsAvp = new CCServiceSpecificUnitsAvp(
										grantedSerUnitAvp
												.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE));
								CalculatedAmountAvp calculatedAmountAvp = new CalculatedAmountAvp(
										ccSerSpecUnitsAvp.getAsLong() / 100);
								ppi.addSubAvp(calculatedAmountAvp);
							}
						}
					}
				}
				if (chargingRequest.getOperation() == Operation.COMMIT) {
					Avp avp = cca.getAvp(UsedServiceUnitAvp.AVP_CODE);
					if (avp != null) {
						UsedServiceUnitAvp usedServiceUnitAvp = new UsedServiceUnitAvp(
								avp);
						if (usedServiceUnitAvp.getSubAvp(CCMoneyAvp.AVP_CODE) != null) {
							CCMoneyAvp ccMoneyAvp = new CCMoneyAvp(
									usedServiceUnitAvp
											.getSubAvp(CCMoneyAvp.AVP_CODE));
							UnitValueAvp unitValAvp = new UnitValueAvp(
									ccMoneyAvp.getSubAvp(UnitValueAvp.AVP_CODE));
							ValueDigitsAvp valueDigitsAvp = new ValueDigitsAvp(
									unitValAvp
											.getSubAvp(ValueDigitsAvp.AVP_CODE));

							ExponentAvp exponentAvp = new ExponentAvp(
									unitValAvp.getSubAvp(ExponentAvp.AVP_CODE));

							double val = valueDigitsAvp.getAsLong()
									* (Math.pow(10, exponentAvp.getAsInt()));
							long pesoInCents = (long) (val * 100);

							CalculatedAmountAvp calculatedAmountAvp = new CalculatedAmountAvp(
									pesoInCents);
							ppi.addSubAvp(calculatedAmountAvp);
						} else {
							CCServiceSpecificUnitsAvp ccSerSpecUnitsAvp = new CCServiceSpecificUnitsAvp(
									usedServiceUnitAvp
											.getSubAvp(CCServiceSpecificUnitsAvp.AVP_CODE));
							CalculatedAmountAvp calculatedAmountAvp = new CalculatedAmountAvp(
									ccSerSpecUnitsAvp.getAsLong() / 100);
							ppi.addSubAvp(calculatedAmountAvp);
						}
					}
				}
			}

			ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp();
			serviceInfoAvp.addPPiInformationAvp(ppi);
			answerAvp.add(serviceInfoAvp);
		} catch (AvpDataException e) {
			log.error("error during iterating AVP", e);
			answerAvp.clear();
			answerAvp.add(new ResultCodeAvp(
					DiameterErrorCode.DIAMETER_UNABLE_TO_COMPLY.getCode()));
		}
		return answerAvp;
	}

	private CreditControl createCreditControlAvp(Avp cc, Ccr scapCcr,
			ChargingRequest request) throws AvpDataException {
		CreditControl control = new CreditControl();
		MultipleServicesCreditControlAvp ccAvp = new MultipleServicesCreditControlAvp();
		control.setAvp(ccAvp);
		if (cc != null) {
			List<Avp> avps = cc.getDataAsGroup();
			for (Avp rsu : avps) {
				if (rsu.getAvpCode() == RequestedServiceUnitAvp.AVP_CODE) {
					RequestedServiceUnitAvp su = new RequestedServiceUnitAvp(
							rsu);
					RequestedServiceUnitAvp requestedServiceUnitAvp = new RequestedServiceUnitAvp();

					CCServiceSpecificUnitsAvp avpCcServiceSpecificUnitsAvp = null;
					CCMoneyAvp moneyAvp = null;

					if (request.getOperation() == Operation.CANCEL)
						avpCcServiceSpecificUnitsAvp = new CCServiceSpecificUnitsAvp(
								0);
					else {
						Avp money = su.getSubAvp(CCMoneyAvp.AVP_CODE);
						if (money != null) {
							for (Avp avp : money.getDataAsGroup()) {
								if (avp.getAvpCode() == UnitValueAvp.AVP_CODE) {
									UnitValueAvp unit = new UnitValueAvp(avp);
									ValueDigitsAvp value = new ValueDigitsAvp(
											unit.getSubAvp(ValueDigitsAvp.AVP_CODE));
									control.setMoney(value.getAsLong());
									if (request.getOperation() == Operation.DIRECT_DEBIT
											&& scapCcr
													.getAvp(ServiceIdentifierAvp.AVP_CODE)
													.getAsInt() == 7002) {
										moneyAvp = new CCMoneyAvp();
										moneyAvp.addSubAvp(new CurrencyCodeAvp(
												608));
										UnitValueAvp unitValueAvp = new UnitValueAvp();
										unitValueAvp
												.addSubAvp(unit
														.getSubAvp(ValueDigitsAvp.AVP_CODE));
										unitValueAvp.addExponent(-2);
										moneyAvp.addSubAvp(unitValueAvp);
									} else
										avpCcServiceSpecificUnitsAvp = new CCServiceSpecificUnitsAvp(
												value.getAsLong());
								}
							}
						}
						if (avpCcServiceSpecificUnitsAvp != null) {
							if (scapCcr.getAvp(ServiceIdentifierAvp.AVP_CODE)
									.getAsInt() == 7001) {
								scapCcr.addAvp(convertToServiceParameterInfoAvp(
										1, avpCcServiceSpecificUnitsAvp));
							}
							requestedServiceUnitAvp
									.addSubAvp(avpCcServiceSpecificUnitsAvp);
						} else
							requestedServiceUnitAvp.addSubAvp(moneyAvp);
					}
					ccAvp.addSubAvp(requestedServiceUnitAvp);
					break;
				}
			}
		}
		return control;
	}

	private static class CreditControl {
		private MultipleServicesCreditControlAvp avp;
		private long money;

		public MultipleServicesCreditControlAvp getAvp() {
			return avp;
		}

		public void setAvp(MultipleServicesCreditControlAvp avp) {
			this.avp = avp;
		}

		public long getMoney() {
			return money;
		}

		public void setMoney(long money) {
			this.money = money;
		}

	}

	protected abstract Integer getRequestNumber();

	protected abstract void preProcess(ChargingRequest request, Ccr scapCcr)
			throws AvpDataException;

	protected abstract void postProcess(ChargingRequest request,
			ChargingInfo response, Cca cca) throws AvpDataException;

	public ServiceParameterInfoAvp convertToServiceParameterInfoAvp(int type,
			CCServiceSpecificUnitsAvp avp) throws AvpDataException {
		ServiceParameterInfoAvp info = new ServiceParameterInfoAvp();
		Avp avp2 = new Avp();
		avp2.setData((int) avp.getAsLong());
		info.addSubAvp(new ServiceParameterTypeAvp(type));
		info.addSubAvp(new ServiceParameterValueAvp(avp2.getData()));
		return info;
	}

}
