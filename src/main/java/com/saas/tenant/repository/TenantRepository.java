package com.saas.tenant.repository;

import com.saas.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    
    Optional<Tenant> findByTenantIdAndActiveTrue(String tenantId);
    
    Optional<Tenant> findByDomain(String domain);
    
    boolean existsByTenantId(String tenantId);
}
