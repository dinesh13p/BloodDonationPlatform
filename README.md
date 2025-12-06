# Sabaiko Blood Bank - Blood Donation Platform

A simple and clean Spring Boot application for managing blood donations, connecting donors with receivers.

## Tech Stack

- **Backend**: Spring Boot 3.2.0 (Spring MVC + Spring Security + Spring Data JPA)
- **Frontend**: Thymeleaf + Bootstrap 5
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **Java Version**: 21

## Features

### Admin Workflow
- Login and view dashboard
- View pending donors and receivers
- Approve or reject user accounts
- Delete or restrict users
- Verify donation results and add stars to donors
- View list of all donors and receivers

### Donor Workflow
- Register (status: PENDING)
- Login after admin approval
- Update profile (image, availability, contact, medical history)
- Receive contact from receivers
- Get stars after verified donations
- Delete account

### Receiver Workflow
- Register (status: PENDING)
- Get blue tick after admin approval
- Search donors by blood group/location
- Contact donors
- Verify donations
- Delete account

## Project Structure

```
src/main/java/com/sabaiko/bloodbank/
├── controller/          # REST Controllers
├── service/            # Business Logic
├── repository/         # Data Access Layer
├── entity/             # JPA Entities
├── dto/                # Data Transfer Objects
├── config/             # Configuration (Security, Web)
└── BloodDonationPlatformApplication.java

src/main/resources/
├── templates/          # Thymeleaf Templates
│   ├── auth/          # Login, Registration
│   ├── admin/         # Admin Dashboard
│   ├── donor/         # Donor Pages
│   └── receiver/      # Receiver Pages
├── static/            # Static Resources
└── application.properties
```

## Database Setup

1. Install PostgreSQL
2. Create database:
   ```sql
   CREATE DATABASE sabaiko_bloodbank;
   ```
3. Run the schema file:
   ```bash
   psql -U postgres -d sabaiko_bloodbank -f database/schema.sql
   ```

## Configuration

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/sabaiko_bloodbank
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Running the Application

1. Clone the repository
2. Ensure PostgreSQL is running
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```
   Or use your IDE to run `BloodDonationPlatformApplication`

4. Access the application at: `http://localhost:8080`

## Default Admin Credentials

- **Username**: admin
- **Password**: admin123

## File Uploads

Uploaded files (profile images, documents) are stored in the `uploads/` folder in the project root.

## API Endpoints

### Authentication
- `GET /auth/login` - Login page
- `GET /auth/register/donor` - Donor registration
- `GET /auth/register/receiver` - Receiver registration
- `POST /auth/register/donor` - Submit donor registration
- `POST /auth/register/receiver` - Submit receiver registration

### Admin
- `GET /admin/dashboard` - Admin dashboard
- `GET /admin/pending` - Pending users
- `GET /admin/verified` - Verified users
- `POST /admin/approve` - Approve user
- `POST /admin/reject` - Reject user
- `POST /admin/restrict` - Restrict user
- `POST /admin/add-star` - Add star to donor

### Donor
- `GET /donor/profile` - View profile
- `GET /donor/edit` - Edit profile form
- `POST /donor/update` - Update profile
- `POST /donor/delete` - Delete account

### Receiver
- `GET /receiver/profile` - View profile
- `GET /receiver/search` - Search donors
- `POST /receiver/verify-donation` - Verify donation
- `POST /receiver/delete` - Delete account

## Database Schema

### Tables
- `users` - User accounts
- `donor_details` - Donor-specific information
- `receiver_details` - Receiver-specific information
- `documents` - Uploaded documents (Citizenship, PAN)
- `donation_history` - Donation records

### Relationships
- User → DonorDetails (1-1)
- User → ReceiverDetails (1-1)
- User → Documents (1-many)
- DonationHistory → Donor (many-to-one)
- DonationHistory → Receiver (many-to-one)

## Security

- Spring Security with BCrypt password encoding
- Role-based access control (ADMIN, DONOR, RECEIVER)
- Pending users cannot login
- CSRF protection enabled

## Development Notes

- This is a beginner-friendly, college-project-level implementation
- No enterprise patterns or microservices
- Simple 3-layer architecture (Controller → Service → Repository)
- Clean and functional code

## License

This project is for educational purposes.


