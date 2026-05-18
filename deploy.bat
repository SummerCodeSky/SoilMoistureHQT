@echo off
chcp 65001 >nul
echo ========================================
echo   土壤墒情录入 - 部署工具
echo ========================================
echo.

REM 检查 Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未安装 Java，请先安装 JDK 17+
    echo https://adoptium.net/
    pause
    exit /b 1
)

echo [1/4] 检查 Java 环境... OK
echo.

REM 编译
echo [2/4] 编译项目...
mvn clean package -DskipTests >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 编译失败，请检查 Maven 是否正常
    pause
    exit /b 1
)
echo         编译完成
echo.

REM 创建目录
echo [3/4] 创建目录...
mkdir input 2>nul
mkdir output 2>nul
echo         input/ 和 output/ 已创建
echo.

REM 复制配置
echo [4/4] 复制配置文件...
copy /Y src\main\resources\template.txt template.txt >nul
copy /Y src\main\resources\config.json config.json >nul
echo         template.txt 和 config.json 已复制
echo.

echo ========================================
echo   部署完成！
echo ========================================
echo.
echo 使用步骤:
echo   1. 将 Excel 文件放入 input 目录
echo   2. 运行 run.bat 开始处理
echo.
echo 文件说明:
echo   - run.bat     : 批量处理 Excel 文件
echo   - template.txt: 文本模板
echo   - config.json : 替换规则配置
echo.

pause
