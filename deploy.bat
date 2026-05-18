@echo off
chcp 65001 >nul
echo ========================================
echo   项目部署脚本 (Windows)
echo ========================================
echo.

java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 未检测到 Java 环境
    echo.
    echo 请先安装 JDK 17+
    echo 下载地址：https://adoptium.net/temurin/releases/?version=17
    echo.
    pause
    exit /b 1
)

echo [检查] Java 环境正常
java -version | findstr /C:"version"
echo.

mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    if exist "mvnw.cmd" (
        echo [检测] 使用 Maven Wrapper
        set MAVEN_CMD=mvnw.cmd
    ) else (
        echo [错误] 未检测到 Maven
        echo 请安装：https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
) else (
    echo [检查] Maven 环境正常
    set MAVEN_CMD=mvn
)
echo.

echo ========================================
echo   开始编译...
echo ========================================
echo.

%MAVEN_CMD% clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   编译失败！
    echo ========================================
    pause
    exit /b 1
)

echo.
echo [完成] 编译成功!
echo.

echo ========================================
echo   初始化项目...
echo ========================================
echo.

call init.bat

echo.
echo ========================================
echo   部署完成！
echo ========================================
echo.
echo 下一步:
echo   1. 将 Excel 文件放入 input 目录
echo   2. 运行 run.bat 开始处理
echo.

pause
