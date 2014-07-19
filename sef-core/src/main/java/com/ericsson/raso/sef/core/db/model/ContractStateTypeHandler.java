package com.ericsson.raso.sef.core.db.model;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ContractStateTypeHandler extends EnumTypeHandler<ContractState> {

	public ContractStateTypeHandler(Class<ContractState> type) {
		super(type);
		
	}

	@Override
	public ContractState getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		String s = cs.getString(columnIndex);
	    return s == null ? null : ContractState.apiValue(s);
	}

	@Override
	public ContractState getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		 String s = rs.getString(columnIndex);
		    return s == null ? null : ContractState.apiValue(s);
	}

	@Override
	public ContractState getNullableResult(ResultSet rs, String columnName) throws SQLException {
		 String s = rs.getString(columnName);
		    return s == null ? null : ContractState.apiValue(s);
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, ContractState parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.getName());
	}
	
	
	

}
