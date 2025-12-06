# ⚠️ IMPORTANT: Run Application Using Gradle

## The Problem
Your IDE (Spring Tool Suite) was using its own compiled classes from `bin/` folder, which had old/corrupted dependencies. This caused the Spring Boot 4.0.0 error.

## ✅ The Solution
**ALWAYS use Gradle to run the application, NOT the IDE's run button!**

## How to Run

### Method 1: Terminal (Recommended) ✅

1. Open **Command Prompt** or **Terminal**
2. Navigate to project:
   ```bash
   cd E:\SpringBoot\BloodDonationPlatform
   ```
3. Run:
   ```bash
   gradlew.bat bootRun
   ```

### Method 2: IDE Gradle Task

1. In Spring Tool Suite:
   - Right-click project → **Run As** → **Gradle Build...**
2. In the dialog:
   - Enter task: `bootRun`
   - Click **Run**

### Method 3: Create Run Configuration

1. **Run** → **Run Configurations...**
2. Right-click **Gradle Project** → **New**
3. Name: `BloodDonationPlatform BootRun`
4. Project: `BloodDonationPlatform`
5. Tasks: `bootRun`
6. Click **Run**

## ❌ DO NOT USE
- ❌ **Run As** → **Spring Boot App** (This uses IDE's build)
- ❌ Right-click main class → **Run As** → **Java Application**

## Verify It's Working

When you run `gradlew.bat bootRun`, you should see:
```
:: Spring Boot :: (v3.3.5)  ← Should show 3.3.5, NOT 4.0.0
Started BloodDonationPlatformApplication in X.XXX seconds
```

## First Time Setup

Before first run, build the project:
```bash
gradlew.bat clean build
```

This downloads all dependencies (takes 5-10 minutes first time).

## If You Still See Errors

1. **Delete bin folder** (if it exists):
   ```bash
   rmdir /s /q bin
   ```

2. **Refresh Gradle**:
   - Right-click project → **Gradle** → **Refresh Gradle Project**

3. **Clean and rebuild**:
   ```bash
   gradlew.bat clean build
   ```

4. **Run again**:
   ```bash
   gradlew.bat bootRun
   ```

## Summary

✅ **USE**: `gradlew.bat bootRun` (Terminal or Gradle task)  
❌ **DON'T USE**: IDE's "Run As Spring Boot App"

The IDE configuration has been updated to use Gradle's build output (`build/classes`) instead of `bin/`, but you should still use Gradle to run the application.

