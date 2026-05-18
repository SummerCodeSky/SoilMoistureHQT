@echo off
chcp 65001 >nul
echo ========================================
echo   土壤墒情批量处理
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未安装 Java
    pause
    exit /b 1
)

if not exist input (
    echo [错误] input 目录不存在，请先运行 deploy.bat
    pause
    exit /b 1
)

if not exist template.txt (
    echo [错误] template.txt 不存在，请先运行 deploy.bat
    pause
    exit /b 1
)

if not exist config.json (
    echo [错误] config.json 不存在，请先运行 deploy.bat
    pause
    exit /b 1
)

if not exist target\soil-moisture-hqt-1.0.0.jar (
    echo [错误] 程序不存在，请先运行 deploy.bat
    pause
    exit /b 1
)

echo 开始处理 Excel 文件...
echo.

java -jar target\soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

echo.
if %errorlevel% equ 0 (
    echo ========================================
    echo   处理完成！
    echo ========================================
    echo.
    explorer output
)

pause
