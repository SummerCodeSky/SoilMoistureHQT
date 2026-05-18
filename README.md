# 土壤墒情录入项目 (Soil Moisture HQT)

从 jar 包反编译恢复的 Java 项目，用于 Excel 数据文本替换。

## 项目结构

```
SoilMoistureHQT/
├── pom.xml                          # Maven 配置文件
├── README.md                        # 项目说明
├── .gitignore                       # Git 忽略配置
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/excelreplacer/
│   │   │       ├── Main.java              # 主程序入口
│   │   │       ├── CliArgs.java           # 命令行参数定义
│   │   │       ├── CliParser.java         # 命令行参数解析
│   │   │       ├── ConfigLoader.java      # JSON 配置加载
│   │   │       ├── ExcelReader.java       # Excel 文件读取
│   │   │       ├── FileWriter.java        # 文件写入
│   │   │       ├── TextReplacer.java      # 文本替换核心
│   │   │       ├── ReplaceRule.java       # 替换规则定义
│   │   │       ├── ReplaceDetail.java     # 替换详情
│   │   │       └── ReplaceReport.java     # 替换报告
│   │   └── resources/
│   │       ├── data.xlsx                # Excel 数据文件（示例）
│   │       ├── template.txt             # 文本模板（示例）
│   │       ├── config.json              # 配置文件（示例）
│   │       └── log4j2.xml               # 日志配置
│   └── test/
│       ├── java/                        # 测试代码
│       └── resources/                   # 测试资源
├── data.xlsx                        # 数据文件（工作目录）
├── template.txt                     # 模板文件（工作目录）
├── config.json                      # 配置文件（工作目录）
├── run.bat                          # Windows 启动脚本
├── SoilMoistureHQT.jar              # 原始 jar 包
└── SoilMoisture.HQT                 # 输出结果
```

## 编译项目

### 前置要求
- JDK 17+
- Maven 3.6+

### 编译命令

```bash
cd /workspace/SoilMoistureHQT
mvn clean package
```

编译产物：
```
target/soil-moisture-hqt-1.0.0.jar  # 包含所有依赖的 uber jar (约 18MB)
```

## 运行方式

### 方式 1：使用编译后的 jar（推荐）

```bash
java -jar target/soil-moisture-hqt-1.0.0.jar \
  --excel data.xlsx \
  --text template.txt \
  --config config.json \
  --sheet-index 2
```

### 方式 2：使用 Maven 运行

```bash
mvn exec:java
```

### 方式 3：使用原始 jar

```bash
java -jar SoilMoistureHQT.jar \
  --excel data.xlsx \
  --text template.txt \
  --config config.json \
  --sheet-index 2
```

## 命令行参数

| 参数 | 说明 | 必需 | 默认值 |
|------|------|------|--------|
| `--excel` | Excel 数据文件路径 | 是 | - |
| `--text` | 待替换的文本模板 | 是 | - |
| `--config` | 替换规则 JSON 配置 | 是 | - |
| `--sheet` | 指定 Excel 工作表名称 | 否 | 自动检测 |
| `--sheet-index` | 指定 Excel 工作表索引（从 0 开始） | 否 | 自动检测 |
| `--output` | 输出文件路径 | 否 | `template.hqt` |

## 配置格式 (config.json)

```json
[
  {
    "row": 2,
    "col": 1,
    "regexPattern": "^((?:[^,]*,){4})[^,]+",
    "format": "YYYY-MM-DD"
  }
]
```

### 配置说明

| 字段 | 说明 | 必需 |
|------|------|------|
| `row` | Excel 行号（从 1 开始） | 是 |
| `col` | Excel 列号（从 1 开始） | 是 |
| `regexPattern` | 正则表达式匹配模式 | 是 |
| `sheet` | 工作表名称（可选） | 否 |
| `format` | 日期格式化（可选） | 否 |

### 支持的格式

- `YYYY-MM-DD`：将中文日期格式（如 `2026 年 01 月 13 日`）转换为 `2026-01-13`

## 输出

程序生成 `template.hqt` 文件（或 `--output` 指定的文件），包含替换后的最终结果。

## 开发流程

### 修改代码后重新编译

```bash
mvn clean package
java -jar target/soil-moisture-hqt-1.0.0.jar --excel data.xlsx --text template.txt --config config.json --sheet-index 2
```

### 添加日志

编辑 `src/main/resources/log4j2.xml` 调整日志级别。

## 技术栈

- **Java**: 17
- **构建工具**: Maven 3.6+
- **核心依赖**:
  - Apache POI 5.2.3 (Excel 处理)
  - Gson 2.10.1 (JSON 解析)
  - Log4j2 2.20.0 (日志)

## 反编译说明

本项目是通过 CFR 反编译器从 `SoilMoistureHQT.jar` 恢复的源代码：

```bash
# 使用 CFR 反编译
java -jar cfr.jar SoilMoistureHQT.jar --outputdir src
```

注意事项：
1. 部分注释已丢失
2. 变量名可能与原始代码略有差异
3. 已手动修复部分异常处理代码以适应现代 Java 规范

## 常见问题

### Q1: 编译时提示找不到主类
确保 `pom.xml` 中的 `mainClass` 配置正确。

### Q2: 运行时提示找不到配置文件
使用绝对路径或确保在正确的目录下运行。

### Q3: 替换结果不符合预期
1. 检查 `config.json` 中的行号、列号是否正确
2. 确认 Excel 数据与预期一致
3. 验证正则表达式模式

## 恢复日期

2026-05-18
