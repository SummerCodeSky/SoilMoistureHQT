@echo off
chcp 65001 >nul
echo ========================================
echo   Deploy Script
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java not found
    echo Install JDK 17+: https://adoptium.net/
    pause
    exit /b 1
)

echo Building with Maven...
echo.

mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Build success!
echo.

call init.bat
