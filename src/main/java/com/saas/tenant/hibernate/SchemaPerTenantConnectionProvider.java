package com.saas.tenant.hibernate;

import com.saas.tenant.config.TenantContext;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.spi.Stoppable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SchemaPerTenantConnectionProvider implements MultiTenantConnectionProvider, Stoppable {

    private final DataSource dataSource;

    public SchemaPerTenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        final Connection connection = getAnyConnection();
        setSchema(connection, tenantIdentifier);
        return connection;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
        try {
            setSchema(connection, "public");
        } finally {
            connection.close();
        }
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        return null;
    }

    @Override
    public void stop() {
        // no-op
    }

    private void setSchema(Connection connection, Object tenantIdentifier) throws SQLException {
        String schema = tenantIdentifier != null ? String.valueOf(tenantIdentifier) : TenantContext.getSchemaName();
        if (schema == null || schema.isBlank()) {
            schema = "public";
        }
        try {
            connection.setSchema(schema);
        } catch (SQLException e) {
            connection.createStatement().execute("SET search_path TO " + schema);
        }
    }
}
