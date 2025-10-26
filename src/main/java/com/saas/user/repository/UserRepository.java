package com.saas.user.repository;

import com.saas.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByEmailAndDeletedFalse(String email);
    
    boolean existsByEmail(String email);
    
    Optional<User> findByOauthProviderAndOauthId(String provider, String oauthId);
}
