package com.saas.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String accessToken;
    
    private String refreshToken;
    
    private String type = "Bearer";
    
    private Long userId;
    
    private String email;
    
    private String firstName;
    
    private String lastName;
    
    private java.util.Set<String> roles;
}
