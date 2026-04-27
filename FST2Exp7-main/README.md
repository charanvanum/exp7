# Experiment 7: Role-Based Authorization (RBAC) with Spring Boot

This project implements authentication and role-based authorization using Spring Security.

## Features Implemented

- User authentication using Spring Security
- Role-based access control with `ROLE_USER` and `ROLE_ADMIN`
- Protected APIs by role:
  - `GET /api/public/hello` -> Public
  - `GET /api/user/profile` -> USER, ADMIN
  - `GET /api/admin/dashboard` -> ADMIN only
- Proper HTTP responses:
  - `401 Unauthorized` when authentication is missing/invalid
  - `403 Forbidden` when role is insufficient
- Login endpoint for demo:
  - `POST /api/auth/login`

## Project Structure

```text
src/
├── main/
│   ├── java/com/example/experiment7/
│   │   ├── config/
│   │   │   ├── DataInitializer.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── AdminController.java
│   │   │   ├── AuthController.java
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── PublicController.java
│   │   │   └── UserController.java
│   │   ├── dto/
│   │   │   ├── ErrorResponse.java
│   │   │   ├── LoginRequest.java
│   │   │   └── LoginResponse.java
│   │   ├── entity/
│   │   │   ├── AppUser.java
│   │   │   └── Role.java
│   │   ├── repository/
│   │   │   └── UserRepository.java
│   │   ├── service/
│   │   │   ├── AuthService.java
│   │   │   └── CustomUserDetailsService.java
│   │   └── Experiment7Application.java
│   └── resources/
│       ├── application.properties
│       └── data.sql
└── test/
    └── java/com/example/experiment7/RbacSecurityIntegrationTest.java

screenshots/
```

## Tech Stack

- Spring Boot 3
- Spring Security
- Spring Data JPA
- H2 Database
- Maven

## Demo Users

Users are seeded automatically by `DataInitializer` on startup.

- `user1 / user123` -> `ROLE_USER`
- `admin1 / admin123` -> `ROLE_ADMIN`, `ROLE_USER`

Passwords are stored in BCrypt format.

## Run the Project

```bash
mvn spring-boot:run
```

App runs on: `http://localhost:8080`

## Postman Testing Guide

### 1) Public Endpoint (No Auth)

- `GET http://localhost:8080/api/public/hello`
- Expected: `200 OK`

### 2) Login with Valid Credentials

- `POST http://localhost:8080/api/auth/login`
- Body (JSON):

```json
{
  "username": "user1",
  "password": "user123"
}
```

- Expected: `200 OK` with user and role info

### 3) USER Accessing USER Endpoint

- `GET http://localhost:8080/api/user/profile`
- Auth: Basic Auth (`user1` / `user123`)
- Expected: `200 OK`

### 4) USER Accessing ADMIN Endpoint

- `GET http://localhost:8080/api/admin/dashboard`
- Auth: Basic Auth (`user1` / `user123`)
- Expected: `403 Forbidden`

### 5) ADMIN Accessing ADMIN Endpoint

- `GET http://localhost:8080/api/admin/dashboard`
- Auth: Basic Auth (`admin1` / `admin123`)
- Expected: `200 OK`

### 6) Request Without Authentication

- `GET http://localhost:8080/api/user/profile`
- No auth
- Expected: `401 Unauthorized`

## 401 vs 403 (Important)

- `401 Unauthorized`: Not logged in or invalid credentials
- `403 Forbidden`: Logged in, but role does not allow access

## Run Tests

```bash
mvn test
```

Integration tests verify:

- Public access success
- Unauthorized request returns 401
- USER allowed on `/api/user/**`
- USER blocked on `/api/admin/**` with 403
- ADMIN allowed on `/api/admin/**`
- Login success/failure

## Mandatory Screenshots to Add

Add at least 4 images under `screenshots/`.

Required:

1. Login request with valid credentials
2. Successful login response (or secured endpoint success)
3. USER role accessing user endpoint successfully
4. USER denied access to ADMIN endpoint (403) OR ADMIN successfully accessing ADMIN endpoint

Recommended extras:

- Invalid login attempt
- Request without token/auth showing 401
- Access denied response showing 403

You can use the checklist in `screenshots/README.md`.
