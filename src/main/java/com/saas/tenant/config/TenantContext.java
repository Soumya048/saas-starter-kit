package com.saas.tenant.config;

public class TenantContext {
    
    private static final ThreadLocal<TenantInfo> currentTenant = new ThreadLocal<>();
    
    public static void setCurrentTenant(String tenantId, Long tenantDbId) {
        currentTenant.set(new TenantInfo(tenantId, tenantDbId));
    }
    
    public static String getCurrentTenantId() {
        TenantInfo info = currentTenant.get();
        return info != null ? info.getTenantId() : null;
    }
    
    public static Long getCurrentTenantDbId() {
        TenantInfo info = currentTenant.get();
        return info != null ? info.getTenantDbId() : null;
    }
    
    public static String getSchemaName() {
        String tenantId = getCurrentTenantId();
        return tenantId != null ? "tenant_" + tenantId.toLowerCase() : "public";
    }
    
    public static void clear() {
        currentTenant.remove();
    }
    
    private static class TenantInfo {
        private final String tenantId;
        private final Long tenantDbId;
        
        TenantInfo(String tenantId, Long tenantDbId) {
            this.tenantId = tenantId;
            this.tenantDbId = tenantDbId;
        }
        
        String getTenantId() {
            return tenantId;
        }
        
        Long getTenantDbId() {
            return tenantDbId;
        }
    }
}
