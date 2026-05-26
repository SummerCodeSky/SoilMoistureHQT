@echo off
setlocal enabledelayedexpansion

echo ============================================
echo   Soil Moisture Tool - Build Script
echo ============================================
echo.

:: Check Maven installation
mvn -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Maven not found.
    echo.
    echo Please install Maven: https://maven.apache.org/download.cgi
    echo Or use IDE (IntelliJ IDEA/Eclipse) built-in Maven.
    echo.
    pause
    exit /b 1
)

:: Check Java installation
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found.
    echo Please install JDK 17 or higher: https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
)

echo [INFO] Building project...
echo.

:: Execute Maven build
call mvn clean package -DskipTests

if errorlevel 1 (
    echo.
    echo [ERROR] Build failed. Please check the error messages above.
    pause
    exit /b 1
)

:: Copy built JAR to current directory
if exist "target\soil-moisture-hqt-1.0.0.jar" (
    copy /Y "target\soil-moisture-hqt-1.0.0.jar" . >nul
    echo.
    echo ============================================
    echo   Build successful!
    echo   JAR file: soil-moisture-hqt-1.0.0.jar
    echo ============================================
) else (
    echo.
    echo [ERROR] Built JAR file not found.
    pause
    exit /b 1
)

echo.
echo You can now run the application with run.bat
echo.
pause
