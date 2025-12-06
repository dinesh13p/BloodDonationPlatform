# Fix Startup Error - PasswordEncoder Not Found

## Problem
The application shows error: `java.lang.ClassNotFoundException: PasswordEncoder`
This is because Spring Security dependencies are not being loaded correctly.

## Solution Steps

### Step 1: Clean Build Cache

**In Terminal/Command Prompt:**
```bash
cd E:\SpringBoot\BloodDonationPlatform
gradlew.bat clean
```

### Step 2: Delete Build Folders

**Manually delete these folders:**
- `build/` folder
- `.gradle/` folder (if exists in project root)

### Step 3: Refresh Dependencies in IDE

**If using Spring Tool Suite (STS) or Eclipse:**
1. Right-click on project → **Gradle** → **Refresh Gradle Project**
2. Wait for dependencies to download

**If using IntelliJ IDEA:**
1. Right-click on `build.gradle` → **Reload Gradle Project**
2. Or: **File** → **Invalidate Caches** → **Invalidate and Restart**

### Step 4: Rebuild Project

**In Terminal:**
```bash
gradlew.bat build
```

This will download all dependencies. First time may take 5-10 minutes.

### Step 5: Run Application

**Option A: Using Gradle**
```bash
gradlew.bat bootRun
```

**Option B: Using IDE**
1. Right-click `BloodDonationPlatformApplication.java`
2. **Run As** → **Spring Boot App**

## If Still Not Working

### Check build.gradle
Make sure it has:
```gradle
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.1.5'
    id 'io.spring.dependency-management' version '1.1.3'
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-security'
    // ... other dependencies
}
```

### Verify Dependencies Downloaded
Check if these folders exist:
- `build/classes/java/main/`
- `.gradle/caches/` (in user home)

### Alternative: Use Maven
If Gradle continues to have issues, you can convert to Maven, but Gradle should work fine.

## Expected Output When Working

When you run `gradlew.bat bootRun`, you should see:
```
...
Started BloodDonationPlatformApplication in X.XXX seconds
```

Then access: http://localhost:8080

