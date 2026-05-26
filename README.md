# 土壤墒情批量处理工具

跨平台（Windows/Linux/macOS）的 Excel 批量处理工具，自动从 Excel 读取数据并生成 HQT 文件。

---

## 快速开始

### 方式一：使用预编译版本（推荐）

#### Windows 用户

1. **准备文件**：确保以下文件在同一目录
   - `run.bat` - 运行脚本
   - `build.bat` - 构建脚本（如需从源码构建）
   - `soil-moisture-hqt-1.0.0.jar` - 程序文件
   - `template.txt` - 文本模板
   - `config.json` - 替换规则配置

2. **放入 Excel 文件**：创建 `input/` 文件夹，放入需要处理的 Excel 文件
   ```
   工具目录/
   ├── run.bat
   ├── soil-moisture-hqt-1.0.0.jar
   ├── template.txt
   ├── config.json
   └── input/
       ├── 2026.4.20 墒情.xlsx
       ├── 2026.4.30 墒情.xlsx
       └── 2026.5.10 墒情.xlsx
   ```

3. **运行处理**：双击 **`run.bat`**

#### Linux/macOS 用户

1. **赋予执行权限**
   ```bash
   chmod +x run.sh build.sh
   ```

2. **放入 Excel 文件**：创建 `input/` 文件夹，放入需要处理的 Excel 文件

3. **运行处理**
   ```bash
   ./run.sh
   ```

### 方式二：从源码构建

#### Windows 系统

1. 安装 JDK 17+ 和 Maven
   - JDK: https://adoptium.net/temurin/releases/?version=17
   - Maven: https://maven.apache.org/download.cgi

2. 双击运行 **`build.bat`**

3. 构建完成后，双击 **`run.bat`** 运行

#### Linux/macOS 系统

1. 安装 JDK 17+ 和 Maven
   ```bash
   # macOS
   brew install openjdk@17 maven

   # Ubuntu/Debian
   sudo apt install openjdk-17-jdk maven

   # CentOS/RHEL
   sudo yum install java-17-openjdk maven
   ```

2. 运行构建脚本
   ```bash
   chmod +x build.sh
   ./build.sh
   ```

3. 运行程序
   ```bash
   ./run.sh
   ```

---

## 环境要求

| 平台 | 要求 |
|------|------|
| **Windows** | Windows 7/10/11，JDK 17+ |
| **Linux** | JDK 17+，glibc 2.17+ |
| **macOS** | macOS 10.15+，JDK 17+ |

检查 Java 是否安装：
```bash
java -version
```

---

## 配置文件

### template.txt

文本模板，占位符会被 Excel 数据替换：

```txt
618A0900，xx，1,1,{date},08:00:00,105,,,{station_codes},...
```

### config.json

替换规则配置：

```json
[
  {
    "row": 2,
    "col": 1,
    "regexPattern": "^((?:[^,]*,){4})[^,]+"
  },
  {
    "row": 2,
    "col": 2,
    "regexPattern": "^((?:[^,]*,){27})[^,]+"
  }
]
```

**参数说明：**
- `row`: Excel 行号（从 1 开始）
- `col`: Excel 列号（从 1 开始）
- `regexPattern`: 正则表达式匹配模式

---

## 输出说明

### output/ 目录

每个 Excel 文件生成独立的 HQT 文件：

```
output/
├── 2026.4.20 墒情.HQT
├── 2026.4.30 墒情.HQT
└── 2026.5.10 墒情.HQT
```

### Aresult.HQT

所有输出合并为一个文件，文件之间用空行分隔：

```
618A0900，xx，1,1,2026-04-20,...  ← 第一个文件内容
618 9800，xx，1,1,2026-04-20,...

618A0900，xx，1,1,2026-04-30,...  ← 空行分隔
618 9800，xx，1,1,2026-04-30,...

618A0900，xx，1,1,2026-05-10,...  ← 第三个文件内容
```

---

## 常见问题

### Q: 双击 run.bat 后闪退

**原因**：缺少必要文件或 Java 环境

**解决**：
1. 确保 `soil-moisture-hqt-1.0.0.jar` 存在
2. 确保 `template.txt` 和 `config.json` 存在
3. 确保 `input/` 目录有 Excel 文件
4. 命令行运行 `run.bat` 查看详细错误信息

### Q: 提示找不到 Java

**解决**：
1. 下载 JDK 17+：https://adoptium.net/
2. 安装后重启命令行
3. 运行 `java -version` 验证

### Q: 运行 ./run.sh 提示权限不足

**解决**：
```bash
chmod +x run.sh
./run.sh
```

### Q: 构建时提示找不到 Maven

**解决**：
- Windows: 下载 Maven 并配置 PATH 环境变量
- Linux: `sudo apt install maven` 或 `sudo yum install maven`
- macOS: `brew install maven`

### Q: 输出结果为空

**检查**：
1. Excel 文件是否有数据
2. `config.json` 的行号列号是否正确
3. 查看运行时的错误信息

### Q: 中文乱码

**解决**：
1. 使用 UTF-8 编码保存 `template.txt` 和 `config.json`
2. Windows 系统区域设置使用 UTF-8（设置 → 时间和语言 → 语言 → 管理语言设置 → 更改系统区域设置 → 勾选 Beta 版 UTF-8）

---

## 命令行参数

如需自定义处理，可使用命令行：

```bash
java -jar soil-moisture-hqt-1.0.0.jar [参数]

参数:
  -b, --batch              批量模式
  -e, --excel <目录>        Excel 文件目录
  -t, --text <文件>         文本模板文件
  -c, --config <文件>       配置文件
  -o, --output <目录>       输出目录
  -m, --merge-output <文件> 合并输出文件
  --sheet-index <数字>      Excel 工作表索引（从 0 开始）
```

示例：
```bash
java -jar soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m Aresult.HQT --sheet-index 2
```

---

## 项目结构

```
SoilMoistureHQT/
├── run.bat                          # Windows 运行脚本
├── run.sh                           # Linux/macOS 运行脚本
├── build.bat                        # Windows 构建脚本
├── build.sh                         # Linux/macOS 构建脚本
├── soil-moisture-hqt-1.0.0.jar      # 程序文件（构建后生成）
├── template.txt                     # 文本模板
├── config.json                      # 替换规则
├── pom.xml                          # Maven 构建配置
├── input/                           # 放入 Excel 文件
├── output/                          # 输出目录
├── Aresult.HQT                      # 合并文件
└── src/
    └── main/
        └── java/                    # Java 源代码
```

---

## 许可证

本项目仅供学习和内部使用。
