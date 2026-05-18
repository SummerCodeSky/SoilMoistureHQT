@echo off
chcp 65001 >nul
echo ========================================
echo   项目初始化脚本
echo ========================================
echo.

if not exist "input" (
    mkdir input
    echo [完成] 创建 input 目录
) else (
    echo [跳过] input 目录已存在
)

if not exist "output" (
    mkdir output
    echo [完成] 创建 output 目录
) else (
    echo [跳过] output 目录已存在
)

if not exist "template.txt" (
    copy /Y src\main\resources\template.txt template.txt >nul
    if exist "template.txt" (
        echo [完成] 创建 template.txt
    ) else (
        echo [警告] 无法复制 template.txt，请手动创建
    )
) else (
    echo [跳过] template.txt 已存在
)

if not exist "config.json" (
    copy /Y src\main\resources\config.json config.json >nul
    if exist "config.json" (
        echo [完成] 创建 config.json
    ) else (
        echo [警告] 无法复制 config.json，请手动创建
    )
) else (
    echo [跳过] config.json 已存在
)

echo.
echo ========================================
echo   初始化完成！
echo ========================================
echo.
echo 使用说明:
echo   1. 将 Excel 文件放入 input 目录
echo   2. 运行 run.bat 开始处理
echo   3. 结果在 output 目录和 merged.HQT
echo.

pause
