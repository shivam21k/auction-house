# Auction House (Spring Boot + MySQL + JWT + Docker)

Production-style auction platform with role-based dashboards and REST APIs.

## Stack
- Spring Boot 3
- Spring Security (Form login + JWT)
- Spring Data JPA (MySQL)
- Thymeleaf UI
- Docker Compose (App + MySQL)

## Roles and Features
- `ADMIN`: dashboard metrics, pending product approval, current auction, upcoming queue, approve/reject product.
- `SELLER`: signup/login, seller dashboard, add product with auction slot request, seller profile.
- `BUYER`: signup/login, current live auction, upcoming auctions, place bids.

## Default Admin Credentials
- Email: `admin@auctionhouse.com`
- Password: `Admin@123`

## Run Locally (without Docker)
1. Configure MySQL in `src/main/resources/application.properties`.
2. Run:
```powershell
.\mvnw.cmd spring-boot:run
```

## Run with Docker Compose
```powershell
docker compose up --build
```

- App: `http://localhost:8080`
- MySQL: `localhost:3306`

## Web Routes
- `/login`
- `/signup`
- `/admin/dashboard`
- `/seller/dashboard`
- `/buyer/dashboard`

## JWT API
### Auth
- `POST /api/auth/signup`
- `POST /api/auth/login`

### Admin (Bearer token with ADMIN role)
- `GET /api/admin/dashboard`
- `POST /api/admin/products/{id}/approve`
- `POST /api/admin/products/{id}/reject`

### Seller (Bearer token with SELLER role)
- `GET /api/seller/dashboard`
- `GET /api/seller/profile`
- `POST /api/seller/products`

### Buyer (Bearer token with BUYER role)
- `GET /api/buyer/dashboard`
- `POST /api/buyer/bid`

## Sample API Login Payload
```json
{
  "email": "admin@auctionhouse.com",
  "password": "Admin@123"
}
```

Response includes JWT token:
```json
{
  "token": "<jwt>",
  "role": "ADMIN",
  "email": "admin@auctionhouse.com",
  "fullName": "System Administrator"
}
```

## Swagger / OpenAPI
- UI: http://localhost:8080/swagger-ui/index.html`r
- Spec: http://localhost:8080/v3/api-docs`r

Use the **Authorize** button in Swagger and paste: Bearer <jwt-token>.`r

