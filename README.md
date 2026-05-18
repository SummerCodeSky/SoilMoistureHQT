# 土壤墒情录入项目 (Soil Moisture HQT)

从 jar 包反编译恢复的 Java 项目，用于 Excel 数据文本替换。

## 项目结构

```
SoilMoistureHQT/
├── pom.xml                          # Maven 配置文件
├── README.md                        # 项目说明
├── .gitignore                       # Git 忽略配置
├── setup.bat                        # Windows 初始化脚本
├── run.bat                          # Windows 运行脚本
├── run.sh                           # Linux/Mac 运行脚本
├── template.txt                     # 文本模板
├── config.json                      # 替换规则配置
├── soil-moisture-hqt-1.0.0.jar      # 程序 jar 包
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/excelreplacer/
│       │       └── ...              # Java 源代码
│       └── resources/
│           ├── data.xlsx            # Excel 数据文件（示例）
│           ├── template.txt         # 文本模板（示例）
│           ├── config.json          # 配置文件（示例）
│           └── log4j2.xml           # 日志配置
├── input/                           # 【放置 Excel 文件】
│   ├── 数据 1.xlsx
│   └── 数据 2.xlsx
├── output/                          # 【输出目录】
│   ├── 数据 1.HQT
│   └── 数据 2.HQT
└── merged.HQT                       # 【合并文件】所有输出合并
```

## 快速使用

### Windows 用户

#### 1. 初始化（首次使用）

```batch
setup.bat
```

自动创建：
- `input/` 目录
- `output/` 目录
- `template.txt`
- `config.json`

#### 2. 放入 Excel 文件

将 Excel 文件放入 `input/` 目录

#### 3. 运行处理

```batch
run.bat
```

### Linux/Mac 用户

```bash
# 初始化
mkdir input output
cp src/main/resources/template.txt .
cp src/main/resources/config.json .

# 放入 Excel 文件到 input/

# 运行
./run.sh
```

### 命令行方式（跨平台）

```bash
java -jar soil-moisture-hqt-1.0.0.jar \
  -b \
  -e input \
  -t template.txt \
  -c config.json \
  -o output \
  -m merged.HQT \
  --sheet-index 2
```
- ✅ 创建 input/output 目录
- ✅ 生成 template.txt 和 config.json

### 2. 准备 Excel 文件

将 Excel 文件放入 `input/` 目录：

```
input/
├── 安康 2026.4.20 墒情.xlsx
├── 安康 2026.4.30 墒情.xlsx
└── 安康 2026.5.10 墒情.xlsx
```

### 3. 运行处理

双击 `run.bat` 执行批量处理：

```batch
run.bat
```

### 4. 查看结果

处理完成后自动打开 `output/` 目录：

```
output/
├── 安康 2026.4.20 墒情.HQT
├── 安康 2026.4.30 墒情.HQT
└── 安康 2026.5.10 墒情.HQT

merged.HQT              # 合并文件（所有数据，空行分隔）
```

---

## 环境要求

- **JDK 17+**: https://adoptium.net/temurin/releases/?version=17
- **Maven 3.6+**: https://maven.apache.org/download.cgi（或使用项目自带 Maven Wrapper）

### 检查环境

```batch
# 检查 Java
java -version

# 检查 Maven
mvn -version
```

## 运行方式

### Windows 用户（推荐）

#### 方式 1：一键部署（首次使用）

双击 `deploy.bat` 或在命令行执行：

```batch
deploy.bat
```

自动完成：
- 检查 Java/Maven 环境
- 编译项目
- 创建必要目录和配置文件

#### 方式 2：运行批量处理

将 Excel 文件放入 `input/` 目录后，双击 `run.bat` 或执行：

```batch
run.bat
```

自动完成：
- 检查环境和必要文件
- 批量处理所有 Excel 文件
- 生成独立 HQT 文件和合并文件
- 自动打开 output 目录

### 命令行方式（跨平台）

#### 单个 Excel 文件处理

```bash
java -jar target/soil-moisture-hqt-1.0.0.jar \
  -e data.xlsx \
  -t template.txt \
  -c config.json \
  --sheet-index 2
```

#### 批量处理多个 Excel 文件

```bash
# 从 input/ 读取所有 Excel，输出到 output/，合并为 merged.HQT
java -jar target/soil-moisture-hqt-1.0.0.jar \
  -b \
  -e input \
  -t template.txt \
  -c config.json \
  -o output \
  -m merged.HQT \
  --sheet-index 2
```

**批量模式说明：**
- 自动扫描指定目录下的所有 `.xlsx` 和 `.xls` 文件
- 每个 Excel 文件生成独立的 `.HQT` 文件（文件名与 Excel 文件名一致）
- 输出目录由 `-o` 指定（可选，默认为 `output`）
- `-m` 可选，将所有输出合并为一个文件，文件之间用空行分隔

## 命令行参数

| 参数 | 说明 | 必需 | 默认值 |
|------|------|------|--------|
| `-e, --excel` | Excel 数据文件路径（批量模式为目录） | 是 | - |
| `-t, --text` | 待替换的文本模板 | 是 | - |
| `-c, --config` | 替换规则 JSON 配置 | 是 | - |
| `-o, --output` | 输出文件/目录路径 | 否 | 模板同名 `.hqt` |
| `-s, --sheet` | 指定 Excel 工作表名称 | 否 | 自动检测 |
| `--sheet-index` | 指定 Excel 工作表索引（从 0 开始） | 否 | 自动检测 |
| `-b, --batch` | 批量模式：处理目录下所有 Excel 文件 | 否 | 单文件模式 |
| `-m, --merge-output` | 批量模式：合并所有输出到单个文件 | 否 | 不合并 |

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

## 使用示例

### 单文件处理

```bash
java -jar target/soil-moisture-hqt-1.0.0.jar \
  -e data.xlsx \
  -t template.txt \
  -c config.json \
  --sheet-index 2
```

### 批量处理（推荐）

```bash
# 从 input/ 目录读取所有 Excel 文件，输出到 output/ 目录，并合并为 merged.HQT
java -jar target/soil-moisture-hqt-1.0.0.jar \
  -b \
  -e input \
  -t template.txt \
  -c config.json \
  -o output \
  -m merged.HQT \
  --sheet-index 2
```

输出：
- `output/file1.HQT` - 对应 `input/file1.xlsx`
- `output/file2.HQT` - 对应 `input/file2.xlsx`
- `merged.HQT` - 所有输出合并（空行分隔）

## 开发流程

### 修改代码后重新编译

```bash
mvn clean package
java -jar target/soil-moisture-hqt-1.0.0.jar -b -e . -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2
```

### 批量模式工作流程

1. 将所有 Excel 文件放到同一目录
2. 准备 template.txt 和 config.json
3. 运行批量处理命令
4. 检查 output/ 目录下的独立文件
5. 使用 merged.HQT 查看合并结果

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
