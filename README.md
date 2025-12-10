# Sabaiko Blood Bank - Blood Donation Platform

A comprehensive Spring Boot web application for managing blood donations, connecting verified donors with receivers in Nepal. The platform streamlines the blood donation process by providing a secure, role-based system for donors, receivers, and administrators.

## Tech Stack

- **Backend Framework**: Spring Boot 3.3.5
  - Spring MVC (Web)
  - Spring Security (Authentication & Authorization)
  - Spring Data JPA (Database Access)
  - Spring Validation (Form Validation)
- **Frontend**:
  - Thymeleaf (Server-side templating)
  - Tailwind CSS (Styling)
  - Bootstrap 5 (Some components)
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **Java Version**: 21
- **Security**: BCrypt password encoding, CSRF protection

## System Overview

The platform operates with three main user roles, each with specific workflows and access levels:

### Landing & Authentication
- **Landing Page**: Login page is the default entry point (`localhost:8080`)
- **Registration**: Users can register as either Donor or Receiver from the login page
- **Authentication**: Role-based redirects after successful login
  - Admin → Admin Dashboard
  - Donor → Donor Dashboard (Home page)
  - Receiver → Receiver Dashboard (Home page)

### Admin Workflow
- **Dashboard**: View statistics and manage users
- **Pending Users**: Review and approve/reject new registrations
- **Verified Users**: View all approved donors and receivers
- **User Management**: 
  - Approve or reject pending accounts
  - Restrict or delete user accounts
  - Add stars to donors for verified donations
- **Navigation**: Accessible through header (Pending Users, Verified Users, Logout)

### Donor Workflow
- **Registration**: Create account with blood type, address details (Province, District, Municipality, Ward)
- **Dashboard**: Home page with welcome content and phone mockup images
- **Profile Management**:
  - View profile details
  - Edit profile (all fields except Blood Type - which is locked)
  - Update address (Province, District, Municipality, Ward)
  - Upload profile image
  - Set availability status
  - Update medical history and bio
- **Public Pages**: Access About, Blog, and FAQ pages through header
- **Navigation**: Accessible through header (My Profile, About, Blog, FAQ, Logout)

### Receiver Workflow
- **Registration**: Create account with PAN, organization name, and address details
- **Dashboard**: Home page with "Search Donors" button
- **Search Functionality**:
  - Search donors by Blood Type
  - Filter by Address (Province, District, Municipality, Ward)
  - View available donors with contact information
  - Verify donations after receiving blood
- **Profile Management**: Edit profile details
- **Navigation**: Accessible through header (Profile, Search Donors, Logout)

## Key Features

### Address Management
- Address is composed of four components:
  - Province
  - District
  - Municipality (Palika)
  - Ward Number
- Address fields are combined automatically during registration and editing
- Real-time address preview in registration forms

### Security Features
- Role-based access control (ADMIN, DONOR, RECEIVER)
- Pending users cannot login until admin approval
- Password encryption using BCrypt
- CSRF protection enabled
- Session management

### Responsive Design
- Mobile-friendly interface
- Responsive navigation with dropdown menus for mobile devices
- Scrollable registration forms
- Adaptive layouts for all screen sizes

### File Uploads
- Profile image uploads for donors
- Files stored in `uploads/` directory
- Maximum file size: 10MB

## Project Structure

```
src/main/java/bloodbank/
├── controller/          # MVC Controllers
│   ├── AdminController.java
│   ├── AuthController.java
│   ├── DonorController.java
│   ├── ReceiverController.java
│   ├── HomeController.java
│   └── SearchController.java
├── service/            # Business Logic Layer
│   ├── AdminService.java
│   ├── DonorService.java
│   ├── ReceiverService.java
│   ├── UserService.java
│   ├── FileUploadService.java
│   └── DonationHistoryService.java
├── repository/         # Data Access Layer (JPA Repositories)
│   ├── UserRepository.java
│   ├── DonorDetailsRepository.java
│   ├── ReceiverDetailsRepository.java
│   ├── DocumentRepository.java
│   └── DonationHistoryRepository.java
├── entity/             # JPA Entities
│   ├── User.java
│   ├── DonorDetails.java
│   ├── ReceiverDetails.java
│   ├── Document.java
│   ├── DonationHistory.java
│   ├── BloodGroup.java (Enum)
│   ├── UserRole.java (Enum)
│   └── UserStatus.java (Enum)
├── dto/                # Data Transfer Objects
│   ├── RegistrationDTO.java
│   ├── DonorUpdateDTO.java
│   └── ReceiverUpdateDTO.java
├── config/             # Configuration Classes
│   ├── SecurityConfig.java
│   ├── CustomAuthenticationSuccessHandler.java
│   ├── WebConfig.java
│   └── DataInitializer.java
└── BloodDonationPlatformApplication.java

src/main/resources/
├── templates/          # Thymeleaf Templates
│   ├── auth/          # Authentication pages
│   │   ├── login.html
│   │   ├── register-donor.html
│   │   └── register-receiver.html
│   ├── admin/         # Admin pages
│   │   ├── dashboard.html
│   │   ├── pending-users.html
│   │   └── verified-users.html
│   ├── donor/         # Donor pages
│   │   ├── donor-dashboard.html
│   │   ├── donor-profile.html
│   │   ├── edit-donor.html
│   │   └── search-users.html
│   ├── receiver/      # Receiver pages
│   │   ├── receiver-dashboard.html
│   │   ├── receiver-profile.html
│   │   ├── edit-receiver.html
│   │   ├── search-donor.html
│   │   ├── about.html
│   │   ├── blog.html
│   │   └── faq.html
│   └── layout/        # Shared layout components
│       ├── header.html
│       ├── footer.html
│       ├── home.html
│       ├── about.html
│       ├── blog.html
│       ├── faq.html
│       └── search.html
├── static/            # Static Resources
│   ├── phone-mockup-1.png
│   └── phone-mockup-2.png
└── application.properties
```

## Database Setup

1. **Install PostgreSQL** (Version 12 or higher recommended)

2. **Create Database**:
   ```sql
   CREATE DATABASE blooddonationplatformdb;
   ```

3. **Create User** (optional):
   ```sql
   CREATE USER blooddonationplatform WITH PASSWORD 'blooddonationplatform';
   GRANT ALL PRIVILEGES ON DATABASE blooddonationplatformdb TO blooddonationplatform;
   ```

4. **Run Schema** (if using schema file):
   ```bash
   psql -U blooddonationplatform -d blooddonationplatformdb -f database/schema.sql
   ```

## Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5433/blooddonationplatformdb
spring.datasource.username=blooddonationplatform
spring.datasource.password=blooddonationplatform

# JPA/Hibernate - Auto-create/update tables
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Server Port
server.port=8080
```

## Running the Application

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd BloodDonationPlatform
   ```

2. **Ensure PostgreSQL is running** on port 5433 (or update the port in `application.properties`)

3. **Run the application**:
   ```bash
   # Using Gradle
   ./gradlew bootRun
   
   # Or on Windows
   gradlew.bat bootRun
   ```

4. **Access the application**:
   - Open browser: `http://localhost:8080`
   - You will be redirected to the login page

## Default Admin Credentials

The system includes a default admin account created by `DataInitializer`:
- **Username**: `admin`
- **Password**: `admin123`

**Note**: Change the default password in production!

## User Registration Flow

### Donor Registration
1. Click "Register as Donor" on login page
2. Fill in:
   - Username, Email, Password
   - Full Name, Phone
   - Address details (Province, District, Municipality, Ward)
   - Blood Group (required, cannot be changed later)
3. Submit → Account status: PENDING
4. Wait for admin approval
5. Login after approval → Access Donor Dashboard

### Receiver Registration
1. Click "Register as Receiver" on login page
2. Fill in:
   - Username, Email, Password
   - Organization Name, Phone
   - PAN (required for receivers)
   - Address details (Province, District, Municipality, Ward)
3. Submit → Account status: PENDING
4. Wait for admin approval
5. Login after approval → Access Receiver Dashboard

## API Endpoints

### Public Endpoints
- `GET /` - Redirects to login page
- `GET /auth/login` - Login page
- `GET /auth/register/donor` - Donor registration form
- `GET /auth/register/receiver` - Receiver registration form
- `POST /auth/register/donor` - Submit donor registration
- `POST /auth/register/receiver` - Submit receiver registration
- `GET /about` - About page (public)
- `GET /blog` - Blog page (public)
- `GET /faq` - FAQ page (public)
- `GET /search` - Public blood search

### Admin Endpoints (Requires ADMIN role)
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/pending` - Pending users list
- `GET /admin/verified` - Verified users list
- `POST /admin/approve` - Approve user
- `POST /admin/reject` - Reject user
- `POST /admin/restrict` - Restrict user account
- `POST /admin/delete` - Delete user account
- `POST /admin/add-star` - Add star to donor

### Donor Endpoints (Requires DONOR role)
- `GET /donor/dashboard` - Donor dashboard (home page)
- `GET /donor/profile` - View donor profile
- `GET /donor/edit` - Edit profile form
- `POST /donor/update` - Update profile
- `POST /donor/delete` - Delete account

### Receiver Endpoints (Requires RECEIVER role)
- `GET /receiver/dashboard` - Receiver dashboard (home page)
- `GET /receiver/profile` - View receiver profile
- `GET /receiver/search` - Search donors page
- `POST /receiver/verify-donation` - Verify donation
- `GET /receiver/edit` - Edit profile form
- `POST /receiver/update` - Update profile
- `POST /receiver/delete` - Delete account

## Database Schema

### Core Tables

#### `users`
- User accounts with authentication details
- Fields: id, username, password (encrypted), email, full_name, phone, role, status
- Status: PENDING, VERIFIED, RESTRICTED

#### `donor_details`
- Donor-specific information
- Fields: id, user_id (FK), blood_group, date_of_birth, address, province, district, palika, ward_no, availability, medical_history, profile_image, bio, stars, last_donation_date
- Relationship: One-to-One with User

#### `receiver_details`
- Receiver-specific information
- Fields: id, user_id (FK), pan, address, province, district, palika, ward_no, bio
- Relationship: One-to-One with User

#### `documents`
- Uploaded documents (Citizenship, PAN)
- Fields: id, user_id (FK), document_type, file_path, upload_date
- Relationship: Many-to-One with User

#### `donation_history`
- Donation records and verifications
- Fields: id, donor_id (FK), receiver_id (FK), donation_date, verified_by_receiver, verified_date
- Relationships: Many-to-One with User (donor and receiver)

## Security Configuration

- **Authentication**: Form-based login
- **Password Encoding**: BCrypt
- **Role-Based Access**: ADMIN, DONOR, RECEIVER
- **CSRF Protection**: Enabled
- **Session Management**: Default Spring Security session handling
- **Access Control**:
  - Pending users cannot login
  - Role-specific route protection
  - Public pages accessible without authentication

## Development Notes

- **Architecture**: Simple 3-layer architecture (Controller → Service → Repository)
- **Package Structure**: `bloodbank` package
- **Template Engine**: Thymeleaf with Spring Security integration
- **Styling**: Primarily Tailwind CSS with some Bootstrap components
- **Responsive Design**: Mobile-first approach with dropdown navigation
- **File Storage**: Local file system (`uploads/` directory)
- **Database**: JPA/Hibernate with auto-update schema

## Features Implemented

✅ Role-based authentication and authorization  
✅ User registration (Donor/Receiver) with admin approval workflow  
✅ Address management (Province, District, Municipality, Ward)  
✅ Donor profile management (editable except blood type)  
✅ Receiver search functionality (by blood type and address)  
✅ Admin dashboard with user management  
✅ Responsive design with mobile navigation  
✅ Public pages (About, Blog, FAQ)  
✅ File upload for profile images  
✅ Donation verification system  
✅ Star rating for donors  

## License

This project is for educational purposes.

## Author

Sabaiko Blood Bank Platform
