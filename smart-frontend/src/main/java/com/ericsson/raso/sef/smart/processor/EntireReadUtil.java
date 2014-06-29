package com.ericsson.raso.sef.smart.processor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.raso.sef.core.Constants;
import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.db.model.ContractState;
import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.commons.read.Customer;
import com.ericsson.raso.sef.smart.commons.read.CustomerBucketRead;
import com.ericsson.raso.sef.smart.commons.read.CustomerRead;
import com.ericsson.raso.sef.smart.commons.read.CustomerVersionRead;
import com.ericsson.raso.sef.smart.commons.read.Rop;
import com.ericsson.raso.sef.smart.commons.read.RopBucketRead;
import com.ericsson.raso.sef.smart.commons.read.RopRead;
import com.ericsson.raso.sef.smart.commons.read.RopVersionRead;
import com.ericsson.raso.sef.smart.commons.read.Rpp;
import com.ericsson.raso.sef.smart.commons.read.RppBucketRead;
import com.ericsson.raso.sef.smart.commons.read.RppRead;
import com.ericsson.raso.sef.smart.commons.read.RppVersionRead;
import com.ericsson.raso.sef.smart.commons.read.Tag;
import com.ericsson.raso.sef.smart.commons.read.WelcomePack;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackBucketRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackVersionRead;
import com.ericsson.sef.bes.api.entities.Subscriber;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.BooleanParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ByteParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.DateTimeParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.EnumerationValueParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntElement;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.IntParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ListParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.LongParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.ShortParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StringParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.StructParameter;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.SymbolicParameter;

public class EntireReadUtil {

	private static Logger logger = LoggerFactory
			.getLogger(EntireReadUtil.class);

	public static Object symbolicOrDateParameter(String name, String value) {
		if (value.equals(SmartConstants.MAX_DATETIME)) {
			SymbolicParameter symbolicParameter = new SymbolicParameter();
			symbolicParameter.setName(name);
			symbolicParameter.setValue(value);
			return symbolicParameter;
		} else {
			DateTimeParameter dateTimeParameter = new DateTimeParameter();
			dateTimeParameter.setName(name);
			  if ( value != null && !"null".equals(value))
			dateTimeParameter.setValue(DateUtil.convertDateToUTCtime(value));
			return dateTimeParameter;
		}
	}

	public static IntParameter intParameter(String name, int value) {
		IntParameter intParameter = new IntParameter();
		intParameter.setName(name);
		intParameter.setValue(value);
		return intParameter;
	}

	public static StringParameter stringParameter(String name, String value) {
		StringParameter parameter = new StringParameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	public static EnumerationValueParameter enumerationValueParameter(
			String name, String value) {
		EnumerationValueParameter parameter = new EnumerationValueParameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	public static ShortParameter shortParameter(String name, int value) {
		ShortParameter parameter = new ShortParameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	public static ByteParameter byteParameter(String name, byte value) {
		ByteParameter parameter = new ByteParameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	public static LongParameter longParameter(String name, long value) {
		LongParameter parameter = new LongParameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	public static BooleanParameter booleanParameter(String name, boolean value) {
		BooleanParameter parameter = new BooleanParameter();
		parameter.setName(name);
		parameter.setValue(value);
		return parameter;
	}

	public static ListParameter listParameter(String name) {
		ListParameter parameter = new ListParameter();
		parameter.setName(name);
		return parameter;
	}

	public static Operation createCustomerRead(CustomerRead customerRead) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("Customer");

		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId", customerRead.getCustomerId()));

		if (customerRead.getBillCycleId() != null) {
			parameterList.add(EntireReadUtil.shortParameter("billCycleId", customerRead.getBillCycleId()));
		}

		if (customerRead.getBillCycleSwitch() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("billCycleSwitch", customerRead.getBillCycleSwitch()));
		}

		if (customerRead.getBillCycleIdAfterSwitch() != null) {
			parameterList.add(EntireReadUtil.shortParameter("billCycleIdAfterSwitch", customerRead.getBillCycleIdAfterSwitch()));
		}

		if (customerRead.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter", customerRead.getPrefetchFilter()));
		}

		if (customerRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", customerRead.getCategory()));
		}

		return operation;
	}

	public static Operation createCustomerBucketRead(
			CustomerBucketRead customerBucketRead) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("Customer");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				customerBucketRead.getCustomerId()));

		if (customerBucketRead.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"bCategory", customerBucketRead.getbCategory()));
		}

		if (customerBucketRead.getbInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"bInvalidFrom", customerBucketRead.getbInvalidFrom()));
		}

		if (customerBucketRead.getbValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"bValidFrom", customerBucketRead.getbValidFrom()));
		}

		if (customerBucketRead.getbSeriesId() != null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId",
					customerBucketRead.getbSeriesId()));
		}

		return operation;
	}

	public static Operation createCustomerVersionRead(
			CustomerVersionRead customerVersionRead) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("Customer");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				customerVersionRead.getCustomerId()));

		if (customerVersionRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"category", customerVersionRead.getCategory()));
		}

		if (customerVersionRead.getvValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"vValidFrom", customerVersionRead.getvValidFrom()));
		}

		if (customerVersionRead.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"vInvalidFrom", customerVersionRead.getvInvalidFrom()));
		}
		return operation;
	}

	public static Operation createRopRead(RopRead ropRead) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("ROP");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				ropRead.getCustomerId()));

		if (ropRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key",
					ropRead.getKey()));
		}

		if (ropRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"category", ropRead.getCategory()));
		}

		if (ropRead.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter",
					ropRead.getPrefetchFilter()));
		}

		if (ropRead.getAnnoFirstWarningPeriodSent() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"AnnoFirstWarningPeriodSent",
					ropRead.getAnnoFirstWarningPeriodSent()));
		}

		if (ropRead.getAnnoSecondWarningPeriodSent() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"AnnoSecondWarningPeriodSent",
					ropRead.getAnnoSecondWarningPeriodSent()));
		}

		if (ropRead.getIsBalanceClearanceOnOutpayment() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"IsBalanceClearanceOnOutpayment",
					ropRead.getIsBalanceClearanceOnOutpayment()));
		}

		if (ropRead.getIsCollectCallAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"IsCollectCallAllowed", ropRead.getIsCollectCallAllowed()));
		}

		if (ropRead.getIsFirstCallPassed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"IsFirstCallPassed", ropRead.getIsFirstCallPassed()));
		}

		if (ropRead.getIsGPRSUsed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsGPRSUsed",
					ropRead.getIsGPRSUsed()));
		}

		if (ropRead.getIsLastRechargeInfoStored() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"IsLastRechargeInfoStored",
					ropRead.getIsLastRechargeInfoStored()));
		}

		if (ropRead.getIsLastTransactionEnqUsed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"IsLastTransactionEnqUsed",
					ropRead.getIsLastTransactionEnqUsed()));
		}

		if (ropRead.getIsLocked() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsLocked",
					ropRead.getIsLocked()));
		}

		if (ropRead.getIsOperatorCollectCallAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"IsOperatorCollectCallAllowed",
					ropRead.getIsOperatorCollectCallAllowed()));
		}

		if (ropRead.getIsSmsAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsSmsAllowed",
					ropRead.getIsSmsAllowed()));
		}

		if (ropRead.getIsUSCAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsUSCAllowed",
					ropRead.getIsUSCAllowed()));
		}

		if (ropRead.getIsUSCAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsUSCAllowed",
					ropRead.getIsUSCAllowed()));
		}

		if (ropRead.getActiveEndDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"ActiveEndDate", ropRead.getActiveEndDate()));
		}

		ListParameter counterListParameter = EntireReadUtil
				.listParameter("ChargedMenuAccessCounter");
		parameterList.add(counterListParameter);
		for (Integer counter : ropRead.getChargedMenuAccessCounter()) {
			IntElement intParameter = new IntElement();
			intParameter.setValue(counter);
			counterListParameter.getElementOrBooleanElementOrByteElement().add(
					intParameter);
		}

		if (ropRead.getcTaggingStatus() != null) {
			parameterList.add(EntireReadUtil.intParameter("c_TaggingStatus",
					ropRead.getcTaggingStatus()));
		}

		if (ropRead.getFirstCallDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"FirstCallDate", ropRead.getFirstCallDate()));
		}

		if (ropRead.getGraceEndDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("GraceEndDate", ropRead.getGraceEndDate()));
		}

		if (ropRead.getS_CRMTitle() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle",
					ropRead.getS_CRMTitle()));
		}

		if (ropRead.getLastKnownPeriod() != null) {
			parameterList.add(EntireReadUtil.stringParameter("LastKnownPeriod",
								toSmartEnumerated(ropRead.getLastKnownPeriod())));
		}

		if (ropRead.getPreActiveEndDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"PreActiveEndDate", ropRead.getPreActiveEndDate()));
		}

		return operation;
	}

	private static String toSmartEnumerated(String lastKnownPeriod) {
		switch(ContractState.apiValue(lastKnownPeriod)) {
			case ACTIVE:
				return "Active";
			case BARRED:
				return "-";
			case DUNNING:
				return null;
			case GRACE:
				return "Grace";
			case NONE:
				return "-";
			case PREACTIVE:
				return "PreActive";
			case READY_TO_DELETE:
			case RECYCLED:
				return "Recycle";
			default:
				return "-";
		}
	}

	public static Operation createRopBucketRead(RopBucketRead ropBucketRead) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("ROP");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				ropBucketRead.getCustomerId()));

		if (ropBucketRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key",
					ropBucketRead.getKey()));
		}

		if (ropBucketRead.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"bCategory", ropBucketRead.getbCategory()));
		}

		if (ropBucketRead.getbSeriesId() != null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId",
					ropBucketRead.getbSeriesId()));
		}

		if (ropBucketRead.getbInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"bInvalidFrom", ropBucketRead.getbInvalidFrom()));
		}

		if (ropBucketRead.getbValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"bValidFrom", ropBucketRead.getbValidFrom()));
		}

		if (ropBucketRead.getOnPeakFuBalance() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("OnPeakAccountID_FU");
			parameter.getParameterOrBooleanParameterOrByteParameter().add(
					EntireReadUtil.longParameter("Balance",
							ropBucketRead.getOnPeakFuBalance()));
			parameterList.add(parameter);
		}

		return operation;
	}

	public static Operation createRopVersionRead(RopVersionRead ropVersionRead) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("ROP");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				ropVersionRead.getCustomerId()));

		if (ropVersionRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key",
					ropVersionRead.getKey()));
		}

		if (ropVersionRead.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"vInvalidFrom", ropVersionRead.getvInvalidFrom()));
		}

		if (ropVersionRead.getvValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"vValidFrom", ropVersionRead.getvValidFrom()));
		}

		if (ropVersionRead.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey",
					ropVersionRead.getOfferProfileKey()));
		}

		if (ropVersionRead.getOnPeakAccountExpiryDate() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("OnPeakAccountID");
			parameter.getParameterOrBooleanParameterOrByteParameter().add(
					EntireReadUtil.symbolicOrDateParameter("ExpiryDate",
							ropVersionRead.getOnPeakAccountExpiryDate()));
			parameterList.add(parameter);
		}

		if (ropVersionRead.getsOfferId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_OfferId",
					ropVersionRead.getsOfferId()));
		}
		return operation;
	}

	public static Operation createRppRead(RppRead rppRead) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("RPP_s_subLifeIncentive");

		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId", rppRead.getCustomerId()));
		if (rppRead.getsCanBeSharedByMultipleRops() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_CanBeSharedByMultipleRops", rppRead.getsCanBeSharedByMultipleRops()));
		}

		if (rppRead.getsInsertedViaBatch() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_InsertedViaBatch", rppRead.getsInsertedViaBatch()));
		}

		if (rppRead.getsPreActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_PreActive", rppRead.getsPreActive()));
		}

		if (rppRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", rppRead.getCategory()));
		}

		if (rppRead.getcIACCreditLimitValidity() != null) {
			parameterList.add(EntireReadUtil.longParameter("c_IACCreditLimitValidity", rppRead.getcIACCreditLimitValidity()));
		}

		if (rppRead.getcUnliResetRechargeValidity() != null) {
			parameterList.add(EntireReadUtil.longParameter("c_UnliResetRechargeValidity", rppRead.getcUnliResetRechargeValidity()));
		}

		if (rppRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", rppRead.getKey()));
		}

		if (rppRead.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", rppRead.getOfferProfileKey()));
		}

		if (rppRead.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter", rppRead.getPrefetchFilter()));
		}

		if (rppRead.getsActivationEndTime() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ActivationEndTime", rppRead.getsActivationEndTime()));
		}

		if (rppRead.getsActivationStartTime() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ActivationStartTime", rppRead.getsActivationStartTime()));
		}

		if (rppRead.getsCRMTitle() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", rppRead.getsCRMTitle()));
		}

		if (rppRead.getsPackageId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_PackageId", rppRead.getsPackageId()));
		}

		if (rppRead.getC_TokenBasedExpiredDate() != null) {
			parameterList.add(EntireReadUtil.longParameter( "c_TokenBasedExpiredDate", rppRead.getC_TokenBasedExpiredDate()));
		}

		if (rppRead.getsPeriodStartPoint() != null) {
			parameterList.add(EntireReadUtil.intParameter("s_PeriodStartPoint", rppRead.getsPeriodStartPoint()));
		}

		return operation;
	}

	public static Operation createRppBucketRead(RppBucketRead rppBucketRead) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("RPP_s_subLifeIncentive");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();

		parameterList.add(EntireReadUtil.stringParameter("CustomerId", rppBucketRead.getCustomerId()));

		if (rppBucketRead.getsActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Active", rppBucketRead.getsActive()));
		}

		if (rppBucketRead.getsValid() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Valid", rppBucketRead.getsValid()));
		}

		if (rppBucketRead.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", rppBucketRead.getbCategory()));
		}

		if (rppBucketRead.getbInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", rppBucketRead.getbInvalidFrom()));
		}

		if (rppBucketRead.getbValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", rppBucketRead.getbValidFrom()));
		}

		if (rppBucketRead.getbSeriesId() != null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId", rppBucketRead.getbSeriesId()));
		}

		if (rppBucketRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", rppBucketRead.getKey()));
		}

		if (rppBucketRead.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", rppBucketRead.getOfferProfileKey()));
		}

		if (rppBucketRead.getsError() != null) {
			parameterList.add(EntireReadUtil.byteParameter("s_Error", rppBucketRead.getsError()));
		}

		if (rppBucketRead.getsExpireDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ExpireDate", rppBucketRead.getsExpireDate()));
		}

		if (rppBucketRead.getsInfo() != null) {
			parameterList.add(EntireReadUtil.intParameter("s_Info", rppBucketRead.getsInfo()));
		}

		if (rppBucketRead.getsNextPeriodAct() != null) {
			parameterList.add(EntireReadUtil.longParameter("s_NextPeriodAct", rppBucketRead.getsNextPeriodAct().getTime()));
		}

		if (rppBucketRead.getsPeriodicBonusBalance() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("s_PeriodicBonus_FU");
			parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.longParameter("Balance",
																						rppBucketRead.getsPeriodicBonusBalance()));
			parameterList.add(parameter);
		}

		if (rppBucketRead.getsRenewalPeriodEnd() != null) {
			parameterList.add(EntireReadUtil.longParameter("s_RenewalPeriodEnd", rppBucketRead.getsNextPeriodAct().getTime()));
		}

		return operation;
	}

	public static Operation createRppVersionRead(RppVersionRead rppVersionRead) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("RPP_s_subLifeIncentive");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", rppVersionRead.getCustomerId()));

		if (rppVersionRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("Category", rppVersionRead.getCategory()));
		}

		if (rppVersionRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", rppVersionRead.getKey()));
		}

		if (rppVersionRead.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", rppVersionRead.getOfferProfileKey()));
		}

		if (rppVersionRead.getsPeriodicBonusExpiryDate() != null
				|| rppVersionRead.getsPeriodicBonusCreditLimit() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("s_PeriodicBonus");
			parameterList.add(parameter);

			if (rppVersionRead.getsPeriodicBonusExpiryDate() != null) {
				parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.symbolicOrDateParameter("ExpiryDate",
																						rppVersionRead.getsPeriodicBonusExpiryDate()));
			}

			if (rppVersionRead.getsPeriodicBonusCreditLimit() != null) {
				parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.longParameter("CreditLimit",
																					rppVersionRead.getsPeriodicBonusCreditLimit()));
			}
		}

		if (rppVersionRead.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", rppVersionRead.getvInvalidFrom()));
		}

		if (rppVersionRead.getvValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", rppVersionRead.getvValidFrom()));
		}

		return operation;
	}

	public static Operation createWelcomePackRead(WelcomePackRead read) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("RPP_s_subLifeIncentive");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				read.getCustomerId()));

		if (read.getsCanBeSharedByMultipleRops() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"s_CanBeSharedByMultipleRops",
					read.getsCanBeSharedByMultipleRops()));
		}

		if (read.getsInsertedViaBatch() != null) {
			parameterList.add(EntireReadUtil.booleanParameter(
					"s_InsertedViaBatch", read.getsInsertedViaBatch()));
		}

		if (read.getsPreActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_PreActive",
					read.getsPreActive()));
		}

		if (read.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"category", read.getCategory()));
		}

		if (read.getKey() != null) {
			parameterList
					.add(EntireReadUtil.intParameter("Key", read.getKey()));
		}

		if (read.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey",
					read.getOfferProfileKey()));
		}

		if (read.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter",
					read.getPrefetchFilter()));
		}

		if (read.getsActivationEndTime() != null) {
			parameterList.add(EntireReadUtil.longParameter(
					"s_ActivationEndTime", read.getsActivationEndTime()));
		}

		if (read.getsActivationStartTime() != null) {
			parameterList.add(EntireReadUtil.longParameter(
					"s_ActivationStartTime", read.getsActivationStartTime()));
		}

		if (read.getsCrmTitle() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle",
					read.getsCrmTitle()));
		}

		if (read.getsPackageId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_PackageId",
					read.getsPackageId()));
		}

		if (read.getsPeriodStartPoint() != null) {
			parameterList.add(EntireReadUtil.intParameter("s_PeriodStartPoint",
					read.getsPeriodStartPoint()));
		}

		if (read.getsPreActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_PreActive",
					read.getsPreActive()));
		}

		return operation;
	}

	public static Operation createWelcomePackBucketRead(
			WelcomePackBucketRead read) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("RPP_s_subLifeIncentive");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				read.getCustomerId()));

		if (read.getKey() != null) {
			parameterList
					.add(EntireReadUtil.intParameter("Key", read.getKey()));
		}

		if (read.getsPackageId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_PackageId",
					read.getsPackageId()));
		}

		if (read.getsActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Active",
					read.getsActive()));
		}

		if (read.getsValid() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Valid",
					read.getsValid()));
		}

		if (read.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"bCategory", read.getbCategory()));
		}

		if (read.getbInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"bInvalidFrom", read.getbInvalidFrom()));
		}

		if (read.getbSeriesId() != null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId",
					read.getbSeriesId()));
		}

		if (read.getbValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"bValidFrom", read.getbValidFrom()));
		}

		if (read.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey",
					read.getOfferProfileKey()));
		}

		if (read.getsError() != null) {
			parameterList.add(EntireReadUtil.byteParameter("s_Error",
					read.getsError()));
		}

		if (read.getsExpiryDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"s_ExpiryDate", read.getsExpiryDate()));
		}

		if (read.getsInfo() != null) {
			parameterList.add(EntireReadUtil.intParameter("s_Info",
					read.getsInfo()));
		}

		if (read.getsNextPeriodAct() != null) {
			parameterList.add(EntireReadUtil.longParameter("s_ExpireDate",
					read.getsNextPeriodAct()));
		}

		return operation;
	}

	public static Operation createWelcomePackVersionRead(
			WelcomePackVersionRead read) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("RPP_s_subLifeIncentive");

		List<Object> parameterList = operation.getParameterList()
				.getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId",
				read.getCustomerId()));

		if (read.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter(
					"Category", read.getCategory()));
		}

		if (read.getKey() != null) {
			parameterList
					.add(EntireReadUtil.intParameter("Key", read.getKey()));
		}

		if (read.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey",
					read.getOfferProfileKey()));
		}

		if (read.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"vInvalidFrom", read.getvInvalidFrom()));
		}

		if (read.getvValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter(
					"vValidFrom", read.getvValidFrom()));
		}

		return operation;
	}

	public static Customer getCustomer(Subscriber subcriber, Date currentTime) {
		Customer customer = new Customer();
		customer.setCustomerRead(createCustomerRead(subcriber));
		customer.setCustomerBucketRead(createCustomerBucketRead(subcriber, currentTime));
		customer.setCustomerVersionRead(createCustomerVersionRead(subcriber, currentTime));
		return customer;
	}

	private static CustomerRead createCustomerRead(Subscriber subcriber) {
		CustomerRead customerRead = new CustomerRead();
		customerRead.setCustomerId(subcriber.getMsisdn());
		customerRead.setBillCycleId(0);
		customerRead.setBillCycleIdAfterSwitch(-1);
		customerRead.setBillCycleSwitch(SmartConstants.MAX_DATETIME);
		customerRead.setCategory("ONLINE");
		customerRead.setPrefetchFilter(-1);
		return customerRead;
	}

	private static CustomerVersionRead createCustomerVersionRead(Subscriber subcriber, Date currentTime) {
		CustomerVersionRead versionRead = new CustomerVersionRead();
		versionRead.setCustomerId(subcriber.getMsisdn());
		versionRead.setCategory("ONLINE");
		versionRead.setvInvalidFrom(SmartConstants.MAX_DATETIME);
		// IConfig config = SefCoreServiceResolver.getConfigService();
		versionRead.setvValidFrom(DateUtil.convertDateToString(currentTime));
		return versionRead;
	}

	private static CustomerBucketRead createCustomerBucketRead(Subscriber subscriber, Date currentTime) {
		CustomerBucketRead bucketRead = new CustomerBucketRead();
		bucketRead.setCustomerId(subscriber.getMsisdn());
		bucketRead.setbCategory("ONLINE");
		bucketRead.setbSeriesId(0);
		IConfig config = SefCoreServiceResolver.getConfigService();
		bucketRead.setbValidFrom(DateUtil.convertDateToString(currentTime));
		logger.debug("bucketRead. bValidFrom : " + bucketRead.getbValidFrom());
		bucketRead.setbInvalidFrom(SmartConstants.MAX_DATETIME);
		return bucketRead;
	}

	public static Rop getRop(Subscriber subscriber, Date currentDateTime) {
		Rop rop = new Rop();
		rop.setRopRead(createRopRead(subscriber));
		rop.setRopBucketRead(createRopBucketRead(subscriber));
		rop.setRopVersionRead(createRopVersionRead(subscriber, currentDateTime));
		return rop;
	}

	private static RopRead createRopRead(Subscriber subscriber) {
		RopRead ropRead = new RopRead();
		ropRead.setCustomerId(subscriber.getMsisdn());
		ropRead.setKey(1);
		ropRead.setCategory("ONLINE");
		ropRead.setPrefetchFilter(-1);
		
		if (subscriber.getMetas() != null) {
			if (subscriber.getMetas().containsKey(Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE)) {

				logger.debug("Active End Date is " + subscriber.getMetas().get(Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE));

				String serviceFeeExpiry = subscriber.getMetas().get(Constants.READ_SUBSCRIBER_SERVICE_FEE_EXPIRY_DATE);
				if (serviceFeeExpiry != null) {
					ropRead.setActiveEndDate(DateUtil.convertDateToString(new Date(Long.parseLong(serviceFeeExpiry))));
				}
				
				logger.debug("Active End Date after formating is " + ropRead.getActiveEndDate());
			}
		}

		ropRead.setAnnoFirstWarningPeriodSent(false);
		ropRead.setAnnoSecondWarningPeriodSent(false);

		for (int i = 1; i <= 6; i++) {
			ropRead.getChargedMenuAccessCounter().add(0);
		}

		Tag tag = getSmartTagging(subscriber);
		if (tag != null) {
			ropRead.setcTaggingStatus(tag.getSmartId());
		}

		ropRead.setCustomerId(subscriber.getMsisdn());

		IConfig config = SefCoreServiceResolver.getConfigService();
		if (subscriber.getActiveDate() != null)
			ropRead.setFirstCallDate(DateUtil.convertDateToString(new Date(subscriber.getActiveDate())));


		String graceEndDate = getGraceEndDate(subscriber);
		logger.debug(" graceEndDate Before formatting is " + graceEndDate);
		if (graceEndDate != null) {
			
			try {
				SimpleDateFormat storeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date date = storeFormat.parse(graceEndDate);
				SimpleDateFormat nsnFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				ropRead.setGraceEndDate(nsnFormat.format(date));
				logger.debug(" graceEndDate After formatting is " + ropRead.getGraceEndDate());
				
			} catch (ParseException e) {
				logger.error("Date ParseException ", e);
			}
		} else {
			logger.error("graceEndDate was not found in IL DB/CS-AIR!!!");
		}
		
		
		ropRead.setIsBalanceClearanceOnOutpayment(true);

		String isCFMOC = subscriber.getMetas().get("IsCFMOC");

		if (isCFMOC != null) {
			int val = Boolean.parseBoolean(isCFMOC) ? 1 : 0;
			ropRead.setIsCFMOC(val);
		} 

		String IsCollectCallAllowed = subscriber.getMetas().get("IsCollectCallAllowed");
		if (IsCollectCallAllowed != null) {
			ropRead.setIsCollectCallAllowed(Boolean.valueOf(IsCollectCallAllowed));
		}

		String IsFirstCallPassed = subscriber.getMetas().get("IsFirstCallPassed");
		if (IsFirstCallPassed != null) {
			ropRead.setIsFirstCallPassed(Boolean.valueOf(IsFirstCallPassed));
		}

		String IsGPRSUsed = subscriber.getMetas().get("IsGPRSUsed");
		if (IsGPRSUsed != null) {
			ropRead.setIsGPRSUsed(Boolean.valueOf(IsGPRSUsed));
		}

		String IsLastRechargeInfoStored = subscriber.getMetas().get("IsLastRechargeInfoStored");
		if (IsLastRechargeInfoStored != null) {
			ropRead.setIsLastRechargeInfoStored(Boolean.valueOf(IsLastRechargeInfoStored));
		}

		String IsLastTransactionEnqUsed = subscriber.getMetas().get("IsLastTransactionEnqUsed");
		if (IsLastTransactionEnqUsed != null) {
			ropRead.setIsLastTransactionEnqUsed(Boolean.valueOf(IsLastTransactionEnqUsed));
		}

		String IsOperatorCollectCallAllowed = subscriber.getMetas().get("IsOperatorCollectCallAllowed");
		if (IsOperatorCollectCallAllowed != null) {
			ropRead.setIsOperatorCollectCallAllowed(Boolean.valueOf(IsOperatorCollectCallAllowed));
		}

		String IsSmsAllowed = subscriber.getMetas().get("IsSmsAllowed");
		if (IsSmsAllowed != null) {
			ropRead.setIsSmsAllowed(Boolean.valueOf(IsSmsAllowed));
		}

		String preActiveEndDate = subscriber.getMetas().get("PreActiveEndDate");
		logger.debug(" preActiveEndDate Before formatting is " + preActiveEndDate);
		if (preActiveEndDate != null) {
			
			try {
				SimpleDateFormat storeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
				Date date = storeFormat.parse(preActiveEndDate);
				SimpleDateFormat nsnFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
				ropRead.setPreActiveEndDate(nsnFormat.format(date));
				logger.debug(" preActiveEndDate After formatting is " + ropRead.getPreActiveEndDate());
				
			} catch (ParseException e) {
				logger.error("Date ParseException ", e);
			}
		} else {
			logger.error("PreActiveEndDate was not found in IL DB!!!");
		}

		ropRead.setLastKnownPeriod(subscriber.getContractState());
		ropRead.setS_CRMTitle("-");
		if (tag != null && tag.isSmartTag()) {
			ropRead.setIsLocked(true);
		} else {
			ropRead.setIsLocked(false);
		}

		return ropRead;
	}

	private static RopBucketRead createRopBucketRead(Subscriber subscriber) {
		RopBucketRead read = new RopBucketRead();
		read.setCustomerId(subscriber.getMsisdn());
		read.setbCategory("ONLINE");
		read.setbInvalidFrom(SmartConstants.MAX_DATETIME);
		/*
		 * if (accountDetailsResponse != null &&
		 * accountDetailsResponse.getActivationDate() != null) {config
		 * read.setbValidFrom(accountDetailsResponse.getActivationDate()
		 * .toString()); }
		 */
		IConfig config = SefCoreServiceResolver.getConfigService();
		logger.debug("read.subscriber.bValidFro : "
				+ subscriber.getActiveDate());
		if (subscriber.getActiveDate() != null)
			read.setbValidFrom(DateUtil.convertDateToString(new Date(subscriber
					.getActiveDate())));
		logger.debug("read.bValidFrom. bValidFrom : " + read.getbValidFrom());
		/*
		 * DedicatedAccountInformation da =
		 * daMap.get(SmConstants.AIRTIME_DA_ID); if (da != null) {
		 * read.setOnPeakFuBalance(Long.valueOf(da
		 * .getDedicatedAccountActiveValue1())); }
		 */
		String peakFullBalance = getDedicatedAccount(subscriber, "1");
		if (peakFullBalance != null)
			read.setOnPeakFuBalance(Long.parseLong(peakFullBalance));
		read.setbSeriesId(0);
		return read;
	}

	private static RopVersionRead createRopVersionRead(Subscriber subscriber,
			Date currentTime) {
		RopVersionRead read = new RopVersionRead();
		read.setCustomerId(subscriber.getMsisdn());
		read.setCategory("ONLINE");
		read.setKey(1);
		/*
		 * OfferInformation offer = offerMap.get(SmConstants.AIRTIME_OFFER_ID);
		 * if (offer != null) {
		 * read.setOnPeakAccountExpiryDate(offer.getExpiryDateTime()
		 * .toString()); }
		 */
		String offerExpiryDateTime = getOfferExpiryDateTime(subscriber);
		logger.debug("createRopVersionRead getOfferExpiryDateTime "
				+ offerExpiryDateTime);
		if (offerExpiryDateTime != null)
			read.setOnPeakAccountExpiryDate(DateUtil
					.convertDateToString(new Date(Long
							.parseLong(offerExpiryDateTime))));
		logger.debug("createRopVersionRead getOnPeakAccountExpiryDate "
				+ read.getOnPeakAccountExpiryDate());

		read.setsOfferId("TnT");
		IConfig config = SefCoreServiceResolver.getConfigService();
		logger.debug("createRopVersionRead.subscriber.getActiveDate() : "
				+ subscriber.getActiveDate());
		if (subscriber.getActiveDate() != null)
			read.setvValidFrom(DateUtil.convertDateToString(new Date(subscriber
					.getActiveDate())));

		logger.debug("createRopVersionRead.setvValidFrom. bValidFrom : "
				+ read.getvValidFrom());
		read.setvInvalidFrom(SmartConstants.MAX_DATETIME);

		return read;
	}

	
	private static Tag getSmartTagging(Subscriber subscriber) {

		Map<String, String> metaMap = subscriber.getMetas();
		String tagging = metaMap.get("Tagging");
		if (tagging == null)
			tagging = metaMap.get("c_TaggingStatus");
		logger.debug("SUBSCRIBER TAGGING IN SMART FORMAT: "  + tagging);
		return Tag.getTagBySmartId(Integer.parseInt(tagging));
		
	}



	private static String getGraceEndDate(Subscriber subscriber) {

		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;

		String graceEndDate = metaMap.get("GraceEndDate");

		if (graceEndDate == null) {
			logger.debug("Manila Inside getGraceEndDate");
			for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
				key = i.next();
				String value = null;

				logger.debug("Checking for grace offer... Key is " + key);

				if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO)) {
					value = metaMap.get(key);

					logger.debug("Manila getGraceEndDate Key Matched is " + key
							+ " Value is " + value);
					StringTokenizer str = new StringTokenizer(value, ",");
					String offerId = str.nextToken();
					String startDate = str.nextToken();
					String startDateTime = str.nextToken();
					String expiryDate = str.nextToken();
					String expiryDateTime = str.nextToken();

					if ("1".equals(offerId)) {
						graceEndDate = ((expiryDate.equals("null")?expiryDateTime:expiryDate));
						long dateMillis = Long.parseLong(graceEndDate);
						
						SimpleDateFormat metaStoreFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");
						return metaStoreFormat.format(new Date(dateMillis));
					}
				}
			}
		}

		return null;
	}

	private static String getDedicatedAccount(Subscriber subscriber,
			String dedicatedAccountId) {
		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;
			if (key.startsWith(Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID)) {

				value = metaMap.get(key);
				logger.debug("Manila getDedicatedAccount Key is " + key
						+ " Value is " + value);
				if (dedicatedAccountId.equals(value)) {
					String temp = "";
					if (key.length() > Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID
							.length()) {
						temp = key.substring(
								Constants.READ_BALANCES_DEDICATED_ACCOUNT_ID
										.length(), key.length());

						logger.debug(" Temp Value identified as  " + temp);
					}
					if (metaMap
							.containsKey(Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1
									+ temp)) {
						value = metaMap
								.get(Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1
										+ temp);
						return value;
					} else {
						logger.debug("Manila getDedicatedAccount Key not found "
								+ Constants.READ_BALANCES_DEDICATED_ACCOUNT_VALUE_1
								+ temp);
					}
				}
			}
		}
		return null;
	}

	private static String getOfferExpiryDateTime(Subscriber subscriber) {
		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;

		logger.debug("Manila Inside offerExpiryDate");
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;

			logger.debug("Manila getOfferExpiryDateTime Key is " + key);

			if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO)) {
				value = metaMap.get(key);

				logger.debug("Manila getOfferExpiryDateTime Key Matched is "
						+ key + " Value is " + value);
				StringTokenizer str = new StringTokenizer(value, ",");
				String offerId = str.nextToken();
				String startDate = str.nextToken();
				String startDateTime = str.nextToken();
				String expiryDate = str.nextToken();
				String expiryDateTime = str.nextToken();

				if ("1001".equals(offerId)) {
					if(expiryDateTime !=null)
					return expiryDateTime;
					else return expiryDate;
				}

			}
		}

		/*
		 * if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID)) {
		 * value = metaMap.get(key);
		 * 
		 * logger.debug("Manila getOfferExpiryDateTime Key Matched is " + key +
		 * " Value is " + value); if ("1001".equals(value)) { String temp = "";
		 * if (key.length() > Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID
		 * .length()) { temp = key.substring(
		 * Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID .length(),
		 * key.length()); logger.debug("Manila getOfferExpiryDateTime temp is "
		 * + temp); } if (metaMap
		 * .containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME +
		 * temp)) { value = metaMap
		 * .get(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME + temp);
		 * 
		 * return value; } else { logger.debug(
		 * "Manila READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME is not found"); }
		 * } }
		 */
		return null;
	}

	private static String getOfferExpiryDateTime(Subscriber subscriber,
			String offerId) {
		Map<String, String> metaMap = subscriber.getMetas();
		String value = null;
		String key = null;
		logger.debug(" Manila getOfferExpiryDateTime offerId is " + offerId);
		Set<String> keySet = metaMap.keySet();
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();

			if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO)) {
				value = metaMap.get(key);

				logger.debug("Manila getOfferExpiryDateTime Key Matched is "
						+ key + " Value is " + value);
				StringTokenizer str = new StringTokenizer(value, ",");
				String offerIdTemp = str.nextToken();
				String startDate = str.nextToken();
				String startDateTime = str.nextToken();
				String expiryDate = str.nextToken();
				String expiryDateTime = str.nextToken();

				if (offerId.equals(offerIdTemp)) {
					if(expiryDateTime !=null)
					return expiryDateTime;
					else
						return expiryDate;
				}

			}
		}

		/*
		 * if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID)) {
		 * value = metaMap.get(key);
		 * 
		 * logger.debug("Manila getOfferExpiryDateTime Key Matched is " + key +
		 * " Value is " + value);
		 * 
		 * String temp = ""; if (key.length() >
		 * Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID .length()) { temp =
		 * key.substring(
		 * Constants.READ_SUBSCRIBER_OFFER_INFO_OFFER_ID.length(),
		 * key.length()); } if (metaMap
		 * .containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME +
		 * temp)) { value = metaMap
		 * .get(Constants.READ_SUBSCRIBER_OFFER_INFO_EXPIRY_DATE_TIME + temp);
		 * return value; } }
		 */
		return null;
	}

	private static String getOfferStartDateTime(Subscriber subscriber,
			String offerId) {

		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;

		logger.debug("Manila Inside getOfferStartDateTime");
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;

			logger.debug("Manila getOfferStartDateTime Key is " + key);

			if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO)) {
				value = metaMap.get(key);

				logger.debug("Manila getOfferStartDateTime Key Matched is "
						+ key + " Value is " + value);
				StringTokenizer str = new StringTokenizer(value, ",");
				String offerIdTemp = str.nextToken();
				String startDate = str.nextToken();
				String startDateTime = str.nextToken();
				String expiryDate = str.nextToken();
				String expiryDateTime = str.nextToken();

				if (offerId.equals(offerIdTemp)) {
					if(startDate !=null)
					return startDate;
						else
					return startDateTime;
				}

			}
		}

		/*
		 * Map<String, String> metaMap = subscriber.getMetas(); String value =
		 * null; if
		 * (key.startsWith(Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID)) {
		 * value = metaMap.get(key);
		 * logger.debug("Manila getOfferStartDateTime Key is " + key +
		 * " Value is " + value); String temp = ""; if (key.length() >
		 * Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID .length()) { temp =
		 * key.substring(
		 * Constants.READ_SUBSCRIBER_SERVICE_OFFERING_ID.length(),
		 * key.length()); } if (metaMap
		 * .containsKey(Constants.READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME +
		 * temp)) { value = metaMap
		 * .get(Constants.READ_SUBSCRIBER_OFFER_INFO_START_DATE_TIME + temp);
		 * return value; } }
		 */
		return null;
	}

	public static Collection<Rpp> getRpp(Subscriber subscriber, Date currentTime) {
		List<Rpp> rpps = new ArrayList<Rpp>();
		// Collection<Integer> offerIds = offerMap.keySet();
		int index = 1;
		Map<String, String> metaMap = subscriber.getMetas();
		Set<String> keySet = metaMap.keySet();
		String key = null;
		for (Iterator<String> i = keySet.iterator(); i.hasNext();) {
			key = i.next();
			String value = null;
			logger.debug("getRpp key value is " + key);

			if (key.startsWith(Constants.READ_SUBSCRIBER_OFFER_INFO)) {
				value = metaMap.get(key);

				logger.debug("Manila getOfferStartDateTime Key Matched is " + key + " Value is " + value);
				StringTokenizer str = new StringTokenizer(value, ",");
				String offerIdTemp = str.nextToken();

				logger.debug("selected offerId: " + offerIdTemp);
				if (SefCoreServiceResolver.getConfigService().getValue("GLOBAL_walletMapping", offerIdTemp) != null) {
					value = metaMap.get(key);
					Rpp rpp = createRpp(subscriber, key, index++, currentTime, offerIdTemp);
					if (rpp != null) {
						rpps.add(rpp);
					}
				}
			}
		}
		return rpps;
	}

	private static Rpp createRpp(Subscriber subscriber, String key, int index,
			Date currentTime, String offerId) {
		// String wallet =
		// offerWalletMapping.getProperty(String.valueOf(offerInformation.getOfferID()));

		// String offerId = subscriber.getMetas().get(key);
		logger.debug("offerId  is " + offerId);
		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());
		logger.debug("walletName  is " + walletName);
		if (walletName != null) {
			Rpp rpp = new Rpp();

			RppRead rppRead = createRppRead(subscriber, key, offerId);
			rppRead.setKey(index);
			rpp.setRppRead(rppRead);

			RppBucketRead bucketRead = createRppBucketRead(subscriber, key,
					offerId);
			bucketRead.setKey(index);
			rpp.setRppBucketRead(bucketRead);

			RppVersionRead versionRead = createRppVersionRead(subscriber, key,
					offerId, currentTime);
			versionRead.setKey(index);
			rpp.setRppVersionRead(versionRead);

			return rpp;
		}
		return null;
	}

	private static RppRead createRppRead(Subscriber subscriber, String key,
			String offerId) {
		// String wallet =
		// offerWalletMapping.getProperty(String.valueOf(offerId));
		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());

		RppRead read = new RppRead();
		read.setCustomerId(subscriber.getMsisdn());
		read.setCategory("ONLINE");
		read.setcUnliResetRechargeValidity(0);
		read.setOfferProfileKey(1);
		read.setPrefetchFilter(-1);
		/*
		 * if (offerInformation.getExpiryDateTime() != null) {
		 * read.setsActivationEndTime(offerInformation.getExpiryDateTime()
		 * .toString()); }
		 */

		IConfig config = SefCoreServiceResolver.getConfigService();

		String offerExpiryDateString = getOfferExpiryDateTime(subscriber, offerId);
		logger.debug("createRppRead offerExpiryDateString  is "+ offerExpiryDateString);
		

		/*
		 * if (offerInformation.getStartDateTime() == null ||
		 * offerInformation.getStartDateTime().before(new Date(0))) {
		 * read.setsActivationStartTime(new Date(0).toString()); } else {
		 * read.setsActivationStartTime(offerInformation.getStartDateTime()
		 * .toString()); }
		 */
		
		String offerStartTime = getOfferStartDateTime(subscriber, offerId);
		logger.debug("createRppRead offerStartTime  is "+ offerStartTime);
		if(offerStartTime !=null)
		read.setsActivationStartTime(offerStartTime);
		read.setsCanBeSharedByMultipleRops(false);
		read.setsCRMTitle("-");
		read.setsInsertedViaBatch(false);
		read.setsPackageId(walletName);
		Date offerExpiryDate = null;

		if (offerExpiryDateString != null) {
		
			offerExpiryDate = new Date(Long.parseLong(offerExpiryDateString));
			read.setsActivationEndTime(DateUtil.convertDateToString(offerExpiryDate));
			
			

			if (walletName.equalsIgnoreCase("1030AutoFbc")
					&& offerExpiryDate != null) {
				read.setC_TokenBasedExpiredDate(offerExpiryDate.getTime());
			}
		}
		read.setsPeriodStartPoint(-1);
		if (ContractState.PREACTIVE.name().equals(
				ContractState.apiValue(subscriber.getContractState()))) {
			read.setsPreActive(true);
		} else {
			read.setsPreActive(false);
		}

		Long balance = 0l;

		/***********
		 * TBD once the property isssue is fixed. Cannot port the
		 * usageTimingWindows property as it does not comply to our DTD
		 ********/
		// Need to uncomment the below once the schema is corrected for the
		// property files.

		//WalletUsage usage = WalletUsageUtil.getWalletUsage(walletName);

		// if (usage != null) {
		// UsageCounterUsageThresholdInformation uc = ucUtMap.get(Integer
		// .valueOf(usage.getUc()));
		// if (uc != null
		// && usage.getUc() == uc.getUsageCounterID().intValue()) {
		// List<UsageThresholdInformation> tList = uc
		// .getUsageThresholdInformation();
		// for (UsageThresholdInformation ut : tList) {
		// if (ut.getUsageThresholdID().intValue() == usage.getUt()) {
		// if (uc.getUsageCounterValue() != null
		// && ut.getUsageThresholdValue() != null) {
		// balance = Long.valueOf(ut.getUsageThresholdValue())
		// - Long.valueOf(uc.getUsageCounterValue());
		// }
		// }
		// }
		// }
		// }

		read.setcIACCreditLimitValidity(balance);
		return read;
	}

	private static RppBucketRead createRppBucketRead(Subscriber subscriber,
			String key, String offerId) {
		// String wallet =
		// offerWalletMapping.getProperty(String.valueOf(offerId)));

		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());

		RppBucketRead read = new RppBucketRead();
		read.setCustomerId(subscriber.getMsisdn());
		read.setOfferProfileKey(1);

		/*
		 * if(offer.getStartDateTime() == null ||
		 * offer.getStartDateTime().before(new Date(0))) {
		 * read.setbValidFrom(new Date(0).toString()); } else {
		 * read.setbValidFrom(offer.getStartDateTime().toString()); }
		 */

		String offerStarteDateTime = getOfferStartDateTime(subscriber, offerId);
		logger.debug("createRppBucketRead.offerStarteDateTime : " + offerStarteDateTime);
		read.setbValidFrom(offerStarteDateTime);
		logger.debug("read.bValidFro : " + read.getbValidFrom());
		String offerExpiryDateString = getOfferExpiryDateTime(subscriber, offerId);
		IConfig config = SefCoreServiceResolver.getConfigService();
		Date offerExpiryDate = null;
		logger.debug("createRppBucketRead.offerExpiryDateString : " + offerExpiryDateString);
		if (offerExpiryDateString != null) {
			
			offerExpiryDate = new Date(Long.parseLong(offerExpiryDateString));
			
			logger.debug("createRppBucketRead.offerExpiryDate After formatting: "
					+ offerExpiryDate);
			read.setbInvalidFrom(DateUtil.convertDateToString(offerExpiryDate));
			read.setsNextPeriodAct(offerExpiryDate);
			read.setsExpireDate(DateUtil.convertDateToString(offerExpiryDate));
		}
		read.setbSeriesId(0);
		read.setbCategory("ONLINE");

		if (offerExpiryDate != null && offerExpiryDate.after(new Date())) {
			read.setsActive(true);
		} else {
			read.setsActive(false);
		}

		read.setsError((byte) 0);
		read.setsInfo(0);
		read.setsValid(true);

		// String daId =
		// offerDaMapping.getProperty(String.valueOf(offer.getOfferID()));
		String daId = SefCoreServiceResolver.getConfigService().getValue(
				"Global_offerMapping", offerId);

		if (daId != null && Integer.parseInt(offerId) <= 2000) {
			Long balance = 0l;

			String dedicatedAccountVal1 = getDedicatedAccount(subscriber, daId);
			// DedicatedAccountInformation da =
			// daMap.get(Integer.valueOf(daId));
			if (dedicatedAccountVal1 != null) {
				balance = Long.parseLong(dedicatedAccountVal1);
				long confec = getConversionFector(offerId);
				balance = balance / confec;
			}
			read.setsPeriodicBonusBalance(balance);
		} else {

			Long balance = 0l;

			// WalletUsage usage = WalletUsageUtil.getWalletUsage(wallet);
			// if (usage != null) {
			// UsageCounterUsageThresholdInformation uc = ucUtMap.get(Integer
			// .valueOf(usage.getUc()));
			// if (uc != null
			// && usage.getUc() == uc.getUsageCounterID().intValue()) {
			// List<UsageThresholdInformation> tList = uc
			// .getUsageThresholdInformation();
			// for (UsageThresholdInformation ut : tList) {
			// if (ut.getUsageThresholdID().intValue() == usage
			// .getUt()) {
			// if (uc.getUsageCounterValue() != null
			// && ut.getUsageThresholdValue() != null) {
			// balance = Long.valueOf(ut
			// .getUsageThresholdValue())
			// - Long.valueOf(uc
			// .getUsageCounterValue());
			// }
			// }
			// }
			// }
			// }

			read.setsPeriodicBonusBalance(balance);
		}

		return read;
	}

	private static long getConversionFector(String offerId) {

		long conFec = 1;
		// IConfig config = SmCoreContext.getConfig();
		// Properties walletMapping =
		// config.properties(IConfig.GLOBAL_COMPONENT, "walletMapping");

		String walletName = SefCoreServiceResolver.getConfigService().getValue(
				"GLOBAL_walletMapping", offerId.trim());
		// String walletName = name;
		String conversionFactor = SefCoreServiceResolver.getConfigService()
				.getValue("GLOBAL_walletConversionFactor", walletName);

		if (conversionFactor != null) {
			conFec = Long.parseLong(conversionFactor);
		}
		/*
		 * Properties offerConvMapping =
		 * config.properties(IConfig.GLOBAL_COMPONENT,
		 * SmConstants.OfferConversionFector); //String walletName =
		 * walletMapping.getProperty(offerId.trim()); if(walletName!=null){
		 * String conv = offerConvMapping.getProperty(walletName);
		 * if(conv!=null){ conFec= Long.parseLong(conv); } }
		 */
		return conFec;
	}

	private static RppVersionRead createRppVersionRead(Subscriber subscriber,
			String key, String offerId, Date currentTime) {
		RppVersionRead read = new RppVersionRead();
		read.setCategory("ONLINE");
		read.setCustomerId(subscriber.getMsisdn());
		read.setOfferProfileKey(1);
		read.setsPeriodicBonusCreditLimit(0L);

		// if (offer.getStartDateTime() == null ||
		// offer.getStartDateTime().before(new Date(0))) {
		// read.setvValidFrom(new Date(0).toString()); } else {
		// read.setvValidFrom(offer.getStartDateTime().toString()); }
		//
		String offerStartDateString = getOfferStartDateTime(subscriber, offerId);
		logger.debug("createRppVersionRead offerStartDateString is "+offerStartDateString);
		String offerExpiryDateString = getOfferExpiryDateTime(subscriber, offerId);
		logger.debug("createRppVersionRead offerExpiryDateString is "+offerExpiryDateString);
		Date offerExpiryDate=null;
		if (offerExpiryDateString != null) {
			
			offerExpiryDate = new Date(Long.parseLong(offerExpiryDateString));
			read.setsPeriodicBonusExpiryDate(DateUtil.convertDateToString(offerExpiryDate));
			read.setvInvalidFrom(DateUtil.convertDateToString(offerExpiryDate));

		} else {
			read.setvInvalidFrom(SmartConstants.MAX_DATETIME);
		}

		IConfig config = SefCoreServiceResolver.getConfigService();
		if (offerStartDateString != null) {
			read.setvValidFrom(offerStartDateString);
		} else {
			read.setvValidFrom(DateUtil.convertDateToString(currentTime));
		}
		return read;
	}

	public static WelcomePack getWelcomePack(Subscriber subscriber) {
		WelcomePack pack = new WelcomePack();
		pack.setRead(createRead(subscriber));
		pack.setBucketRead(createBucketRead(subscriber));
		pack.setVersionRead(createVersionRead(subscriber));
		return pack;
	}

	private static WelcomePackRead createRead(Subscriber subscriber) {
		WelcomePackRead read = new WelcomePackRead();
		read.setCategory("ONLINE");
		read.setCustomerId(subscriber.getCustomerId());
		read.setPrefetchFilter(-1);
		read.setsCrmTitle("-");
		read.setsCanBeSharedByMultipleRops(false);
		read.setsInsertedViaBatch(false);
		read.setOfferProfileKey(1);
		if (subscriber.getMetas() != null && ((subscriber.getMetas().containsKey("package") || subscriber.getMetas().containsKey("Package")))) {
			String welcomePack = subscriber.getMetas().get("package");
			if (welcomePack == null)
				welcomePack = subscriber.getMetas().get("Package");

			read.setsPackageId(welcomePack);
		}
		read.setsPreActive(ContractState.PREACTIVE.getName().equals(subscriber.getContractState()));
		if (subscriber.getActiveDate() != null)
			read.setsActivationStartTime(subscriber.getActiveDate());
		read.setsPeriodStartPoint(-1);
		return read;
	}

	private static WelcomePackVersionRead createVersionRead(Subscriber subscriber) {
		WelcomePackVersionRead read = new WelcomePackVersionRead();
		read.setCategory("ONLINE");
		read.setCustomerId(subscriber.getCustomerId());
		read.setOfferProfileKey(1);
		IConfig config = SefCoreServiceResolver.getConfigService();

		if (subscriber.getActiveDate() != null)
			read.setvValidFrom(DateUtil.convertDateToString(new Date(subscriber.getActiveDate())));
		read.setvInvalidFrom(SmartConstants.MAX_DATETIME);
		return read;
	}

	private static WelcomePackBucketRead createBucketRead(Subscriber subscriber) {
		WelcomePackBucketRead read = new WelcomePackBucketRead();
		read.setbCategory("ONLINE");
		read.setbSeriesId(0);
		read.setOfferProfileKey(1);
		read.setsActive(true);
		read.setsError((byte) 0);
		read.setsInfo(0);
		read.setsValid(true);
		read.setCustomerId(subscriber.getCustomerId());
		IConfig config = SefCoreServiceResolver.getConfigService();
		logger.debug("subscriber.getActiveDate() is ......." + subscriber.getActiveDate());
		if (subscriber.getActiveDate() != null)
			read.setbValidFrom(DateUtil.convertDateToString(new Date(subscriber.getActiveDate())));
		read.setbInvalidFrom(SmartConstants.MAX_DATETIME);

		return read;
	}

}