@echo off
mkdir input
mkdir output
copy src\main\resources\template.txt template.txt
copy src\main\resources\config.json config.json
echo Done! Run run.bat to process files.
pause
