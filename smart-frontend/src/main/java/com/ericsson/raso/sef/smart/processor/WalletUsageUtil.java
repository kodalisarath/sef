package com.ericsson.raso.sef.smart.processor;

import java.util.List;

import org.joda.time.DateTime;

import com.ericsson.raso.sef.core.SefCoreServiceResolver;
import com.ericsson.raso.sef.core.config.IConfig;
import com.ericsson.raso.sef.core.config.Property;
import com.ericsson.raso.sef.core.config.Section;
import com.ericsson.raso.sef.smart.commons.SmartConstants;

public class WalletUsageUtil {

	public static WalletUsage getWalletUsage(String wallet) {

		IConfig config = SefCoreServiceResolver.getConfigService();
		Section section = config.getSection("Global_usageTimingsStartWindow");
		List<Property> propertyList = SefCoreServiceResolver.getConfigService()
				.getProperties(section);
		String selectedWindowName = "";
		for (Property property : propertyList) {
			String usageWindowName = property.getKey();
			String startWindow = config.getValue(
					"Global_usageTimingsStartWindow", usageWindowName);
			String endWindow = config.getValue("Global_usageTimingsEndWindow",
					usageWindowName);

			DateTime start = toDate(startWindow);
			DateTime end = toDate(endWindow);

			DateTime current = new DateTime();

			if (current.isAfter(start) && current.isBefore(end)) {
				selectedWindowName = usageWindowName;
				break;
			}

			if (selectedWindowName != null) {
				
				WalletUsage walletUsage = new WalletUsage();
				String uc = config.getValue(
						"uc_walletUsage_"+selectedWindowName, wallet);
				String ut = config.getValue(
						"ut_walletUsage_"+selectedWindowName, wallet);
				if(uc != null)
					walletUsage.setUc(Integer.parseInt(uc));
				if(ut != null)
					walletUsage.setUt(Integer.parseInt(ut));
				if(uc !=null  || ut != null)
				{
					walletUsage.setName(wallet);
					return walletUsage;
				}
			}
		}
			return null;
			
		}

		
		
	private static DateTime toDate(String windowTime) {
		windowTime = windowTime.replace("TODAY-", "");
		boolean tomorrow = false;
		if (windowTime.contains("TOMORROW")) {
			windowTime = windowTime.replace("TOMORROW-", "");
			tomorrow = true;
		}
		String[] time = windowTime.split(":");
		DateTime dateTime = new DateTime();
		int dayOfMonth = tomorrow ? dateTime.getDayOfMonth() + 1 : dateTime
				.getDayOfMonth();
		return new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
				dayOfMonth, Integer.valueOf(time[0]), Integer.valueOf(time[1]));
	}

	public static void main(String[] args) {
		String startWindow = "TODAY-00:00";
		String endWindow = "TODAY-23:00";

		DateTime start = toDate(startWindow);
		DateTime end = toDate(endWindow);

		DateTime current = new DateTime();

		if (current.isAfter(start) && current.isBefore(end)) {
			System.out.println("sds");
		}

	}

}
