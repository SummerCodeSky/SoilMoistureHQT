@echo off
setlocal enabledelayedexpansion

echo ============================================
echo   Soil Moisture Batch Processing Tool
echo ============================================
echo.

:: Check Java installation
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found.
    echo Please install JDK 17 or higher: https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
)

:: Check required files
if not exist "soil-moisture-hqt-1.0.0.jar" (
    echo [ERROR] soil-moisture-hqt-1.0.0.jar not found.
    echo Please run build.bat first, or place the built JAR in this directory.
    echo.
    pause
    exit /b 1
)

if not exist "template.txt" (
    echo [ERROR] template.txt not found.
    echo.
    pause
    exit /b 1
)

if not exist "config.json" (
    echo [ERROR] config.json not found.
    echo.
    pause
    exit /b 1
)

if not exist "input" (
    echo [INFO] input directory not found. Creating...
    mkdir input
    echo Please place Excel files to process in the input directory.
    echo.
    pause
    exit /b 1
)

:: Check for Excel files
dir /b input\*.xlsx >nul 2>&1 || dir /b input\*.xls >nul 2>&1
if errorlevel 1 (
    echo [INFO] No Excel files found in input directory.
    echo Please place .xlsx or .xls files in the input directory.
    echo.
    pause
    exit /b 1
)

echo [INFO] Processing Excel files...
echo.

:: Run the application
java -jar soil-moisture-hqt-1.0.0.jar --batch --text template.txt --config config.json

if errorlevel 1 (
    echo.
    echo [ERROR] An error occurred during processing.
    pause
    exit /b 1
)

echo.
echo ============================================
echo   Processing complete!
echo   Output files are in the output directory
echo ============================================
echo.
pause
