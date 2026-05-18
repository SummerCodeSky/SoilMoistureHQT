@echo off
echo ========================================
echo   Soil Moisture Batch Processor
echo ========================================
echo.

if not exist input (
    echo ERROR: input folder not found
    echo Please create input folder and put Excel files
    pause
    exit /b 1
)

if not exist template.txt (
    echo ERROR: template.txt not found
    pause
    exit /b 1
)

if not exist config.json (
    echo ERROR: config.json not found
    pause
    exit /b 1
)

if not exist soil-moisture-hqt-1.0.0.jar (
    echo ERROR: soil-moisture-hqt-1.0.0.jar not found
    pause
    exit /b 1
)

echo Processing Excel files...
echo.

java -jar soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

echo.
if %errorlevel% equ 0 (
    echo ========================================
    echo   Processing Complete!
    echo ========================================
    echo.
    echo Output: output\
    echo Merged: merged.HQT
    echo.
    explorer output
)

pause
