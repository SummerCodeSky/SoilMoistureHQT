#!/bin/bash

# 土壤墒情批量处理工具 - Linux/macOS 构建脚本

echo "============================================"
echo "  土壤墒情批量处理工具 - 构建脚本"
echo "============================================"
echo ""

# 检查 Maven 是否安装
if ! command -v mvn &> /dev/null; then
    echo "[错误] 未找到 Maven 构建工具"
    echo ""
    echo "请先安装 Maven:"
    echo "  macOS: brew install maven"
    echo "  Ubuntu/Debian: sudo apt install maven"
    echo "  CentOS/RHEL: sudo yum install maven"
    exit 1
fi

# 检查 Java 是否安装
if ! command -v java &> /dev/null; then
    echo "[错误] 未找到 Java 运行环境"
    echo "请安装 JDK 17 或更高版本"
    exit 1
fi

echo "[信息] 正在构建项目..."
echo ""

# 执行 Maven 构建
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "[错误] 构建失败，请检查错误信息"
    exit 1
fi

# 复制生成的 JAR 文件到当前目录
if [ -f "target/soil-moisture-hqt-1.0.0.jar" ]; then
    cp -f "target/soil-moisture-hqt-1.0.0.jar" .
    echo ""
    echo "============================================"
    echo "  构建成功！"
    echo "  JAR 文件: soil-moisture-hqt-1.0.0.jar"
    echo "============================================"
else
    echo ""
    echo "[错误] 未找到生成的 JAR 文件"
    exit 1
fi

echo ""
echo "现在可以运行 ./run.sh 启动程序"
echo ""
