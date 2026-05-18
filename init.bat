@echo off
chcp 65001 >nul
echo ========================================
echo   项目初始化脚本
echo ========================================
echo.

mkdir input 2>nul
mkdir output 2>nul

if not exist template.txt copy src\main\resources\template.txt template.txt
if not exist config.json copy src\main\resources\config.json config.json

echo 初始化完成!
echo.
echo input 目录 - 放入 Excel 文件
echo output 目录 - 输出结果
echo.
echo 运行 run.bat 开始处理
echo.
pause
