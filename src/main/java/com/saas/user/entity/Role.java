package com.saas.user.entity;

public enum Role {
    SUPER_ADMIN("SUPER_ADMIN", "Super Administrator with access to all tenants"),
    ADMIN("ADMIN", "Administrator within a tenant"),
    USER("USER", "Regular user");
    
    private final String value;
    private final String description;
    
    Role(String value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
