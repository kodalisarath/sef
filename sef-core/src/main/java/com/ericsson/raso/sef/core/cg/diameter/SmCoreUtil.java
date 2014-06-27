package com.ericsson.raso.sef.core.cg.diameter;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SmCoreUtil {

	public static String getServerIP(String eth) {
		try {
			Enumeration<NetworkInterface> nwInterfaces = NetworkInterface.getNetworkInterfaces();
			while (nwInterfaces.hasMoreElements()) {
				NetworkInterface nwInterface = nwInterfaces.nextElement();
				if (nwInterface.getName().equalsIgnoreCase(eth)) {
					Enumeration<InetAddress> addresses = nwInterface.getInetAddresses();
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
			Enumeration<NetworkInterface> nwInterfaces = NetworkInterface.getNetworkInterfaces();
			while (nwInterfaces.hasMoreElements()) {
				NetworkInterface nwInterface = nwInterfaces.nextElement();
				Enumeration<InetAddress> addresses = nwInterface.getInetAddresses();
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
			if (list2.contains(t)){
				list.add(t);
			}
		}
		return list;
	}
}
