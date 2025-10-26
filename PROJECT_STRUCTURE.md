# Project Structure

## Overview

This document outlines the complete structure of the SaaS Starter Kit and explains the purpose of each component.

## Directory Structure

```
saas-starter-kit/
├── src/
│   ├── main/
│   │   ├── java/com/saas/
│   │   │   ├── SaasStarterKitApplication.java   # Main application entry point
│   │   │   │
│   │   │   ├── auth/                             # Authentication module
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthController.java       # Login, signup, logout endpoints
│   │   │   │   ├── dto/
│   │   │   │   │   ├── AuthResponse.java         # Authentication response DTO
│   │   │   │   │   ├── LoginRequest.java         # Login request DTO
│   │   │   │   │   ├── SignupRequest.java       # Signup request DTO
│   │   │   │   │   └── RefreshTokenRequest.java  # Refresh token request DTO
│   │   │   │   └── service/
│   │   │   │       └── AuthService.java          # Authentication business logic
│   │   │   │
│   │   │   ├── oauth/                             # OAuth integration module
│   │   │   │   ├── controller/
│   │   │   │   │   └── OAuthController.java      # OAuth callback endpoints
│   │   │   │   └── service/
│   │   │   │       └── OAuthService.java         # OAuth business logic
│   │   │   │
│   │   │   ├── security/                           # Security configuration
│   │   │   │   ├── config/
│   │   │   │   │   └── SecurityConfig.java        # Spring Security configuration
│   │   │   │   ├── filter/
│   │   │   │   │   └── JwtAuthenticationFilter.java  # JWT token filter
│   │   │   │   ├── jwt/
│   │   │   │   │   └── JwtUtil.java              # JWT utility class
│   │   │   │   └── service/
│   │   │   │       └── CustomUserDetailsService.java  # User details service
│   │   │   │
│   │   │   ├── tenant/                             # Multi-tenant module
│   │   │   │   ├── config/
│   │   │   │   │   └── TenantContext.java         # Thread-local tenant context
│   │   │   │   ├── entity/
│   │   │   │   │   └── Tenant.java                # Tenant entity
│   │   │   │   ├── filter/
│   │   │   │   │   └── TenantFilter.java          # Tenant identification filter
│   │   │   │   ├── repository/
│   │   │   │   │   └── TenantRepository.java      # Tenant data access
│   │   │   │   └── service/
│   │   │   │       └── TenantService.java         # Tenant business logic
│   │   │   │
│   │   │   ├── user/                               # User management module
│   │   │   │   ├── controller/
│   │   │   │   │   └── UserController.java        # User endpoints
│   │   │   │   ├── entity/
│   │   │   │   │   ├── User.java                  # User entity
│   │   │   │   │   ├── Role.java                  # Role enum
│   │   │   │   │   └── RefreshToken.java          # Refresh token entity
│   │   │   │   └── repository/
│   │   │   │       ├── UserRepository.java        # User data access
│   │   │   │       └── RefreshTokenRepository.java  # Refresh token data access
│   │   │   │
│   │   │   ├── subscription/                       # Subscription management
│   │   │   │   ├── controller/
│   │   │   │   │   ├── SubscriptionController.java     # Subscription endpoints
│   │   │   │   │   └── StripeWebhookController.java    # Stripe webhooks
│   │   │   │   ├── dto/
│   │   │   │   │   └── SubscriptionRequest.java   # Subscription request DTO
│   │   │   │   └── service/
│   │   │   │       ├── StripeService.java         # Stripe API wrapper
│   │   │   │       └── SubscriptionService.java  # Subscription business logic
│   │   │   │
│   │   │   └── common/                              # Common utilities
│   │   │       └── exception/
│   │   │           └── GlobalExceptionHandler.java  # Global exception handler
│   │   │
│   │   └── resources/
│   │       ├── db/migration/
│   │       │   └── V1__create_initial_schema.sql  # Database migrations
│   │       ├── application.yml                      # Application configuration
│   │       ├── application-local.yml                # Local environment config
│   │       └── logback-spring.xml                   # Logging configuration
│   │
│   └── test/                                         # Test directory
│       └── java/com/saas/                            # Test files
│
├── build.gradle                                      # Gradle build configuration
├── settings.gradle                                   # Gradle settings
├── gradle.properties                                # Gradle properties
├── gradlew                                           # Gradle wrapper script (Unix)
├── gradlew.bat                                       # Gradle wrapper script (Windows)
├── .gitignore                                        # Git ignore rules
├── README.md                                         # Main documentation
├── SETUP.md                                          # Setup guide
└── PROJECT_STRUCTURE.md                             # This file
```

## Component Descriptions

### Core Application

**SaasStarterKitApplication.java**

- Spring Boot main application class
- Entry point for the application

### Authentication Module (`auth/`)

Handles user authentication with JWT tokens:

- **AuthController**: REST endpoints for signup, login, logout
- **AuthService**: Business logic for authentication flows
- **DTOs**: Request/Response objects for API communication

### OAuth Module (`oauth/`)

Handles OAuth2 authentication with third-party providers:

- **OAuthController**: OAuth callback endpoints
- **OAuthService**: OAuth business logic
- Supports Google and GitHub

### Security Module (`security/`)

Spring Security configuration and JWT handling:

- **SecurityConfig**: Security filter chain and configuration
- **JwtAuthenticationFilter**: JWT token validation on each request
- **JwtUtil**: JWT token generation and parsing
- **CustomUserDetailsService**: User details for Spring Security

### Multi-Tenant Module (`tenant/`)

Implements schema-per-tenant architecture:

- **TenantContext**: Thread-local storage for current tenant
- **TenantFilter**: Intercepts requests to identify tenant
- **TenantService**: Tenant management logic
- **TenantRepository**: Database access for tenants

### User Module (`user/`)

User management and profiles:

- **User**: Main user entity with roles and tenant association
- **Role**: Role enumeration (SUPER_ADMIN, ADMIN, USER)
- **RefreshToken**: Refresh token storage
- **UserController**: User profile endpoints
- **Repositories**: Data access for users and refresh tokens

### Subscription Module (`subscription/`)

Stripe integration for subscription management:

- **SubscriptionController**: Subscription CRUD endpoints
- **StripeWebhookController**: Stripe webhook handler
- **StripeService**: Stripe API wrapper
- **SubscriptionService**: Subscription business logic

### Common Module (`common/`)

Shared utilities and configurations:

- **GlobalExceptionHandler**: Centralized error handling

## Key Design Patterns

### 1. Multi-Tenant Isolation

Uses **schema-per-tenant** approach where each tenant has its own schema. The `TenantFilter` identifies the tenant from request headers and sets it in `TenantContext` which is used throughout the request lifecycle.

### 2. JWT Authentication

Stateless authentication using JWT tokens:

- Access tokens for API access (short-lived)
- Refresh tokens for token renewal (long-lived)
- Tokens stored in HTTP-only cookies (client-side)

### 3. Role-Based Access Control (RBAC)

Three-tier role system:

- **SUPER_ADMIN**: Access to all tenants
- **ADMIN**: Administrator within tenant
- **USER**: Regular user

### 4. Repository Pattern

Data access layer uses Spring Data JPA repositories for clean separation of concerns.

### 5. DTO Pattern

Separate DTOs for API communication to avoid exposing internal entity structure.

## Database Schema

### Tenants Table

- Stores tenant information
- Includes Stripe customer ID for billing
- Tracks subscription status

### Users Table

- User accounts and profiles
- OAuth provider information
- Linked to tenants

### User Roles Table

- Many-to-many relationship for roles
- Supports multiple roles per user

### Refresh Tokens Table

- Stores refresh tokens
- Used for token renewal
- Supports token revocation

## API Endpoints

### Authentication

- `POST /api/v1/auth/signup` - User registration
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/logout` - User logout

### Users

- `GET /api/v1/users/me` - Get current user
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/me` - Update current user

### OAuth

- `GET /api/v1/oauth/callback` - OAuth callback
- `GET /api/v1/oauth/success` - OAuth success handler

### Subscriptions

- `POST /api/v1/subscriptions/create` - Create subscription
- `POST /api/v1/subscriptions/cancel` - Cancel subscription

### Webhooks

- `POST /api/v1/webhooks/stripe` - Stripe webhook handler

## Security Flow

1. **Request arrives** → TenantFilter identifies tenant
2. **Tenant context set** in ThreadLocal
3. **JwtAuthenticationFilter** validates JWT token
4. **Security context populated** with user details
5. **Request processed** with tenant-specific data
6. **Context cleared** after response

## Configuration Files

### application.yml

Main configuration file with:

- Database connection
- Security settings
- JWT configuration
- OAuth client configuration
- Stripe API keys

### application-local.yml

Local development overrides

### logback-spring.xml

Logging configuration with profiles

## Migration Strategy

Uses Flyway for database versioning:

- `V1__create_initial_schema.sql`: Creates all tables
- Future migrations: `V2__*.sql`, `V3__*.sql`, etc.

## Extension Points

To extend this template:

1. **Add new entities**: Create in appropriate module
2. **Add new endpoints**: Create controllers in modules
3. **Add new services**: Implement business logic
4. **Add migrations**: Create new Flyway migration files
5. **Add roles**: Extend Role enum
6. **Add OAuth providers**: Configure in application.yml

## Best Practices Implemented

1. ✅ Separation of concerns
2. ✅ Single Responsibility Principle
3. ✅ Dependency Injection
4. ✅ Exception handling
5. ✅ Logging
6. ✅ Validation
7. ✅ Security best practices
8. ✅ RESTful API design
9. ✅ Database migrations
10. ✅ Configuration management

---

This structure provides a solid foundation for building multi-tenant SaaS applications!
