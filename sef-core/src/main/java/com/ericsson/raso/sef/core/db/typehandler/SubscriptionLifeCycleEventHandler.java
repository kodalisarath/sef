package com.ericsson.raso.sef.core.db.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import com.ericsson.raso.sef.core.db.model.SubscriptionLifeCycleEvent;

@MappedTypes(SubscriptionLifeCycleEvent.class)
public class SubscriptionLifeCycleEventHandler extends BaseTypeHandler<SubscriptionLifeCycleEvent> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, SubscriptionLifeCycleEvent status, JdbcType jdbcType) throws SQLException {
        if ((jdbcType == null) || (jdbcType.equals(JdbcType.VARCHAR))) {
            preparedStatement.setString(i, status.name());
        } else {
            throw new UnsupportedOperationException("Unable to convert Subscriber Status to " + jdbcType.toString());
        }
    }

    @Override
    public SubscriptionLifeCycleEvent getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String name = rs.getString(columnName);
        return (name == null) ? null : SubscriptionLifeCycleEvent.valueOf(name);
    }

    @Override
    public SubscriptionLifeCycleEvent getNullableResult(ResultSet rs, int column) throws SQLException {
        String name = rs.getString(column);
        return (name == null) ? null : SubscriptionLifeCycleEvent.valueOf(name);
    }

    @Override
    public SubscriptionLifeCycleEvent getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String name = callableStatement.getString(i);
        return (name == null) ? null : SubscriptionLifeCycleEvent.valueOf(name);
    }

}