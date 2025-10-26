package com.saas.auth.service;

import com.saas.auth.dto.AuthResponse;
import com.saas.auth.dto.LoginRequest;
import com.saas.auth.dto.RefreshTokenRequest;
import com.saas.auth.dto.SignupRequest;
import com.saas.security.jwt.JwtUtil;
import com.saas.tenant.config.TenantContext;
import com.saas.tenant.entity.Tenant;
import com.saas.tenant.service.TenantService;
import com.saas.user.entity.RefreshToken;
import com.saas.user.entity.User;
import com.saas.user.repository.RefreshTokenRepository;
import com.saas.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TenantService tenantService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Create or get tenant
        Tenant tenant = createOrGetTenant(request);
        
        // Create user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .tenantId(tenant.getId())
                .active(true)
                .emailVerified(false)
                .build();
        
        user.addRole("USER");
        
        user = userRepository.save(user);
        
        return generateAuthResponse(user);
    }
    
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmailAndDeletedFalse(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        if (!user.getActive()) {
            throw new RuntimeException("User account is not active");
        }
        
        // Set tenant context
        tenantService.setTenantContext(user.getTenantId().toString());
        
        return generateAuthResponse(user);
    }
    
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        
        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired");
        }
        
        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.getActive()) {
            throw new RuntimeException("User account is not active");
        }
        
        // Set tenant context
        tenantService.setTenantContext(user.getTenantId().toString());
        
        // Generate new tokens
        return generateAuthResponse(user);
    }
    
    @Transactional
    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    token.setRevoked(true);
                    token.setRevokedAt(LocalDateTime.now());
                    refreshTokenRepository.save(token);
                });
    }
    
    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtUtil.generateAccessToken(
                user.getEmail(), 
                user.getId(), 
                user.getTenantId(), 
                user.getRoles()
        );
        
        String refreshTokenStr = jwtUtil.generateRefreshToken(
                user.getEmail(), 
                user.getId(), 
                user.getTenantId()
        );
        
        // Save or update refresh token
        saveRefreshToken(refreshTokenStr, user);
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }
    
    private void saveRefreshToken(String token, User user) {
        // Delete old refresh tokens for this user
        refreshTokenRepository.deleteByUserId(user.getId());
        
        // Create new refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .userId(user.getId())
                .tenantId(user.getTenantId())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .build();
        
        refreshTokenRepository.save(refreshToken);
    }
    
    private Tenant createOrGetTenant(SignupRequest request) {
        if (request.getTenantId() != null && !request.getTenantId().isBlank()) {
            return tenantService.getCurrentTenant()
                    .orElseGet(() -> tenantService.createTenant(
                            request.getTenantId(), 
                            request.getTenantName(), 
                            null
                    ));
        }
        
        // Create a new tenant for the user
        String tenantId = UUID.randomUUID().toString().substring(0, 8);
        return tenantService.createTenant(tenantId, request.getTenantName(), null);
    }
}
