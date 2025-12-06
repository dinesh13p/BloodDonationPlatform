# REST API Endpoints

## Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/auth/login` | Show login page | Public |
| GET | `/auth/register/donor` | Show donor registration form | Public |
| GET | `/auth/register/receiver` | Show receiver registration form | Public |
| POST | `/auth/register/donor` | Submit donor registration | Public |
| POST | `/auth/register/receiver` | Submit receiver registration | Public |
| POST | `/logout` | Logout user | Authenticated |

## Admin Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/admin/dashboard` | Admin dashboard | ADMIN |
| GET | `/admin/pending` | View pending users | ADMIN |
| GET | `/admin/verified` | View verified users | ADMIN |
| POST | `/admin/approve` | Approve user (userId) | ADMIN |
| POST | `/admin/reject` | Reject user (userId) | ADMIN |
| POST | `/admin/restrict` | Restrict user (userId) | ADMIN |
| POST | `/admin/add-star` | Add star to donor (donorId) | ADMIN |

## Donor Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/donor/profile` | View donor profile | DONOR |
| GET | `/donor/edit` | Edit profile form | DONOR |
| POST | `/donor/update` | Update profile | DONOR |
| POST | `/donor/delete` | Delete account | DONOR |

## Receiver Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/receiver/profile` | View receiver profile | RECEIVER |
| GET | `/receiver/search` | Search donors (bloodGroup, city, state) | RECEIVER |
| POST | `/receiver/verify-donation` | Verify donation (donorId) | RECEIVER |
| POST | `/receiver/delete` | Delete account | RECEIVER |

## Request Parameters

### Registration
- `username` (String, required)
- `password` (String, required, min 6 chars)
- `email` (String, required, valid email)
- `fullName` (String, required)
- `phone` (String, required)
- `bloodGroup` (BloodGroup enum, required for donor)
- `bloodGroupNeeded` (BloodGroup enum, required for receiver)

### Donor Update
- `address` (String, optional)
- `city` (String, optional)
- `state` (String, optional)
- `zipCode` (String, optional)
- `availability` (Boolean, optional)
- `medicalHistory` (String, optional)
- `dateOfBirth` (Date, optional)
- `bloodGroup` (BloodGroup enum, optional)
- `profileImage` (MultipartFile, optional)

### Search Donors
- `bloodGroup` (String, optional) - Enum name (e.g., "A_POSITIVE")
- `city` (String, optional)
- `state` (String, optional)

## Response Types

All endpoints return HTML views (Thymeleaf templates) except for error cases which may return JSON or redirect to error pages.

## Error Handling

- Validation errors are shown on the form pages
- Authentication errors redirect to login page
- Authorization errors return 403 Forbidden
- Database errors are logged and shown as user-friendly messages


