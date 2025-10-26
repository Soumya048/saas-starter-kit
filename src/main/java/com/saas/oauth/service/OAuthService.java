package com.saas.oauth.service;

import com.saas.auth.dto.AuthResponse;
import com.saas.auth.service.AuthService;
import com.saas.security.jwt.JwtUtil;
import com.saas.tenant.config.TenantContext;
import com.saas.tenant.entity.Tenant;
import com.saas.tenant.service.TenantService;
import com.saas.user.entity.User;
import com.saas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    
    private final UserRepository userRepository;
    private final TenantService tenantService;
    private final JwtUtil jwtUtil;
    
    @Transactional
    public AuthResponse handleOAuthLogin(OAuth2AuthenticationToken oauthToken, String provider) {
        Map<String, Object> attributes = oauthToken.getPrincipal().getAttributes();
        
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String oauthId = (String) attributes.get("sub"); // Google/GitHub user ID
        
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email not found in OAuth provider response");
        }
        
        // Find existing user by OAuth ID or email
        User user = userRepository.findByOauthProviderAndOauthId(provider, oauthId)
                .orElseGet(() -> userRepository.findByEmail(email).orElse(null));
        
        if (user == null) {
            // Create new user
            user = createUserFromOAuth(email, name, oauthId, provider);
        } else {
            // Update user if OAuth info changed
            if (user.getOauthId() == null || !user.getOauthId().equals(oauthId)) {
                user.setOauthProvider(provider);
                user.setOauthId(oauthId);
                user.setEmailVerified(true);
                user = userRepository.save(user);
            }
        }
        
        // Set tenant context
        tenantService.setTenantContext(user.getTenantId().toString());
        
        // Generate token
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(), 
                user.getId(), 
                user.getTenantId(), 
                user.getRoles()
        );
        
        String refreshToken = jwtUtil.generateRefreshToken(
                user.getEmail(), 
                user.getId(), 
                user.getTenantId()
        );
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }
    
    private User createUserFromOAuth(String email, String name, String oauthId, String provider) {
        // Parse name
        String[] nameParts = name != null ? name.split(" ", 2) : new String[]{"", ""};
        String firstName = nameParts[0];
        String lastName = nameParts.length > 1 ? nameParts[1] : "";
        
        // Get or create tenant
        Tenant tenant = getOrCreateTenant();
        
        // Create user
        User user = User.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .oauthProvider(provider)
                .oauthId(oauthId)
                .emailVerified(true)
                .active(true)
                .tenantId(tenant.getId())
                .build();
        
        user.addRole("USER");
        
        return userRepository.save(user);
    }
    
    private Tenant getOrCreateTenant() {
        // Try to get current tenant from context
        return tenantService.getCurrentTenant()
                .orElseGet(() -> {
                    String tenantId = UUID.randomUUID().toString().substring(0, 8);
                    return tenantService.createTenant(tenantId, "My Organization", null);
                });
    }
    
    public void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken authentication = 
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        null,
                        user.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role))
                                .toList()
                );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
