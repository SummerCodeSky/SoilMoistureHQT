@echo off
chcp 65001 >nul
echo ========================================
echo   一键部署脚本 (Windows)
echo ========================================
echo.

REM 检查 Java
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
java -version
echo.

REM 检查 Maven
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 未检测到 Maven
    echo.
    echo 请选择:
    echo   1. 手动安装 Maven: https://maven.apache.org/download.cgi
    echo   2. 使用项目自带的 Maven Wrapper (如果有 mvnw.cmd)
    echo.
    if exist "mvnw.cmd" (
        echo [检测] 发现 Maven Wrapper，使用 mvnw.cmd 编译
        set MAVEN_CMD=mvnw.cmd
    ) else (
        echo [错误] 未找到 Maven，无法编译
        pause
        exit /b 1
    )
) else (
    echo [检查] Maven 环境正常
    mvn -version | findstr /C:"Apache Maven"
    set MAVEN_CMD=mvn
)
echo.

REM 编译项目
echo ========================================
echo   开始编译...
echo ========================================
echo.

%MAVEN_CMD% clean package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo   编译失败!
    echo ========================================
    echo.
    echo 请检查:
    echo   1. JDK 版本是否为 17+
    echo   2. Maven 是否正常
    echo   3. 网络连接是否正常 (需要下载依赖)
    echo.
    pause
    exit /b 1
)

echo.
echo [完成] 编译成功!
echo.

REM 运行初始化
echo ========================================
echo   初始化项目结构...
echo ========================================
echo.

call init.bat

echo.
echo ========================================
echo   部署完成!
echo ========================================
echo.
echo 下一步:
echo   1. 将 Excel 文件放入 input 目录
echo   2. 运行 run.bat 开始处理
echo.
echo 文件说明:
echo   - run.bat        批量处理脚本
echo   - init.bat       初始化脚本
echo   - template.txt   文本模板
echo   - config.json    替换规则配置
echo   - input/         放入 Excel 文件
echo   - output/        输出结果目录
echo.

pause
