package com.ericsson.raso.sef.core.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.joda.time.DateTime;

@MappedTypes(DateTime.class)
public class DateTimeTypeHandler extends BaseTypeHandler<DateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, DateTime dateTime, JdbcType jdbcType) throws SQLException {
        if ( (jdbcType == null) || (jdbcType.equals(JdbcType.DATE))|| (jdbcType.equals(JdbcType.TIMESTAMP)) ) {
            preparedStatement.setTimestamp(i, new Timestamp(dateTime.getMillis()));
        } else {
            throw new UnsupportedOperationException("Unable to convert DateTime to " + jdbcType.toString());
        }
    }

    @Override
    public DateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp date = rs.getTimestamp(columnName);
        return (date == null) ? null : new DateTime(date.getTime());
    }

    @Override
    public DateTime getNullableResult(ResultSet rs, int i) throws SQLException {
    	Timestamp date = rs.getTimestamp(i);
        return (date == null) ? null : new DateTime(date.getTime());
    }

    @Override
    public DateTime getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
    	Timestamp date = callableStatement.getTimestamp(i);
        return (date == null) ? null : new DateTime(date.getTime());
    }

}