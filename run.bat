@echo off
chcp 65001 >nul
echo ========================================
echo   土壤墒情批量处理工具
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未检测到 Java 环境，请先安装 JDK 17+
    echo 下载地址：https://adoptium.net/
    pause
    exit /b 1
)

if not exist "input" (
    echo [错误] 找不到 input 目录
    pause
    exit /b 1
)

if not exist "template.txt" (
    echo [错误] 找不到 template.txt
    pause
    exit /b 1
)

if not exist "config.json" (
    echo [错误] 找不到 config.json
    pause
    exit /b 1
)

if not exist "target\soil-moisture-hqt-1.0.0.jar" (
    echo [错误] 找不到 target\soil-moisture-hqt-1.0.0.jar
    echo 请先运行：mvn clean package
    pause
    exit /b 1
)

set /a excelCount=0
for %%f in (input\*.xlsx) do set /a excelCount+=1
for %%f in (input\*.xls) do set /a excelCount+=1

if %excelCount% equ 0 (
    echo [错误] input 目录下没有找到 Excel 文件
    pause
    exit /b 1
)

echo [信息] 找到 %excelCount% 个 Excel 文件
echo.
echo [处理中] 请稍候...
echo.

java -jar target\soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo   处理完成！
    echo ========================================
    echo.
    echo 输出文件位置:
    echo   - 独立文件：output\*.HQT
    echo   - 合并文件：merged.HQT
    echo.
    if exist "output" explorer output
) else (
    echo.
    echo ========================================
    echo   处理失败，请检查错误信息
    echo ========================================
)

pause
