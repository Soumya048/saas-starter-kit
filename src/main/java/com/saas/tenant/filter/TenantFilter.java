package com.saas.tenant.filter;

import com.saas.tenant.config.TenantContext;
import com.saas.tenant.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class TenantFilter extends OncePerRequestFilter {
    
    private final TenantService tenantService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            String tenantId = extractTenantId(request);
            
            if (tenantId != null) {
                tenantService.setTenantContext(tenantId);
            } else {
                // Allow public endpoints (auth endpoints) without tenant
                String path = request.getRequestURI();
                if (!path.startsWith("/api/v1/auth/") && !path.startsWith("/actuator/")) {
                    log.warn("Tenant not found for path: {}", path);
                }
            }
            
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
    
    private String extractTenantId(HttpServletRequest request) {
        // Try to get tenant from header first
        String tenantId = request.getHeader("X-Tenant-ID");
        
        if (tenantId == null || tenantId.isBlank()) {
            // Try to extract from subdomain
            String host = request.getHeader("Host");
            if (host != null && host.contains(".")) {
                String subdomain = host.split("\\.")[0];
                if (!subdomain.equals("www") && !subdomain.equals("localhost")) {
                    tenantId = subdomain;
                }
            }
        }
        
        return tenantId != null ? tenantId.trim().toLowerCase() : null;
    }
}
