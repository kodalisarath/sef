package com.ericsson.raso.sef.cg.engine.util;

import java.net.InetAddress;
import java.lang.reflect.Type;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.pps.diameter.rfcapi.base.avp.Avp;
import com.ericsson.raso.sef.core.cg.model.Operation;
import com.ericsson.raso.sef.core.db.model.smart.ChargingSession;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SefCoreUtil {
	private final static Logger logger = LoggerFactory.getLogger(SefCoreUtil.class);
	public static String getServerIP(String eth) {
		try {
			Enumeration<NetworkInterface> nwInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (nwInterfaces.hasMoreElements()) {
				NetworkInterface nwInterface = nwInterfaces.nextElement();
				if (nwInterface.getName().equalsIgnoreCase(eth)) {
					Enumeration<InetAddress> addresses = nwInterface
							.getInetAddresses();
					while (addresses.hasMoreElements()) {
						InetAddress address = addresses.nextElement();
						return address.getHostAddress();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<String> getServerIPList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> nwInterfaces = NetworkInterface
					.getNetworkInterfaces();
			while (nwInterfaces.hasMoreElements()) {
				NetworkInterface nwInterface = nwInterfaces.nextElement();
				Enumeration<InetAddress> addresses = nwInterface
						.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress address = addresses.nextElement();
					ipList.add(address.getHostAddress());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ipList;
	}

	public static <T> List<T> intersection(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<T>();
		for (T t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}

	public static ChargingSession convertToGSONFormat(
			ChargingSession chargingSession) {
		//GsonDataFormat gsonDataFormat = new GsonDataFormat();
		Gson gson = new Gson();
		if(logger.isDebugEnabled())
		logger.debug("Gson Object is "+gson);
		if (chargingSession.getRequestAvpMap() != null && chargingSession.getRequestAvpMap().size()>0)
			chargingSession.setRequestAVPMapInJSONFormat(gson.toJson(chargingSession.getRequestAvpMap()));
		if (chargingSession.getResponseAvpMap() != null && chargingSession.getResponseAvpMap().size()>0)
			chargingSession.setResponseAvpMapInJSONFormat(gson.toJson(chargingSession.getResponseAvpMap()));
		return chargingSession;
	}

	public static ChargingSession convertFromGSONFormat(
			ChargingSession chargingSession) {
		//GsonDataFormat gsonDataFormat = new GsonDataFormat();
		Gson gson = new Gson();
		if(logger.isDebugEnabled())
			logger.debug("Gson Object is "+gson);
		
		Type mapStringObjectType = new TypeToken<Map<Operation.Type, List<Avp>>>(){}.getType();
		
		if (chargingSession.getRequestAVPMapInJSONFormat() != null)
			chargingSession.setRequestAvpMap((Map<Operation.Type, List<Avp>>)gson
					.fromJson(chargingSession.getRequestAVPMapInJSONFormat(),mapStringObjectType));
		if (chargingSession.getResponseAvpMapInJSONFormat() != null)
			chargingSession.setResponseAvpMap((Map<Operation.Type, List<Avp>>)gson
					.fromJson(chargingSession.getResponseAvpMapInJSONFormat(),mapStringObjectType));
		return chargingSession;
	}

}