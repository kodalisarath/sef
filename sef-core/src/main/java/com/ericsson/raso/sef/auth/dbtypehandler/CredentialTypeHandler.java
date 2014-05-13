package com.ericsson.raso.sef.auth.dbtypehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.ericsson.raso.sef.auth.Credential;


@MappedTypes(Credential.Type.class)
public class CredentialTypeHandler extends BaseTypeHandler<Credential.Type> {

	@Override
	public void setNonNullParameter(PreparedStatement preparedStatement, int i,
			Credential.Type status, JdbcType jdbcType) throws SQLException {

		if ((jdbcType == null) || (jdbcType.equals(JdbcType.VARCHAR))) {
			preparedStatement.setString(i, status.name());
		} else {
			throw new UnsupportedOperationException(
					"Unable to convert Credential Status to "
							+ jdbcType.toString());
		}

	}

	@Override
	public Credential.Type getNullableResult(ResultSet rs, String columnName) throws SQLException {
		String name = rs.getString(columnName);
		return (name == null) ? null : Credential.Type.valueOf(name);

	}

	@Override
	public Credential.Type getNullableResult(ResultSet rs, int column) throws SQLException {
		String name = rs.getString(column);
		return (name == null) ? null : Credential.Type.valueOf(name);
	}

	@Override
	public Credential.Type getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
		String name = callableStatement.getString(i);
		return (name == null) ? null : Credential.Type.valueOf(name);
	}
	
}
