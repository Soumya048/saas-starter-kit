# SaaS Starter Kit

A production-ready, multi-tenant Spring Boot starter kit for building SaaS applications. This template provides everything you need to quickly launch your own software-as-a-service product.

## Features

- ✅ **Multi-tenant Architecture** - Schema-per-tenant approach for data isolation
- ✅ **JWT Authentication** - Secure access tokens with refresh token support
- ✅ **Role-Based Access Control** - Admin, User, and Super Admin roles
- ✅ **OAuth Integration** - Google and GitHub authentication
- ✅ **Stripe Integration** - Subscription management and payment processing
- ✅ **RESTful APIs** - Production-ready API endpoints
- ✅ **PostgreSQL Database** - Robust relational database
- ✅ **Flyway Migrations** - Database version control
- ✅ **Security Best Practices** - Spring Security with JWT

## Tech Stack

- **Java 21**
- **Spring Boot 3.2.0**
- **PostgreSQL**
- **Spring Security** (JWT Authentication)
- **Stripe** (Payment Processing)
- **OAuth2** (Google, GitHub)
- **Flyway** (Database Migrations)
- **Gradle** (Build Tool)
- **Lombok** (Boilerplate Reduction)

## Prerequisites

- Java 21 or higher
- PostgreSQL 12+
- Gradle 8+
- Stripe Account (for subscription management)
- Google/GitHub OAuth Apps (optional, for OAuth login)

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/saas-starter-kit.git
cd saas-starter-kit
```

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE saas_starter;
```

Update the database configuration in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/saas_starter
    username: your_username
    password: your_password
```

### 3. Environment Variables

Create a `.env` file or set environment variables:

```bash
# JWT Configuration
JWT_SECRET=your-super-secure-secret-key-at-least-256-bits

# Database
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Mail Configuration
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Stripe
STRIPE_API_KEY=sk_test_your_stripe_api_key
STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret

# OAuth (Optional)
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
GITHUB_CLIENT_ID=your-github-client-id
GITHUB_CLIENT_SECRET=your-github-client-secret

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:3000,http://localhost:8080
```

### 4. Build the Project

```bash
./gradlew build
```

### 5. Run the Application

```bash
./gradlew bootRun
```

Or for local development:

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

#### Sign Up

```http
POST /api/v1/auth/signup
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe",
  "tenantId": "mycompany",
  "tenantName": "My Company"
}
```

#### Login

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123!"
}
```

#### Refresh Token

```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "your-refresh-token"
}
```

### User Endpoints

#### Get Current User

```http
GET /api/v1/users/me
Authorization: Bearer <access-token>
```

#### Update Current User

```http
PUT /api/v1/users/me
Authorization: Bearer <access-token>
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "phoneNumber": "+1234567890"
}
```

### Subscription Endpoints

#### Create Subscription

```http
POST /api/v1/subscriptions/create
Authorization: Bearer <access-token>
Content-Type: application/json

{
  "planId": "price_xxx",
  "paymentMethodId": "pm_xxx"
}
```

## Project Structure

```
saas-starter-kit/
├── src/
│   ├── main/
│   │   ├── java/com/saas/
│   │   │   ├── auth/           # Authentication logic
│   │   │   ├── oauth/           # OAuth integration
│   │   │   ├── security/        # Security configuration
│   │   │   ├── tenant/          # Multi-tenant logic
│   │   │   ├── user/            # User management
│   │   │   ├── subscription/    # Subscription management
│   │   │   └── common/          # Common utilities
│   │   └── resources/
│   │       ├── db/migration/    # Flyway migrations
│   │       └── application.yml  # Application configuration
│   └── test/                    # Test files
├── build.gradle                 # Gradle build configuration
├── settings.gradle
└── README.md
```

## Multi-Tenancy

The application uses a **schema-per-tenant** approach where each tenant gets its own schema in the same database. This provides:

- Data isolation between tenants
- Easy tenant-specific customizations
- Better performance than shared tables with tenant_id filters

### How It Works

1. Tenant is identified via `X-Tenant-ID` header or subdomain
2. Tenant context is stored in `ThreadLocal` for the request lifecycle
3. Database queries are automatically scoped to the current tenant's schema

## Security Features

- **JWT Authentication** - Stateless authentication with access and refresh tokens
- **Password Hashing** - BCrypt with strength 12
- **CORS Configuration** - Configurable CORS policies
- **Role-Based Authorization** - Granular permission system
- **OAuth2 Integration** - Social login support

## Subscription Management

The kit includes complete Stripe integration for handling:

- Subscription creation
- Payment processing
- Webhook handling
- Subscription cancellation

### Setting Up Stripe

1. Create a Stripe account at [stripe.com](https://stripe.com)
2. Get your API keys from the Dashboard
3. Configure webhook endpoints for subscription events
4. Add your webhook secret to environment variables

## OAuth Configuration

### Google OAuth

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create a new OAuth 2.0 Client ID
3. Add authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
4. Add the client ID and secret to your environment variables

### GitHub OAuth

1. Go to GitHub Settings > Developer settings > OAuth Apps
2. Create a new OAuth app
3. Add authorization callback URL: `http://localhost:8080/login/oauth2/code/github`
4. Add the client ID and secret to your environment variables

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add some amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or contributions, please open an issue on GitHub.

## Roadmap

- [ ] GraphQL API endpoints
- [ ] Admin panel integration
- [ ] Email verification
- [ ] Two-factor authentication
- [ ] More OAuth providers (Microsoft, LinkedIn)
- [ ] Razorpay integration (alternative to Stripe)
- [ ] API rate limiting
- [ ] Audit logging

---

**Built with ❤️ for entrepreneurs and developers building the next big SaaS.**
