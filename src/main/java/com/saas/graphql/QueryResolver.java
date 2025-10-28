package com.saas.graphql;

import com.saas.tenant.entity.Tenant;
import com.saas.tenant.service.TenantService;
import com.saas.user.entity.User;
import com.saas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class QueryResolver {

    private final UserRepository userRepository;
    private final TenantService tenantService;

    @QueryMapping
    public User me(Authentication authentication) {
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @QueryMapping
    public Tenant tenant() {
        return tenantService.getCurrentTenant()
                .orElseThrow(() -> new RuntimeException("Tenant not found in context"));
    }
}
