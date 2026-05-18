@echo off
echo Setting up...

if not exist input (
    mkdir input
    echo Created: input/
)

if not exist output (
    mkdir output
    echo Created: output/
)

if not exist template.txt (
    copy src\main\resources\template.txt template.txt >nul
    echo Created: template.txt
)

if not exist config.json (
    copy src\main\resources\config.json config.json >nul
    echo Created: config.json
)

echo.
echo Setup complete!
echo.
echo Next: 
echo   1. Put Excel files in input/
echo   2. Run run.bat
echo.

pause
