package com.saas.subscription.controller;

import com.saas.subscription.dto.SubscriptionRequest;
import com.saas.subscription.service.SubscriptionService;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;
    
    @PostMapping("/create")
    public ResponseEntity<?> createSubscription(
            @RequestBody SubscriptionRequest request,
            Authentication authentication) {
        
        try {
            Long tenantId = getTenantIdFromAuthentication(authentication);
            Subscription subscription = subscriptionService.createSubscription(tenantId, request);
            
            return ResponseEntity.ok(Map.of(
                    "subscriptionId", subscription.getId(),
                    "status", subscription.getStatus()
            ));
        } catch (StripeException e) {
            log.error("Error creating subscription", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
    
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelSubscription(Authentication authentication) {
        try {
            Long tenantId = getTenantIdFromAuthentication(authentication);
            Subscription subscription = subscriptionService.cancelSubscription(tenantId);
            
            return ResponseEntity.ok(Map.of(
                    "subscriptionId", subscription.getId(),
                    "status", subscription.getStatus()
            ));
        } catch (StripeException e) {
            log.error("Error cancelling subscription", e);
            return ResponseEntity.badRequest().body(Map.of(
                    "error", e.getMessage()
            ));
        }
    }
    
    private Long getTenantIdFromAuthentication(Authentication authentication) {
        // Extract tenant ID from authentication
        // This should be set in your JWT token
        return null; // TODO: Implement
    }
}
