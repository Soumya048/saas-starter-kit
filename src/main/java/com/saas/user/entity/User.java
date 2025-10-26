package com.saas.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    private String password; // hashed
    
    private String firstName;
    
    private String lastName;
    
    private String phoneNumber;
    
    private String avatarUrl;
    
    private String oauthProvider; // google, github, etc.
    
    private String oauthId;
    
    @Builder.Default
    private Boolean emailVerified = false;
    
    @Builder.Default
    private Boolean active = true;
    
    @Column(nullable = false)
    private Long tenantId;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Builder.Default
    private Set<String> roles = new HashSet<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private Boolean deleted = false;
    
    // Helper methods
    public void addRole(String role) {
        this.roles.add(role);
    }
    
    public void removeRole(String role) {
        this.roles.remove(role);
    }
    
    public boolean hasRole(String role) {
        return this.roles.contains(role);
    }
    
    public boolean isSuperAdmin() {
        return hasRole("SUPER_ADMIN");
    }
    
    public boolean isAdmin() {
        return hasRole("ADMIN") || isSuperAdmin();
    }
}
