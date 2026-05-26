#!/bin/bash

# 土壤墒情批量处理工具 - Linux/macOS 启动脚本

echo "============================================"
echo "  土壤墒情批量处理工具"
echo "============================================"
echo ""

# 检查 Java 是否安装
if ! command -v java &> /dev/null; then
    echo "[错误] 未找到 Java 运行环境"
    echo "请安装 JDK 17 或更高版本"
    echo "  macOS: brew install openjdk@17"
    echo "  Ubuntu/Debian: sudo apt install openjdk-17-jdk"
    echo "  CentOS/RHEL: sudo yum install java-17-openjdk"
    exit 1
fi

# 检查 Java 版本
JAVA_VERSION=$(java -version 2>&1 | head -1 | sed -E 's/.*"([0-9]+)\..*/\1/')
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "[错误] Java 版本过低 (当前: $JAVA_VERSION)，需要 JDK 17 或更高版本"
    exit 1
fi

# 检查必要文件
if [ ! -f "soil-moisture-hqt-1.0.0.jar" ]; then
    echo "[错误] 未找到 soil-moisture-hqt-1.0.0.jar"
    echo "请先运行 ./build.sh 构建项目，或将已构建的 JAR 文件放到当前目录"
    exit 1
fi

if [ ! -f "template.txt" ]; then
    echo "[错误] 未找到 template.txt 模板文件"
    exit 1
fi

if [ ! -f "config.json" ]; then
    echo "[错误] 未找到 config.json 配置文件"
    exit 1
fi

if [ ! -d "input" ]; then
    echo "[提示] 未找到 input 目录，正在创建..."
    mkdir -p input
    echo "请将需要处理的 Excel 文件放入 input 目录"
    exit 1
fi

# 检查 input 目录是否有 Excel 文件
EXCEL_COUNT=$(find input -name "*.xlsx" -o -name "*.xls" 2>/dev/null | wc -l)
if [ "$EXCEL_COUNT" -eq 0 ]; then
    echo "[提示] input 目录中没有找到 Excel 文件"
    echo "请将需要处理的 .xlsx 或 .xls 文件放入 input 目录"
    exit 1
fi

echo "[信息] 正在处理 Excel 文件..."
echo ""

# 运行处理程序
java -jar soil-moisture-hqt-1.0.0.jar --batch --text template.txt --config config.json

if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 处理过程中发生错误"
    exit 1
fi

echo ""
echo "============================================"
echo "  处理完成！"
echo "  输出文件位于 output 目录"
echo "============================================"
echo ""
