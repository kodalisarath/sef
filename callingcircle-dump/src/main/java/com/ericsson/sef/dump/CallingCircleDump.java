package com.ericsson.sef.dump;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class CallingCircleDump {

	public static void main(String[] args) throws Exception {

		String inputDate = "02-07-2014";
		if (args.length > 0)
			inputDate = args[0];
		Properties properties = new Properties();
		properties.load(CallingCircleDump.class.getClassLoader()
				.getResourceAsStream("callingcircle-dump.properties"));
		// SecureSerializationHelper encryptor = new
		// SecureSerializationHelper();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String sql = String.format(
				properties.getProperty("callingcircle.query"), inputDate);
		System.out.println("SQL : " + sql);

		Class.forName(properties.getProperty("jdbc.driverclass"));
		Connection connection = DriverManager.getConnection(
				properties.getProperty("jdbc.url"),
				properties.getProperty("jdbc.username"),
				properties.getProperty("jdbc.password"));
		PreparedStatement stmt = connection.prepareStatement(sql);

		ResultSet rset = stmt.executeQuery();

		Date date = new Date();

		if (rset != null) {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
					new File(properties.getProperty("file_location") + "/"
							+ (inputDate) + ".csv")));
			System.out
					.println("APARTY, BPARTY, PRODUCT_ID, RELATIONSHIP, EXPIRY_TIME, CREATION_TIME");
			bufferedWriter
					.write("APARTY, BPARTY, PRODUCT_ID, RELATIONSHIP, EXPIRY_TIME, CREATION_TIME");
			while (rset.next()) {
				// msisdn, member_b, product_offer, relationship,
				// expiry_datetime, creation_datetime
				System.out.println(String.format(
						"%s,%s,%s,%s,%s,%s",
						(rset.getString(1)),
						(rset.getString(2)),
						rset.getString(3),
						rset.getString(4),
						sdf.format(rset.getDate(5)),
						rset.getDate(6) == null ? null : sdf.format(rset
								.getDate(6))));
				bufferedWriter.write("\n");
				bufferedWriter.write(String.format(
						"%s,%s,%s,%s,%s,%s",
						(rset.getString(1)),
						(rset.getString(2)),
						rset.getString(3),
						rset.getString(4),
						sdf.format(rset.getDate(5)),
						rset.getDate(6) == null ? null : sdf.format(rset
								.getDate(6))));
			}

			bufferedWriter.close();
		}

	}

}
