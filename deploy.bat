@echo off
echo ========================================
echo   Soil Moisture - Deploy Tool
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java not installed. Install JDK 17+
    echo https://adoptium.net/
    pause
    exit /b 1
)

echo Step 1: Build project...
mvn clean package -DskipTests >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Build failed
    pause
    exit /b 1
)
echo OK: Build complete
echo.

echo Step 2: Create directories...
mkdir input 2>nul
mkdir output 2>nul
echo OK: input/ and output/ created
echo.

echo Step 3: Copy config files...
copy /Y src\main\resources\template.txt template.txt >nul
copy /Y src\main\resources\config.json config.json >nul
echo OK: template.txt and config.json copied
echo.

echo ========================================
echo   Deploy Complete!
echo ========================================
echo.
echo Next steps:
echo   1. Put Excel files into input/
echo   2. Run run.bat to process
echo.

pause
