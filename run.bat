@echo off
echo Checking files...

if not exist input (
    echo ERROR: input folder not found
    echo Please create input folder and put Excel files in it
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
    echo Please download the jar file
    pause
    exit /b 1
)

echo Starting batch processing...
echo.

java -jar soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

echo.
if %errorlevel% equ 0 (
    echo Processing complete!
    explorer output
)

pause
