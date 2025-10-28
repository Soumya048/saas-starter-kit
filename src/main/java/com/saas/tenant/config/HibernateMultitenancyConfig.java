package com.saas.tenant.config;

import com.saas.tenant.hibernate.SchemaPerTenantConnectionProvider;
import com.saas.tenant.hibernate.TenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class HibernateMultitenancyConfig {

    @Bean
    public SchemaPerTenantConnectionProvider schemaPerTenantConnectionProvider(DataSource dataSource) {
        return new SchemaPerTenantConnectionProvider(dataSource);
    }

    @Bean
    public TenantIdentifierResolver tenantIdentifierResolver() {
        return new TenantIdentifierResolver();
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
            SchemaPerTenantConnectionProvider connectionProvider,
            TenantIdentifierResolver tenantIdentifierResolver) {
        return (properties) -> {
            properties.put("hibernate.multiTenancy", "SCHEMA");
            properties.put("hibernate.tenant_identifier_resolver", tenantIdentifierResolver);
            properties.put("hibernate.multi_tenant_connection_provider", connectionProvider);
        };
    }
}
