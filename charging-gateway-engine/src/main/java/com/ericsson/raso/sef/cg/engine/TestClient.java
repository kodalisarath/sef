package com.ericsson.raso.sef.cg.engine;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ericsson.pps.diameter.dccapi.DCCStack;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdDataAvp;
import com.ericsson.pps.diameter.dccapi.avp.SubscriptionIdTypeAvp;
import com.ericsson.pps.diameter.dccapi.command.Cca;
import com.ericsson.pps.diameter.dccapi.command.Ccr;
import com.ericsson.pps.diameter.rfcapi.base.DiameterStack;
import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.pps.diameter.rfcapi.base.avp.AvpDataException;
import com.ericsson.pps.diameter.rfcapi.base.avp.avpdatatypes.Time;
import com.ericsson.raso.sef.cg.engine.util.ScapChargingApi;
import com.ericsson.raso.sef.core.cg.diameter.ChargingInfo;
import com.ericsson.raso.sef.core.cg.diameter.DiameterStackBuilder;
import com.ericsson.raso.sef.core.cg.diameter.DiameterStackBuilder.Stack;
import com.ericsson.raso.sef.core.cg.nsn.avp.MethodNameAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.PPIInformationAvp;
import com.ericsson.raso.sef.core.cg.nsn.avp.ServiceInfoAvp;


public class TestClient {

	public static void testChargingGateway() throws Exception {
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.SCAPV2);

		//localIP
		builder.ownStackUri("aaa://10.132.102.103:3868;transport=tcp");
		builder.ownProductId("ILCG");
		builder.ownRealm("smart.com.ph");
		builder.tcpPort(3868);

		//target IP
		builder.addStaticRoute("smart.com.ph", "aaa://10.202.160.5:3868;transport=tcp");

		DCCStack stack = builder.build();

		stack.start();
		System.out.println("Start");
		//		Cca cca =  createGetConsumerAccListRequest(stack.getDiameterStack()).send() ;//ccrMessage.send();
		Cca cca = createCommitRequest(stack.getDiameterStack()).send();
		System.out.println(cca.getResultCode());
		System.out.println("sent CGW");
	}


	public static void main(String[] args) throws Exception {

//		testAll();
//				testCommit();
		
//		testGetConsumerAcc("639777000011","10.245.144.167");
//									testAuthorise("639465490112","2","10.202.160.5");
		//4 is for authorize 5 is for comit
		// 2 is for direct debit
		// 194 is for get consumer acc
									
		testAuthorise("639777000011","2","10.202.160.10"); 	
//					testChargingGateway();	

		// 21,23,24,25 is for authorised
		// 10,22 is for direct debit
/*
		
		ChargingInfo chargingInfo = readObject("21");

		Map<String, Object> map = DiameterUtil.toMap(chargingInfo.getAvpList());
		System.out.println(map.size());
*/		
	}

	public static void testAuthorise(String msisdn,String obj,String server) throws Exception 
	{
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.SCAPV2);

		//localIP
		builder.ownStackUri("aaa://10.132.133.63:3868;transport=tcp");
		builder.ownProductId("ILCG");
		builder.ownRealm("smart.com.ph");
		builder.tcpPort(3868);

		//target IP
		builder.addStaticRoute("smart.com.ph", "aaa://"+server+":3868;transport=tcp");

		DCCStack stack = builder.build();

		stack.start();
		System.out.println("Start");

		ChargingInfo chargingInfo = readObject(obj/*"4"*/);
		Ccr ccrMessage = new Ccr("NSN1234567890", stack.getDiameterStack(), ScapChargingApi.Service_Context_Id);


		List<Avp> subAvplist = new ArrayList<Avp>();

		for(Avp avp : chargingInfo.getAvpList())
		{
			if(avp.getAvpCode() != SubscriptionIdAvp.AVP_CODE )
			{
				subAvplist.add(avp);
			}
		}


		ccrMessage.getDiameterMessage().setAvps(subAvplist);

		ccrMessage.setSessionId("Mark_C_138544891801");
		ccrMessage.setDestinationRealm("smart.com.ph");

		System.out.println(ccrMessage.getSessionId());
		//target IP
		ccrMessage.setDestinationHost(server);
		ccrMessage.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));

		SubscriptionIdTypeAvp type = new SubscriptionIdTypeAvp();
		type.setValue(SubscriptionIdTypeAvp.END_USER_E164);
		SubscriptionIdDataAvp data = new SubscriptionIdDataAvp();
		data.setValue(msisdn);
		// adding nested AVP�s in SubscriptionIdAvp.
		SubscriptionIdAvp subscriptionId = new SubscriptionIdAvp();
		subscriptionId.addSubAvp(type);
		subscriptionId.addSubAvp(data);
		ccrMessage.addAvp(subscriptionId);



		System.out.println("sending CGW");

		Cca cca = ccrMessage.send(); 
		System.out.println(cca.getResultCode());
		System.out.println("sent CGW");
		System.exit(0);
	}

	public static void testAll() throws Exception 
	{
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.SCAPV2);

		//localIP
		builder.ownStackUri("aaa://10.132.133.65:3868;transport=tcp");
		builder.ownProductId("ILCG");
		builder.ownRealm("smart.com.ph");
		builder.tcpPort(3868);

		//target IP
		builder.addStaticRoute("smart.com.ph", "aaa://10.202.160.5:3868;transport=tcp");

		DCCStack stack = builder.build();

		stack.start();
		System.out.println("Start Authorise");

		ChargingInfo chargingInfo = readObject("4");
		Ccr ccrMessage = new Ccr("NSN1234567890", stack.getDiameterStack(), ScapChargingApi.Service_Context_Id);

		ccrMessage.getDiameterMessage().setAvps(chargingInfo.getAvpList());

		ccrMessage.setSessionId("TKJPAE-FW97");
		ccrMessage.setDestinationRealm("smart.com.ph");

		System.out.println(ccrMessage.getSessionId());
		//target IP
		ccrMessage.setDestinationHost("10.202.160.5");
		ccrMessage.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));

		System.out.println("sending Authorise CGW");

		Cca cca = ccrMessage.send(); 
		System.out.println(cca.getResultCode());
		System.out.println("sent Authorise CGW");
		System.exit(0);
	}

	public static void testCommit( ) throws Exception 
	{
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.SCAPV2);

		//localIP
		builder.ownStackUri("aaa://10.132.133.65:3868;transport=tcp");
		builder.ownProductId("ILCG");
		builder.ownRealm("smart.com.ph");
		builder.tcpPort(3868);

		//target IP
		builder.addStaticRoute("smart.com.ph", "aaa://10.202.160.5:3868;transport=tcp");

		DCCStack stack = builder.build();

		stack.start();
		System.out.println("Start commit");

		ChargingInfo chargingInfo = readObject("5");
		Ccr ccrMessage = new Ccr("NSN1234567890", stack.getDiameterStack(), ScapChargingApi.Service_Context_Id);

		ccrMessage.getDiameterMessage().setAvps(chargingInfo.getAvpList());

		ccrMessage.setSessionId("Mark_C_13854408891001");
		ccrMessage.setDestinationRealm("smart.com.ph");

		System.out.println(ccrMessage.getSessionId());
		//target IP
		ccrMessage.setDestinationHost("10.202.160.5");
		ccrMessage.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));

		System.out.println("sending commit CGW");

		Cca cca = ccrMessage.send(); 
		System.out.println(cca.getResultCode());
		System.out.println("sent commit CGW");
		System.exit(0);
	}


	public static Ccr createGetConsumerAccListRequest(DiameterStack stack) {
		Ccr ccrMessage = new Ccr("NSN1234567890", stack, ScapChargingApi.Service_Context_Id);


		ccrMessage.setSessionId(String.valueOf(1234217890/*System.currentTimeMillis()*/));
		ccrMessage.setDestinationRealm("smart.com.ph");

		try {
			System.out.println(ccrMessage.getSessionId());
		} catch (AvpDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// target IP
		ccrMessage.setDestinationHost("10.202.160.5");
		ccrMessage.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));

		SubscriptionIdTypeAvp type = new SubscriptionIdTypeAvp();
		type.setValue(SubscriptionIdTypeAvp.END_USER_E164);
		SubscriptionIdDataAvp data = new SubscriptionIdDataAvp();
		String MSISDN = "639465429831";
		data.setValue(MSISDN);
		SubscriptionIdAvp subscriptionId = new SubscriptionIdAvp();
		subscriptionId.addSubAvp(type);
		subscriptionId.addSubAvp(data);
		ccrMessage.addAvp(subscriptionId);

		ccrMessage.setRequestedAction(2);
		ccrMessage.setCCRequestType(4);
		ccrMessage.setCCRequestNumber(0);


		PPIInformationAvp informationAvp = new PPIInformationAvp();
		informationAvp.addProductId("J");
		informationAvp.addAccessFrontendId("3572217308|EIRA-CRBT|VAS|AO|||");
		informationAvp.addConsumerAccountId(1);
		informationAvp.addCurrency("GEN");
		informationAvp.addSubAvp(new MethodNameAvp(2));		
		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp();
		serviceInfoAvp.addPPiInformationAvp(informationAvp);

		ccrMessage.addAvp(serviceInfoAvp);

		return ccrMessage;
	}




	public static Ccr createCommitRequest(DiameterStack stack) {
		Ccr ccrMessage = new Ccr("NSN1234567890", stack, ScapChargingApi.Service_Context_Id);

		ccrMessage.setSessionId(String.valueOf(1234517990/*System.currentTimeMillis()*/));
		ccrMessage.setDestinationRealm("smart.com.ph");

		try {
			System.out.println(ccrMessage.getSessionId());
		} catch (AvpDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// target IP
		ccrMessage.setDestinationHost("10.202.160.5");
		ccrMessage.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));

		SubscriptionIdTypeAvp type = new SubscriptionIdTypeAvp();
		type.setValue(SubscriptionIdTypeAvp.END_USER_E164);
		SubscriptionIdDataAvp data = new SubscriptionIdDataAvp();
		String MSISDN = "639465429831";
		data.setValue(MSISDN);
		SubscriptionIdAvp subscriptionId = new SubscriptionIdAvp();
		subscriptionId.addSubAvp(type);
		subscriptionId.addSubAvp(data);
		ccrMessage.addAvp(subscriptionId);

		//		ccrMessage.setRequestedAction(2);
		ccrMessage.setCCRequestType(3);
		ccrMessage.setCCRequestNumber(1);


		PPIInformationAvp informationAvp = new PPIInformationAvp();
		informationAvp.addProductId("J");
		informationAvp.addAccessFrontendId("3572217308|EIRA-CRBT|VAS|AO|||");
		informationAvp.addConsumerAccountId(1);
		informationAvp.addCurrency("GEN");
		informationAvp.addSubAvp(new MethodNameAvp(2));		
		ServiceInfoAvp serviceInfoAvp = new ServiceInfoAvp();
		serviceInfoAvp.addPPiInformationAvp(informationAvp);

		ccrMessage.addAvp(serviceInfoAvp);

		return ccrMessage;
	}


	private static ChargingInfo readObject(String objectId) throws Exception {
		InputStream stream = TestClient.class.getClassLoader().getResourceAsStream("requests/" + objectId);
		ObjectInputStream objectInputStream = new ObjectInputStream(stream);
		ChargingInfo info = (ChargingInfo) objectInputStream.readObject();
		return info;
	}

	public static void main1(String[] args) throws Exception {
		ChargingInfo chargingInfo = readObject("21");
		System.out.println(toMap(chargingInfo.getAvpList()));

	}

	public static Map<String, Object> toMap(Collection<Avp> avps)  {
		if(avps == null) {
			return null;
		}
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		for (Avp avp : avps) {
			Collection<Avp> list = null;
			try {
				list = avp.getDataAsGroup();
			} catch (Exception e) {
				//Eat me
			}

			if(list == null) {
				map.put(String.valueOf(avp.getAvpCode()), new String(avp.getData()));
			} else {
				map.put(String.valueOf(avp.getAvpCode()), toMap(list));
			}
		}
		return map;
	
	}
	

	public static void testGetConsumerAcc(String msisdn,String server) throws Exception 
	{
		DiameterStackBuilder builder = new DiameterStackBuilder(Stack.SCAPV2);

		//localIP
		builder.ownStackUri("aaa://10.132.133.65:3868;transport=tcp");
		builder.ownProductId("ILCG");
		builder.ownRealm("smart.com.ph");
		builder.tcpPort(3868);

		//target IP
		builder.addStaticRoute("smart.com.ph", "aaa://"+server+":3868;transport=tcp");

		DCCStack stack = builder.build();

		stack.start();
		System.out.println("Start Get consumer Acc");

		ChargingInfo chargingInfo = readObject("194");
		Ccr ccrMessage = new Ccr("NSN1234567890", stack.getDiameterStack(), ScapChargingApi.Service_Context_Id);


		List<Avp> subAvplist = new ArrayList<Avp>();

		for(Avp avp : chargingInfo.getAvpList())
		{
			if(avp.getAvpCode() != SubscriptionIdAvp.AVP_CODE )
			{
				subAvplist.add(avp);
			}
		}


		ccrMessage.getDiameterMessage().setAvps(subAvplist);
		
		ccrMessage.setSessionId("TKJPAE-FW97");
		ccrMessage.setDestinationRealm("smart.com.ph");

		System.out.println(ccrMessage.getSessionId());
		//target IP
		ccrMessage.setDestinationHost(server);
		ccrMessage.setEventTimestamp(new Time(new Date(System.currentTimeMillis())));


		SubscriptionIdTypeAvp type = new SubscriptionIdTypeAvp();
		type.setValue(SubscriptionIdTypeAvp.END_USER_E164);
		SubscriptionIdDataAvp data = new SubscriptionIdDataAvp();
		data.setValue(msisdn);
		// adding nested AVP�s in SubscriptionIdAvp.
		SubscriptionIdAvp subscriptionId = new SubscriptionIdAvp();
		subscriptionId.addSubAvp(type);
		subscriptionId.addSubAvp(data);
		ccrMessage.addAvp(subscriptionId);

		System.out.println("sending Get consumer Acc CGW");

		Cca cca = ccrMessage.send(); 
		System.out.println(cca.getResultCode());
		System.out.println("sent Get consumer Acc CGW");
		System.exit(0);
	}
}
