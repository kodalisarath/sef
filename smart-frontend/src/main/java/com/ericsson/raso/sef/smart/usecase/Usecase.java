package com.ericsson.raso.sef.smart.usecase;

import org.apache.camel.Processor;

import com.ericsson.raso.sef.smart.SmartServiceResolver;
import com.ericsson.raso.sef.smart.processor.BalanceAdjustment;
import com.ericsson.raso.sef.smart.processor.BucketCreateOrWriteRop;
import com.ericsson.raso.sef.smart.processor.BucketRetrieveReadROP;
import com.ericsson.raso.sef.smart.processor.BucketRetrieveReadRPP;
import com.ericsson.raso.sef.smart.processor.CARecharge;
import com.ericsson.raso.sef.smart.processor.CreateOrWriteCustomerProcessor;
import com.ericsson.raso.sef.smart.processor.CreateOrWriteRop;
import com.ericsson.raso.sef.smart.processor.CreateOrWriteServiceAccessKey;
import com.ericsson.raso.sef.smart.processor.DummyProcessor;
import com.ericsson.raso.sef.smart.processor.EntireDeleteSubscriber;
import com.ericsson.raso.sef.smart.processor.EntireReadSubscriber;
import com.ericsson.raso.sef.smart.processor.ModifyCustomerGrace;
import com.ericsson.raso.sef.smart.processor.ModifyCustomerPreActive;
import com.ericsson.raso.sef.smart.processor.ModifyPackageItem;
import com.ericsson.raso.sef.smart.processor.ModifyTagging;
import com.ericsson.raso.sef.smart.processor.ReadCustomerInfoCharge;
import com.ericsson.raso.sef.smart.processor.RetrieveDeleteProcessor;
import com.ericsson.raso.sef.smart.processor.RetrieveReadRPP;
import com.ericsson.raso.sef.smart.processor.SubscribePackageItem;
import com.ericsson.raso.sef.smart.processor.UnsubscribePackageItem;
import com.ericsson.raso.sef.smart.processor.VersionCreateOrWriteCustomer;
import com.ericsson.raso.sef.smart.processor.VersionCreateOrWriteRop;
import com.ericsson.raso.sef.smart.processor.VersionRetrieveReadROP;
import com.nsn.ossbss.charge_once.wsdl.entity.tis.xsd._1.Operation;

public enum Usecase {

	RECHARGE("Recharge", null, CARecharge.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new RechargeRequest();
		}
	},
	ENTIRE_READ("EntireRead", "Customer", EntireReadSubscriber.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new EntireReadRequest();
		}
	},
	ROP_READ("Read", "ROP", EntireReadSubscriber.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new EntireReadRequest();
		}
	},
	BUCKET_CREATE_OR_WRITE_ROP("BucketCreateOrWrite", "ROP", BucketCreateOrWriteRop.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new BucketCreateOrWriteRopRequest();
		}
	},
	CREATE_OR_WRITE_CUSTOMER("CreateOrWrite", "Customer", CreateOrWriteCustomerProcessor.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new CreateOrWriteCustomerRequest();
		}
	},
	CREATE_OR_WRITE_SERVICE_ACCESS_KEY("CreateOrWrite", "ServiceAccessKey", CreateOrWriteServiceAccessKey.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new CreateOrWriteServiceAccessKeyRequest();
		}
	},
	CREATE_OR_WRITE_ROP("CreateOrWrite", "ROP", CreateOrWriteRop.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new CreateOrWriteRopRequest();
		}
	},
	ENTIRE_DELETE_SUBSCRIBER("EntireDelete", "Customer", EntireDeleteSubscriber.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new EntireDeleteRequest();
		}
	},
	MODIFY_CUSTOMER_GRACE("Modify", "CustomerGrace", ModifyCustomerGrace.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new ModifyCustomerGraceRequest();
		}
	},
	MODIFY_CUSTOMER_PREACTIVE("Modify", "CustomerPreActive", ModifyCustomerPreActive.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new ModifyCustomerPreActiveRequest();
		}
	},
	MODIFY_PACKAGE_ITEM("Modify", "PackageItem", ModifyPackageItem.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new ModifyPackageItemRequest();
		}
	},
	MODIFY_TAGGING("Modify", "Tagging", ModifyTagging.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new ModifyTaggingRequest();
		}
	},
	READ_CUSTOMER_INFO_CHARGE("Read", "CustomerInfoCharge", ReadCustomerInfoCharge.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new ReadCustomerInfoChargeRequest();
		}
	},
	RETRIEVE_DELETE("RetrieveDelete", "ServiceAccessKey", RetrieveDeleteProcessor.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new RetrieveDeleteRequest();
		}
	},
	RETRIEVE_READ_RPP("RetrieveRead", "RPP", RetrieveReadRPP.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new RetrieveReadRPPRequest();
		}
	},
	SUBSCRIBE_PACKAGE_ITEM("Subscribe", "PackageItem", SubscribePackageItem.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new SubscribePackageItemRequest();
		}
	},
	UNSUBSCRIBE_PACKAGE_ITEM("Unsubscribe", "PackageItem", UnsubscribePackageItem.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new UnSubscribePackageItemRequest();
		}
	},
	VERSION_CREATE_OR_WRITE_CUSTOMER("VersionCreateOrWrite", "Customer", VersionCreateOrWriteCustomer.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new VersionCreateOrWriteCustomerRequest();
		}
	},
	VERSION_CREATE_OR_WRITE_ROP("VersionCreateOrWrite", "ROP", VersionCreateOrWriteRop.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new VersionCreateOrWriteROPRequest();
		}
	},
	VERSION_RETRIEVE_READ_ROP("VersionRetrieveRead", "ROP", VersionRetrieveReadROP.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new VersionRetrieveReadROPRequest();
		}
	},
	BALANCE_ADJUSTMENT("BalanceAdjustment", null, BalanceAdjustment.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new BalanceAdjustmentRequest();
		}
	},
	BUCKET_RETRIEVE_READ_ROP("BucketRetrieveRead", "ROP", BucketRetrieveReadROP.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new BucketRetrieveReadROPRequest();
		}
	},
	BUCKET_RETRIEVE_READ_RPP("BucketRetrieveRead", "RPP", BucketRetrieveReadRPP.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new BucketRetrieveReadRPPRequest();
		}
	},
	DUMMY("", "", DummyProcessor.class) {
		@Override
		public SmartRequest cerateRequest() {
			return new DummyRequest();
		}
	};

	private String operation;
	private String modifier;
	private Class<? extends Processor> processorType;

	private Usecase(String operation, String modifier, Class<? extends Processor> processorType) {
		this.operation = operation;
		this.modifier = modifier;
		this.processorType = processorType;
	}

	public String getOperation() {
		return operation;
	}

	public String getModifier() {
		return modifier;
	}

	public static Usecase getUsecaseByOperation(Operation operation) {
		for (Usecase uc : Usecase.values()) {
			if (uc.getOperation().equalsIgnoreCase(operation.getName())) {
				if (uc.getModifier() == null) {
					return uc;
				} else if (uc.getModifier().equalsIgnoreCase(operation.getModifier())) {
					return uc;
				}
			}
		}
		return Usecase.DUMMY;
	}

	public abstract SmartRequest cerateRequest();
	
	public Processor getRequestProcessor() {
		return SmartServiceResolver.getBean(processorType);
	}
}
