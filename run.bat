@echo off
echo ========================================
echo   Soil Moisture - Batch Processor
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not installed
    pause
    exit /b 1
)

if not exist input (
    echo ERROR: input folder not found. Run deploy.bat first
    pause
    exit /b 1
)

if not exist template.txt (
    echo ERROR: template.txt not found. Run deploy.bat first
    pause
    exit /b 1
)

if not exist config.json (
    echo ERROR: config.json not found. Run deploy.bat first
    pause
    exit /b 1
)

if not exist target\soil-moisture-hqt-1.0.0.jar (
    echo ERROR: Program not found. Run deploy.bat first
    pause
    exit /b 1
)

echo Processing Excel files...
echo.

java -jar target\soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

echo.
if %errorlevel% equ 0 (
    echo ========================================
    echo   Processing Complete!
    echo ========================================
    explorer output
)

pause
