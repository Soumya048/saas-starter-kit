package com.saas.tenant.hibernate;

import com.saas.tenant.config.TenantContext;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver {

    @Override
    public String resolveCurrentTenantIdentifier() {
        String schema = TenantContext.getSchemaName();
        return (schema != null && !schema.isBlank()) ? schema : "public";
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
