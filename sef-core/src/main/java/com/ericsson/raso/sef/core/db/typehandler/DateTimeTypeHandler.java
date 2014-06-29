package com.ericsson.raso.sef.core.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;


@MappedTypes(Date.class)
public class DateTimeTypeHandler extends BaseTypeHandler<Date> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Date dateTime, JdbcType jdbcType) throws SQLException {
        if ( (jdbcType == null) || (jdbcType.equals(JdbcType.DATE))|| (jdbcType.equals(JdbcType.TIMESTAMP)) ) {
            preparedStatement.setTimestamp(i, new Timestamp(dateTime.getTime()));
        } else {
            throw new UnsupportedOperationException("Unable to convert DateTime to " + jdbcType.toString());
        }
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp date = rs.getTimestamp(columnName);
        return (date == null) ? null : new Date(date.getTime());
    }

    @Override
    public Date getNullableResult(ResultSet rs, int i) throws SQLException {
    	Timestamp date = rs.getTimestamp(i);
        return (date == null) ? null : new Date(date.getTime());
    }

    @Override
    public Date getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
    	Timestamp date = callableStatement.getTimestamp(i);
        return (date == null) ? null : new Date(date.getTime());
    }

}