# Sabaiko Blood Bank - System Architecture

## Simple 3-Layer Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   Controllers │  │  Thymeleaf   │  │   Static     │   │
│  │               │  │  Templates   │  │   Files      │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                     Business Layer                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │   Services   │  │     DTOs     │  │  Security    │   │
│  │              │  │              │  │   Config     │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                      Data Layer                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │
│  │ Repositories │  │   Entities   │  │ PostgreSQL   │   │
│  │              │  │              │  │  Database    │   │
│  └──────────────┘  └──────────────┘  └──────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## Module Overview

### 1. Admin Module
- **Controller**: `AdminController`
- **Service**: `AdminService`
- **Features**:
  - View dashboard with pending users
  - Approve/reject user accounts
  - Restrict users
  - Add stars to donors
  - View all users

### 2. Donor Module
- **Controller**: `DonorController`
- **Service**: `DonorService`
- **Features**:
  - View and edit profile
  - Update availability
  - Upload profile image
  - View stars
  - Delete account

### 3. Receiver Module
- **Controller**: `ReceiverController`
- **Service**: `ReceiverService`
- **Features**:
  - View profile
  - Search donors by blood group/location
  - Contact donors
  - Verify donations
  - Delete account

### 4. Auth Module
- **Controller**: `AuthController`
- **Service**: `UserService`
- **Features**:
  - User registration (Donor/Receiver)
  - User login
  - Password encryption (BCrypt)
  - Role-based access

### 5. Document Upload Module
- **Service**: `FileUploadService`
- **Features**:
  - Upload citizenship ID
  - Upload PAN card
  - Store files locally in `uploads/` folder

## Technology Stack

- **Backend Framework**: Spring Boot 3.2.0
- **Security**: Spring Security
- **ORM**: Spring Data JPA / Hibernate
- **Database**: PostgreSQL
- **Template Engine**: Thymeleaf
- **UI Framework**: Bootstrap 5
- **Build Tool**: Gradle
- **Java Version**: 21

## Request Flow

```
User Request
    │
    ▼
Controller (Auth, Validation)
    │
    ▼
Service (Business Logic)
    │
    ▼
Repository (Data Access)
    │
    ▼
PostgreSQL Database
```

## Security Flow

```
User Login
    │
    ▼
Spring Security Filter
    │
    ▼
UserService (loadUserByUsername)
    │
    ▼
Check User Status (PENDING/RESTRICTED blocked)
    │
    ▼
Create Authentication Token
    │
    ▼
Role-based Redirect
```


