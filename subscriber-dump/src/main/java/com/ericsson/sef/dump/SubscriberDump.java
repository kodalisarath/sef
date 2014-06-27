package com.ericsson.sef.dump;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;

import com.ericsson.raso.sef.core.SecureSerializationHelper;

public class SubscriberDump {

 
	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Properties properties = new Properties();
		properties.load(SubscriberDump.class.getClassLoader().getResourceAsStream("subscriber-dump.properties"));
		SecureSerializationHelper encryptor = new SecureSerializationHelper();
		
		//(String)encryptor.decrypt(org.apache.commons.codec.binary.Base64.decodeBase64(query.getOwner()))
		Class.forName(properties.getProperty("jdbc.driverclass"));

		//select msisdn, member_b, product_offer, relationship, expiry_datetime, creation_datetime  from subscriber a, callingcircle b
		//opwhere a.msisdn = b.owner and trunc(creation_datetime,'DAY') = TO_DATE('19-09-2014', 'DD-MM-YYYY');
		 
		Connection connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
		String sql = "select msisdn, contract_state, b.attribute_name, attribute_value from subscriber a, subscriber_meta b where"
				+ " a.user_id = b.user_id and attribute_name in (?,?,?,?,?) and contract_state = ? order by msisdn";

		PreparedStatement stmt = connection.prepareStatement(sql);
		stmt.setString(1, "vValidFrom");
		stmt.setString(2, "preActiveEndDate");
		stmt.setString(3, "tagging");
		stmt.setString(4, "vInvalidFrom");
		stmt.setString(5, "package");
		stmt.setString(6, properties.getProperty("subscriber.status"));

		ResultSet rset = stmt.executeQuery();
		
		Map<String, Dump> map = new HashMap<String, Dump>();
		
		Date date = new Date();
		while (rset.next()) {
			String msisdn = rset.getString(1);
			Dump dump = map.get(msisdn);
			if(dump == null) {
				dump = new Dump();
				dump.setDate(date);
				dump.setMsisdn(String.valueOf(encryptor.decrypt(Base64.decodeBase64(msisdn))));
				dump.setContract_state(rset.getString(2));
				map.put(msisdn, dump);
			}
			
			String attrName = rset.getString(3);
			if(attrName.equals("vValidFrom")) {
				dump.setvValidFrom(rset.getString(4));
			}
			
			if(attrName.equals("preActiveEndDate")) {
				dump.setPreActiveEndDate(rset.getString(4));
			}
			
			if(attrName.equals("tagging")) {
				dump.setTagging(rset.getString(4));
			}
			
			if(attrName.equals("package")) {
				dump.setPackaze(rset.getString(4));
			}
			
			if(attrName.equals("vInvalidFrom")) {
				dump.setvInvalidFrom(rset.getString(4));
			}
		}
		
		System.out.println("dump_date, msisdn, contract_state, preActiveEnddate, C_TagStatus, vValidFrom, vInvalidFrom, welcomePack");
		for (Map.Entry<String, Dump> dumps : map.entrySet()) {
			Dump dump = dumps.getValue();
			System.out.println(sdf.format(dump.getDate()) + "," + dump.getMsisdn() + "," + dump.getContract_state() + "," + dump.getPreActiveEndDate() + "," + dump.getTagging() + ","
					+ dump.getvInvalidFrom() + "," + dump.getvValidFrom() + "," + dump.getPackaze());
		}
		

	}
	public static class Dump {
		String msisdn;
		String contract_state;
		String vValidFrom;
		String preActiveEndDate;
		String tagging;
		String vInvalidFrom;
		String packaze;
		Date date;

		public String getMsisdn() {
			return msisdn;
		}

		public void setMsisdn(String msisdn) {
			this.msisdn = msisdn;
		}

		public String getContract_state() {
			return contract_state;
		}

		public void setContract_state(String contract_state) {
			this.contract_state = contract_state;
		}

		public String getvValidFrom() {
			return vValidFrom;
		}

		public void setvValidFrom(String vValidFrom) {
			this.vValidFrom = vValidFrom;
		}

		public String getPreActiveEndDate() {
			return preActiveEndDate;
		}

		public void setPreActiveEndDate(String preActiveEndDate) {
			this.preActiveEndDate = preActiveEndDate;
		}

		public String getTagging() {
			return tagging;
		}

		public void setTagging(String tagging) {
			this.tagging = tagging;
		}

		public String getvInvalidFrom() {
			return vInvalidFrom;
		}

		public void setvInvalidFrom(String vInvalidFrom) {
			this.vInvalidFrom = vInvalidFrom;
		}

		public String getPackaze() {
			return packaze;
		}

		public void setPackaze(String packaze) {
			this.packaze = packaze;
		}
		
		public Date getDate() {
			return date;
		}
		
		public void setDate(Date date) {
			this.date = date;
		}
	}

}
