package com.saas.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    
    @NotBlank
    @Size(min = 1, max = 50)
    private String firstName;
    
    @NotBlank
    @Size(min = 1, max = 50)
    private String lastName;
    
    private String tenantId;
    
    @Size(min = 1, max = 100)
    private String tenantName;
}
