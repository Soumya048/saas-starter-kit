package com.saas.subscription.service;

import com.saas.subscription.dto.SubscriptionRequest;
import com.saas.tenant.config.TenantContext;
import com.saas.tenant.entity.Tenant;
import com.saas.tenant.repository.TenantRepository;
import com.saas.user.entity.User;
import com.saas.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {
    
    private final StripeService stripeService;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public Subscription createSubscription(Long tenantId, SubscriptionRequest request) throws StripeException {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        // Create or get Stripe customer
        Customer customer;
        if (tenant.getStripeCustomerId() == null) {
            customer = stripeService.createCustomer(
                    "tenant_" + tenant.getTenantId() + "@example.com",
                    tenant.getName()
            );
            tenant.setStripeCustomerId(customer.getId());
            tenantRepository.save(tenant);
        } else {
            customer = Customer.retrieve(tenant.getStripeCustomerId());
        }
        
        // Create subscription
        Subscription subscription = stripeService.createSubscription(
                customer.getId(), 
                request.getPlanId()
        );
        
        // Update tenant with subscription info
        tenant.setSubscriptionStatus("pending");
        tenant.setSubscriptionPlan(request.getPlanId());
        tenantRepository.save(tenant);
        
        return subscription;
    }
    
    @Transactional
    public Subscription cancelSubscription(Long tenantId) throws StripeException {
        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));
        
        if (tenant.getStripeCustomerId() == null) {
            throw new RuntimeException("Tenant has no active Stripe subscription");
        }
        
        // Get active subscription
        Customer customer = Customer.retrieve(tenant.getStripeCustomerId());
        Subscription subscription = customer.getSubscriptions().getData().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active subscription found"));
        
        // Cancel subscription
        subscription = stripeService.cancelSubscription(subscription.getId());
        
        tenant.setSubscriptionStatus("cancelled");
        tenantRepository.save(tenant);
        
        return subscription;
    }
    
    public void handleStripeWebhook(String payload, String signature) throws StripeException {
        var event = stripeService.constructEvent(payload, signature);
        
        log.info("Received Stripe webhook event: {}", event.getType());
        
        switch (event.getType()) {
            case "customer.subscription.created":
            case "customer.subscription.updated":
                handleSubscriptionUpdate(event.getDataObjectDeserializer().getRawJson());
                break;
            case "customer.subscription.deleted":
                handleSubscriptionDeleted(event.getDataObjectDeserializer().getRawJson());
                break;
            default:
                log.debug("Unhandled event type: {}", event.getType());
        }
    }
    
    private void handleSubscriptionUpdate(String json) {
        // Parse and update tenant subscription
        // Implementation depends on your specific needs
        log.info("Handling subscription update");
    }
    
    private void handleSubscriptionDeleted(String json) {
        // Handle subscription cancellation
        log.info("Handling subscription deletion");
    }
}
