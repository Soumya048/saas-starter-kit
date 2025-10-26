# Swagger/OpenAPI Integration

This document explains the Swagger integration in the SaaS Starter Kit.

## What is Swagger?

Swagger (now part of OpenAPI) provides interactive API documentation that allows developers to:

- View all available endpoints
- Understand request/response structures
- Test APIs directly from the browser
- Share API documentation with frontend teams

## Integration Details

### Dependencies

The project uses **SpringDoc OpenAPI** (the modern successor to Springfox):

```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0'
```

### Configuration

#### 1. Security Configuration

Swagger endpoints are made accessible in `SecurityConfig.java`:

```java
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
```

This allows public access to Swagger UI without authentication.

#### 2. Application Configuration

Swagger is configured in `application.yml`:

```yaml
springdoc:
  api-docs:
    path: /api-docs
    enabled: true
  swagger-ui:
    enabled: true
    path: /swagger-ui.html
    operations-sorter: method
    tags-sorter: alpha
    try-it-out-enabled: true
```

#### 3. Swagger Configuration Class

The `SwaggerConfig.java` file configures:

- **API Info**: Title, description, version, contact details
- **Security Scheme**: JWT Bearer token authentication
- **Global Security**: All endpoints require Bearer token by default

```java
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("SaaS Starter Kit API")
            .version("1.0.0")
            .description("Production-ready multi-tenant API"))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth", new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
}
```

## Using Swagger

### Access Points

Once the application is running, Swagger is available at:

1. **Swagger UI**: http://localhost:8080/swagger-ui.html
2. **OpenAPI JSON**: http://localhost:8080/v3/api-docs
3. **OpenAPI YAML**: http://localhost:8080/v3/api-docs.yaml

### Testing Endpoints

#### Step 1: Get an Access Token

1. Open Swagger UI
2. Navigate to **Authentication** section
3. Find `/api/v1/auth/signup` endpoint
4. Click "Try it out"
5. Enter your details:
   ```json
   {
     "email": "test@example.com",
     "password": "Test123!@#",
     "firstName": "John",
     "lastName": "Doe",
     "tenantName": "My Company"
   }
   ```
6. Click "Execute"
7. Copy the `accessToken` from the response

#### Step 2: Authenticate in Swagger

1. Click the "Authorize" button (🔓 icon) at the top right
2. Enter your token: `Bearer <your-access-token>`
3. Click "Authorize"
4. Close the dialog

#### Step 3: Test Protected Endpoints

Now you can:

- Test any authenticated endpoint
- See the request/response structures
- Validate your API calls

### Annotations Used

We use OpenAPI annotations to document endpoints:

#### Controller Level

```java
@Tag(name = "Authentication", description = "Authentication endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {
```

#### Method Level

```java
@Operation(
    summary = "User registration",
    description = "Register a new user account"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "400", description = "Bad request")
})
@PostMapping("/signup")
public ResponseEntity<AuthResponse> signup(...) {
```

#### Parameter Level

```java
@Parameter(description = "User ID")
@PathVariable Long id
```

## Features

### 1. Interactive Testing

- Try out all endpoints directly from the browser
- See real request/response examples
- Validate your API without Postman or cURL

### 2. Authentication Support

- Built-in JWT Bearer token support
- "Authorize" button for easy token management
- Secure token storage in browser session

### 3. Request/Response Models

- Complete data models for all DTOs
- Validation rules displayed
- Example values provided

### 4. Code Generation

You can use the OpenAPI spec to:

- Generate client SDKs
- Import into Postman
- Create API documentation websites

## Customization

### Adding More Annotations

To enhance Swagger documentation:

```java
@Operation(
    summary = "Brief summary",
    description = "Detailed description",
    tags = {"Optional", "Tags"}
)
@Parameter(
    name = "id",
    description = "Parameter description",
    required = true,
    schema = @Schema(type = "integer", format = "int64")
)
```

### Schema Examples

```java
@Schema(description = "User authentication response")
public class AuthResponse {
    @Schema(description = "JWT access token", example = "eyJhbGciOi...")
    private String accessToken;
}
```

### Response Examples

```java
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Success",
        content = @Content(schema = @Schema(implementation = User.class))
    ),
    @ApiResponse(responseCode = "404", description = "Not found")
})
```

## Troubleshooting

### Issue: Swagger UI not loading

**Solution**: Check that the endpoint is added to `SecurityConfig`:

```java
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
```

### Issue: "Authorize" button not working

**Solution**: Ensure the security scheme name matches:

- In `SwaggerConfig`: `bearerAuth`
- In controllers: `@SecurityRequirement(name = "bearerAuth")`

### Issue: Endpoints not showing

**Solution**: Ensure your package is scanned:

- `@ComponentScan` includes your packages
- Controllers are annotated with `@RestController`

## Best Practices

1. **Document everything**: Add descriptions to all endpoints
2. **Use examples**: Provide realistic example data
3. **Security**: Document which endpoints require authentication
4. **Versioning**: Update API version in Swagger config
5. **Keep it updated**: Update documentation when changing endpoints

## Production Considerations

In production, you might want to:

1. **Disable Swagger in production**:

   ```yaml
   springdoc:
     swagger-ui:
       enabled: ${SWAGGER_ENABLED:false}
   ```

2. **Custom path**: Use a non-standard path:

   ```yaml
   springdoc:
     swagger-ui:
       path: /api-docs
   ```

3. **Basic Auth**: Add Basic Authentication layer:
   ```java
   @Bean
   public BasicAuthenticationFilter basicAuthFilter() {
       return new BasicAuthenticationFilter(...);
   }
   ```

## Additional Resources

- [SpringDoc OpenAPI Documentation](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)
- [OpenAPI Annotations](https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations)

---

**Happy API Documentation! 📚**
