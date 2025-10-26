package com.saas.oauth.controller;

import com.saas.auth.dto.AuthResponse;
import com.saas.oauth.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/oauth")
@RequiredArgsConstructor
@Slf4j
public class OAuthController {
    
    private final OAuthService oAuthService;
    
    @GetMapping("/callback")
    public ResponseEntity<AuthResponse> oauthCallback(
            @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken,
            HttpServletResponse response) throws IOException {
        
        log.info("OAuth callback for provider: {}", oauthToken.getAuthorizedClientRegistrationId());
        
        AuthResponse authResponse = oAuthService.handleOAuthLogin(
                oauthToken, 
                oauthToken.getAuthorizedClientRegistrationId()
        );
        
        // Optionally redirect to frontend with token
        // For now, return the response
        return ResponseEntity.ok(authResponse);
    }
    
    @GetMapping("/success")
    public ResponseEntity<AuthResponse> oauthSuccess(
            @AuthenticationPrincipal OAuth2AuthenticationToken oauthToken) {
        
        log.info("OAuth success for provider: {}", oauthToken.getAuthorizedClientRegistrationId());
        
        AuthResponse authResponse = oAuthService.handleOAuthLogin(
                oauthToken, 
                oauthToken.getAuthorizedClientRegistrationId()
        );
        
        return ResponseEntity.ok(authResponse);
    }
}
