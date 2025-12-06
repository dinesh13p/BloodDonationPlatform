# Setup Guide - Sabaiko Blood Bank

## Prerequisites

1. **Java 21** - Install JDK 21
2. **PostgreSQL** - Install PostgreSQL (you're using pgAdmin4)
3. **Gradle** - Included in project (gradlew)

## Step-by-Step Setup

### 1. Database Setup (pgAdmin4)

1. **Open pgAdmin4** and connect to your PostgreSQL server

2. **Create Database**:
   - Right-click on "Databases" → "Create" → "Database"
   - Name: `sabaiko_bloodbank`
   - Click "Save"

3. **Run Schema Script**:
   - Right-click on `sabaiko_bloodbank` database → "Query Tool"
   - Open the file: `database/schema.sql`
   - Copy all SQL content and paste into Query Tool
   - Click "Execute" (F5) or click the play button
   - This will create all tables and insert the default admin user

4. **Verify Database**:
   - Check that these tables exist:
     - `users`
     - `donor_details`
     - `receiver_details`
     - `documents`
     - `donation_history`

### 2. Configure Application

1. **Update `src/main/resources/application.properties`**:
   ```properties
   # Database Configuration
   spring.datasource.url=jdbc:postgresql://localhost:5433/sabaiko_bloodbank
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```
   
   **Important**: Update these values:
   - Port: Change `5433` to your PostgreSQL port (default is `5432`)
   - Username: Your PostgreSQL username (default is `postgres`)
   - Password: Your PostgreSQL password

### 3. Build the Project

Open terminal/command prompt in project root and run:

**Windows:**
```bash
gradlew.bat build
```

**Linux/Mac:**
```bash
./gradlew build
```

This will download all dependencies (first time may take a few minutes).

### 4. Run the Application

**Option 1: Using Gradle (Recommended)**

**Windows:**
```bash
gradlew.bat bootRun
```

**Linux/Mac:**
```bash
./gradlew bootRun
```

**Option 2: Using IDE (Spring Tool Suite)**

1. Import project as Gradle project
2. Right-click on `BloodDonationPlatformApplication.java`
3. Select "Run As" → "Spring Boot App"

**Option 3: Using Command Line**

```bash
java -jar build/libs/BloodDonationPlatform-1.0.0.jar
```

### 5. Access the Application

1. Open browser and go to: **http://localhost:8080**

2. **Default Admin Login**:
   - Username: `admin`
   - Password: `admin123`

3. **Test Registration**:
   - Click "Register as Donor" or "Register as Receiver"
   - Fill the form and submit
   - Login as admin to approve the user

## Troubleshooting

### Port 8080 Already in Use

If you get "Port 8080 already in use" error:

1. Change port in `application.properties`:
   ```properties
   server.port=8081
   ```
2. Access at: `http://localhost:8081`

### Database Connection Error

**Error**: `Connection refused` or `Connection to localhost:5433 refused`

**Solutions**:
1. Check PostgreSQL is running
2. Verify port number (5433 or 5432)
3. Check username/password in `application.properties`
4. Verify database name: `sabaiko_bloodbank`

### Cannot Find Database

**Error**: `database "sabaiko_bloodbank" does not exist`

**Solution**: 
1. Create database in pgAdmin4 (Step 1.2)
2. Run schema.sql (Step 1.3)

### Build Errors

**Error**: `Could not resolve dependencies`

**Solution**:
1. Check internet connection
2. Run: `./gradlew clean build --refresh-dependencies`

### Hibernate Errors

**Error**: `Table doesn't exist` or `Column doesn't exist`

**Solution**:
1. Make sure you ran `database/schema.sql`
2. Check `spring.jpa.hibernate.ddl-auto=update` in `application.properties`

## Project Structure

```
BloodDonationPlatform/
├── src/main/java/com/sabaiko/bloodbank/    # Main application code
├── src/main/resources/
│   ├── templates/                          # Thymeleaf HTML pages
│   └── application.properties              # Configuration
├── database/
│   └── schema.sql                          # Database schema
└── build.gradle                            # Dependencies
```

## Default Credentials

- **Admin**: 
  - Username: `admin`
  - Password: `admin123`

## Next Steps

1. ✅ Database created and schema executed
2. ✅ Application running on http://localhost:8080
3. ✅ Login as admin
4. ✅ Register test users (donor/receiver)
5. ✅ Approve users from admin dashboard
6. ✅ Test donor and receiver workflows

## Quick Commands Reference

```bash
# Build project
./gradlew build

# Run application
./gradlew bootRun

# Clean and rebuild
./gradlew clean build

# Run tests
./gradlew test
```

## Support

If you encounter issues:
1. Check the error message in console
2. Verify database connection in pgAdmin4
3. Check `application.properties` configuration
4. Review `README.md` for more details

