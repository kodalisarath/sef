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

public class CallingCircleDump {

 
	
	public static void main(String[] args) throws Exception {
		
		String inputDate = "2099-09-09";
		if (args.length>0)
			inputDate = args[0];
		Properties properties = new Properties();
		properties.load(CallingCircleDump.class.getClassLoader().getResourceAsStream("subscriber-dump.properties"));
		SecureSerializationHelper encryptor = new SecureSerializationHelper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Class.forName(properties.getProperty("jdbc.driverclass"));

		Connection connection = DriverManager.getConnection(properties.getProperty("jdbc.url"), properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
		String sql = String.format(properties.getProperty("callingcircle.query"), inputDate);

		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rset = stmt.executeQuery();
		
		Date date = new Date();
		System.out.println("APARTY, BPARTY, PRODUCT_ID, RELATIONSHIP, EXPIRY_TIME, CREATION_TIME");
		while (rset.next()) {
			//msisdn, member_b, product_offer, relationship, expiry_datetime, creation_datetime
			System.out.println(String.format("%s,%s,%s,%s,%s,%s", 
					encryptor.decrypt(Base64.decodeBase64(rset.getString(1))),
					encryptor.decrypt(Base64.decodeBase64(rset.getString(2))),
					rset.getString(3),
					rset.getString(4),
					sdf.format(rset.getDate(5)),
					sdf.format(rset.getDate(6))
					));
		}
	}

}
