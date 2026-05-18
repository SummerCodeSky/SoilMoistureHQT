@echo off
chcp 65001 >nul
echo ========================================
echo   土壤墒情批量处理
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo Error: Java not found. Install JDK 17+ from:
    echo https://adoptium.net/
    pause
    exit /b 1
)

if not exist input (
    echo Error: input folder not found
    pause
    exit /b 1
)

if not exist template.txt (
    echo Error: template.txt not found
    pause
    exit /b 1
)

if not exist config.json (
    echo Error: config.json not found
    pause
    exit /b 1
)

if not exist target\soil-moisture-hqt-1.0.0.jar (
    echo Error: jar not found. Run: mvn clean package
    pause
    exit /b 1
)

echo Processing Excel files...
echo.

java -jar target\soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

echo.
if %errorlevel% equ 0 (
    echo Done! Check output folder.
    explorer output
)

pause
