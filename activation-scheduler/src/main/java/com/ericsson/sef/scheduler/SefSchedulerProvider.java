package com.ericsson.sef.scheduler;

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.utils.ConnectionProvider;

public class SefSchedulerProvider implements ConnectionProvider {

	@Override
	public Connection getConnection() throws SQLException {
		return SchedulerContext.getDataSource().getConnection();
	}

	@Override
	public void shutdown() throws SQLException {
		//Data source is Managed by Spring
	}

	@Override
	public void initialize() throws SQLException {
		//Data source is Managed by Spring
	}
}
