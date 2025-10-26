package com.saas.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.access-token-validity}")
    private Long accessTokenValidity;
    
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String generateAccessToken(String email, Long userId, Long tenantId, java.util.Set<String> roles) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiryDate = new Date(now + accessTokenValidity * 1000);
        
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("tenantId", tenantId)
                .claim("roles", roles)
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    public String generateRefreshToken(String email, Long userId, Long tenantId) {
        // Refresh tokens typically have longer expiry
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiryDate = new Date(now + 7 * 24 * 60 * 60 * 1000); // 7 days
        
        return Jwts.builder()
                .subject(email)
                .claim("userId", userId)
                .claim("tenantId", tenantId)
                .claim("type", "refresh")
                .issuedAt(issuedAt)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }
    
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }
    
    public Long extractTenantId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("tenantId", Long.class);
    }
    
    public java.util.Set<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        @SuppressWarnings("unchecked")
        java.util.List<String> rolesList = claims.get("roles", java.util.List.class);
        return rolesList != null ? new java.util.HashSet<>(rolesList) : new java.util.HashSet<>();
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
