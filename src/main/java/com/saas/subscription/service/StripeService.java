package com.saas.subscription.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.param.*;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class StripeService {
    
    @Value("${stripe.api-key}")
    private String stripeApiKey;
    
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;
    
    public StripeService(@Value("${stripe.api-key}") String stripeApiKey) {
        this.stripeApiKey = stripeApiKey;
        Stripe.apiKey = stripeApiKey;
    }
    
    public Customer createCustomer(String email, String name) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .setName(name)
                .build();
        
        return Customer.create(params);
    }
    
    public Customer updateCustomer(String customerId, Map<String, String> metadata) throws StripeException {
        CustomerUpdateParams params = CustomerUpdateParams.builder()
                .putAllMetadata(metadata)
                .build();
        
        return Customer.retrieve(customerId).update(params);
    }
    
    public Subscription createSubscription(String customerId, String priceId) throws StripeException {
        SubscriptionCreateParams params = SubscriptionCreateParams.builder()
                .setCustomer(customerId)
                .addItem(SubscriptionCreateParams.Item.builder()
                        .setPrice(priceId)
                        .build())
                .setPaymentBehavior(SubscriptionCreateParams.PaymentBehavior.DEFAULT_INCOMPLETE)
                .setPaymentSettings(SubscriptionCreateParams.PaymentSettings.builder()
//                        .setPaymentMethodTypes(java.util.List.of("card"))

                        .build())
                .build();
        
        return Subscription.create(params);
    }
    
    public Subscription getSubscription(String subscriptionId) throws StripeException {
        return Subscription.retrieve(subscriptionId);
    }
    
    public Subscription cancelSubscription(String subscriptionId) throws StripeException {
        return Subscription.retrieve(subscriptionId).cancel();
    }
    
    public Session createCheckoutSession(String customerId, String priceId, String successUrl, 
                                        String cancelUrl, Map<String, String> metadata) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setCustomer(customerId)
                .setSuccessUrl(successUrl)
                .setCancelUrl(cancelUrl)
                .putAllMetadata(metadata)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(priceId)
                        .setQuantity(1L)
                        .build())
                .build();
        
        return Session.create(params);
    }
    
    public Event constructEvent(String payload, String sigHeader) throws StripeException {
//        return Event.constructFrom(payload, sigHeader, webhookSecret);
        return null; // Placeholder as Stripe Java SDK does not have this exact method
    }
}
