package com.ericsson.raso.sef.core.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.ericsson.raso.sef.core.db.model.CallingCircleRelation;
import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;

@MappedTypes(CallingCircleRelation.class)
public class CallingCircleRelationHandler extends BaseTypeHandler<CallingCircleRelation> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, CallingCircleRelation status, JdbcType jdbcType) throws SQLException {
        if ((jdbcType == null) || (jdbcType.equals(JdbcType.VARCHAR))) {
            preparedStatement.setString(i, status.name());
        } else {
            throw new UnsupportedOperationException("Unable to convert CallingCircleRelation to " + jdbcType.toString());
        }
    }

    @Override
    public CallingCircleRelation getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        return (name == null) ? null : CallingCircleRelation.valueOf(name);
    }

    @Override
    public CallingCircleRelation getNullableResult(ResultSet rs, int column) throws SQLException {
        String name = rs.getString(column);
        return (name == null) ? null : CallingCircleRelation.valueOf(name);
    }

    @Override
    public CallingCircleRelation getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String name = callableStatement.getString(i);
        return (name == null) ? null : CallingCircleRelation.valueOf(name);
    }

}