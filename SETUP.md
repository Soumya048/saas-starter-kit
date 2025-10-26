# SaaS Starter Kit - Setup Guide

## Quick Start

This guide will help you set up the SaaS Starter Kit for development.

## Prerequisites

1. **Java 21** - [Download](https://adoptium.net/)
2. **PostgreSQL 12+** - [Download](https://www.postgresql.org/download/)
3. **Gradle 8+** (included via wrapper)
4. **IDE** (IntelliJ IDEA, VS Code, or Eclipse)

## Step-by-Step Setup

### 1. Database Setup

Create a PostgreSQL database:

```bash
# Connect to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE saas_starter;

# Exit psql
\q
```

### 2. Environment Configuration

Create an `application-local.yml` file in `src/main/resources/` (already created as template):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/saas_starter
    username: postgres
    password: postgres

logging:
  level:
    root: INFO
    com.saas: DEBUG
```

### 3. JWT Secret Key

Generate a secure JWT secret (32+ characters):

```bash
# Generate a random secret key
openssl rand -hex 32
```

Add it to your environment variables or `application-local.yml`:

```bash
# In .env file or environment
export JWT_SECRET=your-generated-secret-key-here
```

### 4. Build and Run

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Or run with local profile
./gradlew bootRun --args='--spring.profiles.active=local'
```

The application will be available at: `http://localhost:8080`

### 5. Verify Installation

Check if the application is running:

```bash
curl http://localhost:8080/actuator/health
```

Expected response:

```json
{ "status": "UP" }
```

## Testing the API

### 1. Create a Test User

```bash
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123!@#",
    "firstName": "John",
    "lastName": "Doe",
    "tenantName": "Test Company"
  }'
```

Expected response:

```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "type": "Bearer",
  "userId": 1,
  "email": "test@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["USER"]
}
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123!@#"
  }'
```

### 3. Get Current User

```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Configuration Options

### Database Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_database
    username: your_username
    password: your_password
```

### Email Configuration (Optional)

For email verification and notifications:

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
```

### OAuth Configuration (Optional)

#### Google OAuth Setup

1. Go to [Google Cloud Console](https://console.cloud.google.com)
2. Create OAuth 2.0 credentials
3. Add to environment variables:

```bash
GOOGLE_CLIENT_ID=your-client-id
GOOGLE_CLIENT_SECRET=your-client-secret
```

#### GitHub OAuth Setup

1. Go to GitHub Settings > OAuth Apps
2. Create new OAuth App
3. Add to environment variables:

```bash
GITHUB_CLIENT_ID=your-client-id
GITHUB_CLIENT_SECRET=your-client-secret
```

### Stripe Configuration (Optional)

For subscription management:

```bash
STRIPE_API_KEY=sk_test_your_api_key
STRIPE_WEBHOOK_SECRET=whsec_your_webhook_secret
```

Get your keys from: https://dashboard.stripe.com/apikeys

## Multi-Tenant Testing

### Testing with Tenant Context

The application supports tenant isolation via:

1. **Header-based**: `X-Tenant-ID: your-tenant-id`
2. **Subdomain-based**: `your-tenant-id.yourdomain.com`

Example request with tenant header:

```bash
curl -X GET http://localhost:8080/api/v1/users/me \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "X-Tenant-ID: default"
```

## Development Tips

### 1. Database Viewing

View your database:

```bash
psql -U postgres -d saas_starter

# View all tables
\dt

# View users
SELECT * FROM users;

# View tenants
SELECT * FROM tenants;
```

### 2. Debug Mode

Add to `application-local.yml`:

```yaml
logging:
  level:
    org.springframework.web: DEBUG
    org.hibernate.SQL: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
```

### 3. Test Database Reset

Reset the database (WARNING: Deletes all data):

```bash
# Drop and recreate database
psql -U postgres <<EOF
DROP DATABASE IF EXISTS saas_starter;
CREATE DATABASE saas_starter;
EOF

# Run Flyway migrations
./gradlew flywayMigrate
```

## Troubleshooting

### Issue: Port 8080 is already in use

**Solution**: Change the port in `application.yml`:

```yaml
server:
  port: 8081
```

### Issue: Database connection failed

**Solutions**:

1. Check PostgreSQL is running: `pg_isready`
2. Verify database exists: `psql -U postgres -l`
3. Check credentials in `application.yml`

### Issue: JWT token not working

**Solutions**:

1. Ensure `JWT_SECRET` is set and consistent
2. Check token expiration
3. Verify Bearer prefix in Authorization header

### Issue: OAuth not working

**Solutions**:

1. Verify client ID and secret are correct
2. Check redirect URIs match
3. Ensure OAuth provider app is approved

## Next Steps

1. Customize the tenant schemas
2. Add your business logic
3. Configure production environment variables
4. Set up continuous deployment
5. Add monitoring and logging

## Production Deployment

### Recommended Setup

1. **Environment Variables**: Use a secrets manager (AWS Secrets Manager, Vault, etc.)
2. **Database**: Use managed PostgreSQL (AWS RDS, Google Cloud SQL)
3. **Load Balancer**: Configure reverse proxy (Nginx, Traefik)
4. **Monitoring**: Add APM (New Relic, Datadog, etc.)
5. **Logging**: Centralized logging (ELK, Splunk)

### Security Checklist

- [ ] Change default JWT secret
- [ ] Use HTTPS in production
- [ ] Enable database SSL
- [ ] Configure CORS properly
- [ ] Set up rate limiting
- [ ] Enable audit logging
- [ ] Use environment variables for secrets
- [ ] Implement backup strategy

## Support

For issues or questions:

- Open an issue on GitHub
- Check existing documentation
- Review logs for error messages

---

Happy coding! 🚀
