@echo off
mvn clean package -DskipTests
call init.bat
pause
