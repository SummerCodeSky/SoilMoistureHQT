@echo off
echo ========================================
echo   土壤墒情批量处理工具
echo ========================================
echo.

if not exist input (
    echo 错误：input 文件夹不存在
    echo 请创建 input 文件夹并放入 Excel 文件
    pause
    exit /b 1
)

if not exist template.txt (
    echo 错误：template.txt 不存在
    pause
    exit /b 1
)

if not exist config.json (
    echo 错误：config.json 不存在
    pause
    exit /b 1
)

if not exist soil-moisture-hqt-1.0.0.jar (
    echo 错误：soil-moisture-hqt-1.0.0.jar 不存在
    echo 请从下载链接获取程序文件
    pause
    exit /b 1
)

echo 开始处理 Excel 文件...
echo.

java -jar soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

echo.
if %errorlevel% equ 0 (
    echo ========================================
    echo   处理完成！
    echo ========================================
    echo.
    echo 输出目录：output\
    echo 合并文件：merged.HQT
    echo.
    explorer output
)

pause
