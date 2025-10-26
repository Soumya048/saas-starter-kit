package com.saas.subscription.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {
    
    private String planId;
    
    private String paymentMethodId;
}
