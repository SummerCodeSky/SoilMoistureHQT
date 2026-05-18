@echo off
echo Checking files...
if not exist SoilMoistureHQT.jar (
    echo ERROR: SoilMoistureHQT.jar not found.
    pause
    exit /b 1
)
if not exist data.xlsx (
    echo ERROR: data.xlsx not found.
    pause
    exit /b 1
)
if not exist template.txt (
    echo ERROR: template.txt not found.
    pause
    exit /b 1
)
if not exist config.json (
    echo ERROR: config.json not found.
    pause
    exit /b 1
)

echo Running...
java -jar SoilMoistureHQT.jar --excel data.xlsx --text template.txt --config config.json --sheet-index 2

if errorlevel 1 (
    echo ERROR: Execution failed.
    pause
    exit /b 1
)

echo Success! Output: SoilMoisture.HQT
pause
