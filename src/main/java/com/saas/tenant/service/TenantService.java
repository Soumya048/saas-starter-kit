package com.saas.tenant.service;

import com.saas.tenant.config.TenantContext;
import com.saas.tenant.entity.Tenant;
import com.saas.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantService {
    
    private final TenantRepository tenantRepository;
    
    @Transactional(readOnly = true)
    public void setTenantContext(String tenantId) {
        Optional<Tenant> tenant = tenantRepository.findByTenantIdAndActiveTrue(tenantId);
        
        if (tenant.isPresent()) {
            TenantContext.setCurrentTenant(tenantId, tenant.get().getId());
        } else {
            log.warn("Tenant not found or inactive: {}", tenantId);
        }
    }
    
    @Transactional(readOnly = true)
    public Optional<Tenant> getCurrentTenant() {
        Long tenantDbId = TenantContext.getCurrentTenantDbId();
        if (tenantDbId != null) {
            return tenantRepository.findById(tenantDbId);
        }
        return Optional.empty();
    }
    
    @Transactional
    public Tenant createTenant(String tenantId, String name, String domain) {
        String schemaName = "tenant_" + tenantId.toLowerCase();
        
        Tenant tenant = Tenant.builder()
                .tenantId(tenantId)
                .name(name)
                .domain(domain)
                .schemaName(schemaName)
                .active(true)
                .subscriptionStatus("active")
                .build();
        
        return tenantRepository.save(tenant);
    }
}
