package com.ericsson.raso.sef.smart.processor;

import java.util.List;

import com.ericsson.raso.sef.smart.commons.SmartConstants;
import com.ericsson.raso.sef.smart.commons.read.CustomerBucketRead;
import com.ericsson.raso.sef.smart.commons.read.CustomerRead;
import com.ericsson.raso.sef.smart.commons.read.CustomerVersionRead;
import com.ericsson.raso.sef.smart.commons.read.RopBucketRead;
import com.ericsson.raso.sef.smart.commons.read.RopRead;
import com.ericsson.raso.sef.smart.commons.read.RopVersionRead;
import com.ericsson.raso.sef.smart.commons.read.RppBucketRead;
import com.ericsson.raso.sef.smart.commons.read.RppRead;
import com.ericsson.raso.sef.smart.commons.read.RppVersionRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackBucketRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackRead;
import com.ericsson.raso.sef.smart.commons.read.WelcomePackVersionRead;
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
	
	public static Object symbolicOrDateParameter(String name, String value) {
		if(value.equals(SmartConstants.MAX_DATETIME)) {
			SymbolicParameter symbolicParameter = new SymbolicParameter();
			symbolicParameter.setName(name);
			symbolicParameter.setValue(value);
			return symbolicParameter;
		} else {
			DateTimeParameter dateTimeParameter = new DateTimeParameter();
			dateTimeParameter.setName(name);
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
	
	public static EnumerationValueParameter enumerationValueParameter(String name, String value) {
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
		
		if(customerRead.getBillCycleId() != null) {
			parameterList.add(EntireReadUtil.shortParameter("billCycleId", customerRead.getBillCycleId()));
		}
		
		if(customerRead.getBillCycleSwitch() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("billCycleSwitch", customerRead.getBillCycleSwitch()));
		}
		
		if(customerRead.getBillCycleIdAfterSwitch() != null) {
			parameterList.add(EntireReadUtil.shortParameter("billCycleIdAfterSwitch", customerRead.getBillCycleIdAfterSwitch()));
		}
		
		if(customerRead.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter", customerRead.getPrefetchFilter()));
		}
		
		if(customerRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", customerRead.getCategory()));
		}
		
		return operation;
	}
	
	public static Operation createCustomerBucketRead(CustomerBucketRead customerBucketRead) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("Customer");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", customerBucketRead.getCustomerId()));
		
		if(customerBucketRead.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", customerBucketRead.getbCategory()));
		}
		
		if(customerBucketRead.getbInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", customerBucketRead.getbInvalidFrom()));
		}
		
		if(customerBucketRead.getbValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", customerBucketRead.getbValidFrom()));
		}
		
		if(customerBucketRead.getbSeriesId() != null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId", customerBucketRead.getbSeriesId()));
		}
		
		return operation;
	}

	public static Operation createCustomerVersionRead(CustomerVersionRead customerVersionRead) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("Customer");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", customerVersionRead.getCustomerId()));
		
		if(customerVersionRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", customerVersionRead.getCategory()));
		}
		
		if(customerVersionRead.getvValidFrom()!= null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", customerVersionRead.getvValidFrom()));
		}
		
		if(customerVersionRead.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", customerVersionRead.getvInvalidFrom()));
		}
		return operation;
	}
	
	public static Operation createRopRead(RopRead ropRead) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("ROP");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", ropRead.getCustomerId()));
		
		if(ropRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", ropRead.getKey()));
		}
		
		if(ropRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", ropRead.getCategory()));
		}
		
		if(ropRead.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter", ropRead.getPrefetchFilter()));
		}
		
		if(ropRead.getAnnoFirstWarningPeriodSent() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("AnnoFirstWarningPeriodSent", ropRead.getAnnoFirstWarningPeriodSent()));
		}
		
		if(ropRead.getAnnoSecondWarningPeriodSent() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("AnnoSecondWarningPeriodSent", ropRead.getAnnoSecondWarningPeriodSent()));
		}
		
		if(ropRead.getIsBalanceClearanceOnOutpayment() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsBalanceClearanceOnOutpayment", ropRead.getIsBalanceClearanceOnOutpayment()));
		}
		
		if(ropRead.getIsCollectCallAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsCollectCallAllowed", ropRead.getIsCollectCallAllowed()));
		}
		
		if(ropRead.getIsFirstCallPassed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsFirstCallPassed", ropRead.getIsFirstCallPassed()));
		}
		
		if(ropRead.getIsGPRSUsed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsGPRSUsed", ropRead.getIsGPRSUsed()));
		}
		
		if(ropRead.getIsLastRechargeInfoStored() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsLastRechargeInfoStored", ropRead.getIsLastRechargeInfoStored()));
		}
		
		if(ropRead.getIsLastTransactionEnqUsed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsLastTransactionEnqUsed", ropRead.getIsLastTransactionEnqUsed()));
		}
		
		if(ropRead.getIsLocked() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsLocked", ropRead.getIsLocked()));
		}
		
		if(ropRead.getIsOperatorCollectCallAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsOperatorCollectCallAllowed", ropRead.getIsOperatorCollectCallAllowed()));
		}
		
		if(ropRead.getIsSmsAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsSmsAllowed", ropRead.getIsSmsAllowed()));
		}
		
		if(ropRead.getIsUSCAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsUSCAllowed", ropRead.getIsUSCAllowed()));
		}
		
		if(ropRead.getIsUSCAllowed() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("IsUSCAllowed", ropRead.getIsUSCAllowed()));
		}
		
		if(ropRead.getActiveEndDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("ActiveEndDate", ropRead.getActiveEndDate()));
		}
		
		ListParameter counterListParameter = EntireReadUtil.listParameter("ChargedMenuAccessCounter");
		parameterList.add(counterListParameter);
		for(Integer counter : ropRead.getChargedMenuAccessCounter()) {
			IntElement intParameter = new IntElement();
			intParameter.setValue(counter);
			counterListParameter.getElementOrBooleanElementOrByteElement().add(intParameter);
		}
		
		if(ropRead.getcTaggingStatus() != null) {
			parameterList.add(EntireReadUtil.intParameter("c_TaggingStatus", ropRead.getcTaggingStatus()));
		}
		
		if(ropRead.getFirstCallDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("FirstCallDate", ropRead.getFirstCallDate()));
		}
		
		if(ropRead.getGraceEndDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("GraceEndDate", ropRead.getGraceEndDate()));
		}
		
		if(ropRead.getS_CRMTitle() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", ropRead.getS_CRMTitle()));
		}
		
		if(ropRead.getLastKnownPeriod() != null) {
			parameterList.add(EntireReadUtil.stringParameter("LastKnownPeriod", ropRead.getLastKnownPeriod()));
		}
		
		if(ropRead.getPreActiveEndDate() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("PreActiveEndDate", ropRead.getPreActiveEndDate()));
		}
		
		return operation;
	}
	
	public static Operation createRopBucketRead(RopBucketRead ropBucketRead) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("ROP");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", ropBucketRead.getCustomerId()));
		
		if(ropBucketRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", ropBucketRead.getKey()));
		}
		
		if(ropBucketRead.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", ropBucketRead.getbCategory()));
		}
		
		if(ropBucketRead.getbSeriesId() != null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId", ropBucketRead.getbSeriesId()));
		}
		
		if(ropBucketRead.getbInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", ropBucketRead.getbInvalidFrom()));
		}
		
		if(ropBucketRead.getbValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", ropBucketRead.getbValidFrom()));
		}
		
		if(ropBucketRead.getOnPeakFuBalance() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("OnPeakAccountID_FU");
			parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.longParameter("Balance", ropBucketRead.getOnPeakFuBalance()));
			parameterList.add(parameter);
		}
		
		return operation;
	}
	
	public static Operation createRopVersionRead(RopVersionRead ropVersionRead) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("ROP");

		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", ropVersionRead.getCustomerId()));
		
		if(ropVersionRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", ropVersionRead.getKey()));
		}
		
		if(ropVersionRead.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", ropVersionRead.getvInvalidFrom()));
		}
		
		if(ropVersionRead.getvValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", ropVersionRead.getvValidFrom()));
		}
		
		if(ropVersionRead.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", ropVersionRead.getOfferProfileKey()));
		}
		
		if(ropVersionRead.getOnPeakAccountExpiryDate() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("OnPeakAccountID");
			parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.symbolicOrDateParameter("ExpiryDate", ropVersionRead.getOnPeakAccountExpiryDate()));
			parameterList.add(parameter);
		}
		
		if(ropVersionRead.getsOfferId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_OfferId", ropVersionRead.getsOfferId()));
		}
		return operation;
	}
	
	
	public static Operation createRppRead(RppRead rppRead) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("RPP");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", rppRead.getCustomerId()));
		if(rppRead.getsCanBeSharedByMultipleRops() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_CanBeSharedByMultipleRops", rppRead.getsCanBeSharedByMultipleRops()));
		}
		
		if(rppRead.getsInsertedViaBatch() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_InsertedViaBatch", rppRead.getsInsertedViaBatch()));
		}
		
		if(rppRead.getsPreActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_PreActive", rppRead.getsPreActive()));
		}
		
		if(rppRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", rppRead.getCategory()));
		}
		
		if(rppRead.getcIACCreditLimitValidity() != null) {
			parameterList.add(EntireReadUtil.longParameter("c_IACCreditLimitValidity", rppRead.getcIACCreditLimitValidity()));
		}
		
		if(rppRead.getcUnliResetRechargeValidity() != null) {
			parameterList.add(EntireReadUtil.longParameter("c_UnliResetRechargeValidity", rppRead.getcUnliResetRechargeValidity()));
		}
		
		if(rppRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", rppRead.getKey()));
		}
		
		if(rppRead.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", rppRead.getOfferProfileKey()));
		}
		
		if(rppRead.getPrefetchFilter() != null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter", rppRead.getPrefetchFilter()));
		}
		
		if(rppRead.getsActivationEndTime() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ActivationEndTime", rppRead.getsActivationEndTime()));
		}
		
		if(rppRead.getsActivationStartTime() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ActivationStartTime", rppRead.getsActivationStartTime()));
		}
		
		if(rppRead.getsCRMTitle() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", rppRead.getsCRMTitle()));
		}
		
		if(rppRead.getsPackageId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_PackageId", rppRead.getsPackageId()));
		}
		
		if(rppRead.getC_TokenBasedExpiredDate() != null) {
			parameterList.add(EntireReadUtil.longParameter("c_TokenBasedExpiredDate", rppRead.getC_TokenBasedExpiredDate()));
		}
		
		if(rppRead.getsPeriodStartPoint() != null) {
			parameterList.add(EntireReadUtil.intParameter("s_PeriodStartPoint", rppRead.getsPeriodStartPoint()));
		}
		
		return operation;
	}
	
	public static Operation createRppBucketRead(RppBucketRead rppBucketRead) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("RPP");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", rppBucketRead.getCustomerId()));
		
		if(rppBucketRead.getsActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Active", rppBucketRead.getsActive()));
		}
		
		if(rppBucketRead.getsValid() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Valid", rppBucketRead.getsValid()));
		}
		
		if(rppBucketRead.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", rppBucketRead.getbCategory()));
		}
		
		if(rppBucketRead.getbInvalidFrom() !=  null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", rppBucketRead.getbInvalidFrom()));
		}
		
		if(rppBucketRead.getbValidFrom() !=  null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", rppBucketRead.getbValidFrom()));
		}
		
		if(rppBucketRead.getbSeriesId() !=  null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId", rppBucketRead.getbSeriesId()));
		}
		
		if(rppBucketRead.getKey() !=  null) {
			parameterList.add(EntireReadUtil.intParameter("Key", rppBucketRead.getKey()));
		}
		
		if(rppBucketRead.getOfferProfileKey()!=  null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", rppBucketRead.getOfferProfileKey()));
		}
		
		if(rppBucketRead.getsError()!=  null) {
			parameterList.add(EntireReadUtil.byteParameter("s_Error", rppBucketRead.getsError()));
		}
		
		if(rppBucketRead.getsExpireDate()!=  null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ExpireDate", rppBucketRead.getsExpireDate()));
		}
		
		if(rppBucketRead.getsInfo()!=  null) {
			parameterList.add(EntireReadUtil.intParameter("s_Info", rppBucketRead.getsInfo()));
		}
		
		if(rppBucketRead.getsNextPeriodAct() != null) {
			parameterList.add(EntireReadUtil.longParameter("s_NextPeriodAct", rppBucketRead.getsNextPeriodAct().getTime()));
		}
		
		if(rppBucketRead.getsPeriodicBonusBalance() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("s_PeriodicBonus_FU");
			parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.longParameter("Balance", rppBucketRead.getsPeriodicBonusBalance()));
			parameterList.add(parameter);
		}
		
		if(rppBucketRead.getsRenewalPeriodEnd() != null) {
			parameterList.add(EntireReadUtil.longParameter("s_RenewalPeriodEnd", rppBucketRead.getsNextPeriodAct().getTime()));
		}
		
		return operation;
	}
	
	public static Operation createRppVersionRead(RppVersionRead rppVersionRead) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("RPP");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", rppVersionRead.getCustomerId()));
		
		if(rppVersionRead.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("Category", rppVersionRead.getCategory()));
		}
		
		if(rppVersionRead.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", rppVersionRead.getKey()));
		}
		
		if(rppVersionRead.getOfferProfileKey()!= null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", rppVersionRead.getOfferProfileKey()));
		}
		
		if(rppVersionRead.getsPeriodicBonusExpiryDate() != null || rppVersionRead.getsPeriodicBonusCreditLimit() != null) {
			StructParameter parameter = new StructParameter();
			parameter.setName("s_PeriodicBonus");
			parameterList.add(parameter);
			
			if(rppVersionRead.getsPeriodicBonusExpiryDate() != null) {
				parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.symbolicOrDateParameter("ExpiryDate", rppVersionRead.getsPeriodicBonusExpiryDate()));
			}
			
			if(rppVersionRead.getsPeriodicBonusCreditLimit() != null) {
				parameter.getParameterOrBooleanParameterOrByteParameter().add(EntireReadUtil.longParameter("CreditLimit", rppVersionRead.getsPeriodicBonusCreditLimit()));
			}
		}
		
		if(rppVersionRead.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", rppVersionRead.getvInvalidFrom()));
		}
		
		if(rppVersionRead.getvValidFrom()!= null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", rppVersionRead.getvValidFrom()));
		}
		
		return operation;
	}
	
	public static Operation createWelcomePackRead(WelcomePackRead read) {
		Operation operation = new Operation();
		operation.setName("Read");
		operation.setModifier("RPP_s_subLifeIncentive");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", read.getCustomerId()));
		
		if(read.getsCanBeSharedByMultipleRops() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_CanBeSharedByMultipleRops", read.getsCanBeSharedByMultipleRops()));
		}
		
		if(read.getsInsertedViaBatch() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_InsertedViaBatch", read.getsInsertedViaBatch()));
		}
		
		if(read.getsPreActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_PreActive", read.getsPreActive()));
		}
		
		if(read.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("category", read.getCategory()));
		}
		
		if(read.getKey()!= null) {
			parameterList.add(EntireReadUtil.intParameter("Key", read.getKey()));
		}
		
		if(read.getOfferProfileKey()!= null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", read.getOfferProfileKey()));
		}
		
		if(read.getPrefetchFilter()!= null) {
			parameterList.add(EntireReadUtil.intParameter("prefetchFilter", read.getPrefetchFilter()));
		}
		
		if(read.getsActivationEndTime()!= null) {
			parameterList.add(EntireReadUtil.longParameter("s_ActivationEndTime", read.getsActivationEndTime()));
		}
		
		if(read.getsActivationStartTime()!= null) {
			parameterList.add(EntireReadUtil.longParameter("s_ActivationStartTime", read.getsActivationStartTime()));
		}
		
		if(read.getsCrmTitle()!= null) {
			parameterList.add(EntireReadUtil.stringParameter("s_CRMTitle", read.getsCrmTitle()));
		}
		
		if(read.getsPackageId()!= null) {
			parameterList.add(EntireReadUtil.stringParameter("s_PackageId", read.getsPackageId()));
		}
		
		if(read.getsPeriodStartPoint()!= null) {
			parameterList.add(EntireReadUtil.intParameter("s_PeriodStartPoint", read.getsPeriodStartPoint()));
		}

		if(read.getsPreActive()!= null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_PreActive", read.getsPreActive()));
		}
	
		return operation;
	}
	
	public static Operation createWelcomePackBucketRead(WelcomePackBucketRead read) {
		Operation operation = new Operation();
		operation.setName("BucketRead");
		operation.setModifier("RPP_s_subLifeIncentive");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", read.getCustomerId()));
		
		if(read.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", read.getKey()));
		}
		
		if(read.getsPackageId() != null) {
			parameterList.add(EntireReadUtil.stringParameter("s_PackageId", read.getsPackageId()));
		}
		
		if(read.getsActive() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Active", read.getsActive()));
		}
		
		if(read.getsValid() != null) {
			parameterList.add(EntireReadUtil.booleanParameter("s_Valid", read.getsValid()));
		}
		
		if(read.getbCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("bCategory", read.getbCategory()));
		}
		
		if(read.getbInvalidFrom()!= null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bInvalidFrom", read.getbInvalidFrom()));
		}
		
		if(read.getbSeriesId()!= null) {
			parameterList.add(EntireReadUtil.intParameter("bSeriesId", read.getbSeriesId()));
		}
		
		if(read.getbValidFrom()!= null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("bValidFrom", read.getbValidFrom()));
		}
		
		if(read.getOfferProfileKey()!= null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", read.getOfferProfileKey()));
		}
		
		if(read.getsError()!= null) {
			parameterList.add(EntireReadUtil.byteParameter("s_Error", read.getsError()));
		}
		
		if(read.getsExpiryDate()!= null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("s_ExpiryDate", read.getsExpiryDate()));
		}
		
		if(read.getsInfo()!= null) {
			parameterList.add(EntireReadUtil.intParameter("s_Info", read.getsInfo()));
		}
		
		if(read.getsNextPeriodAct()!= null) {
			parameterList.add(EntireReadUtil.longParameter("s_ExpireDate", read.getsNextPeriodAct()));
		}
	
		return operation;
	}
	
	public static Operation createWelcomePackVersionRead(WelcomePackVersionRead read) {
		Operation operation = new Operation();
		operation.setName("VersionRead");
		operation.setModifier("RPP_s_subLifeIncentive");
		
		List<Object> parameterList = operation.getParameterList().getParameterOrBooleanParameterOrByteParameter();
		parameterList.add(EntireReadUtil.stringParameter("CustomerId", read.getCustomerId()));
	
		if(read.getCategory() != null) {
			parameterList.add(EntireReadUtil.enumerationValueParameter("Category", read.getCategory()));
		}
		
		if(read.getKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("Key", read.getKey()));
		}
		
		if(read.getOfferProfileKey() != null) {
			parameterList.add(EntireReadUtil.intParameter("OfferProfileKey", read.getOfferProfileKey()));
		}
		
		if(read.getvInvalidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vInvalidFrom", read.getvInvalidFrom()));
		}
		
		if(read.getvValidFrom() != null) {
			parameterList.add(EntireReadUtil.symbolicOrDateParameter("vValidFrom", read.getvValidFrom()));
		}
		
		return operation;
	}
	

	
}
