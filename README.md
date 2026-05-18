# 土壤墒情批量处理工具

Windows 环境下的 Excel 批量处理工具，自动从 Excel 读取数据并生成 HQT 文件。

---

## 快速开始

### 1. 准备文件

将以下文件放在同一目录：

- `run.bat` - 运行脚本
- `soil-moisture-hqt-1.0.0.jar` - 程序文件
- `template.txt` - 文本模板
- `config.json` - 替换规则配置

### 2. 放入 Excel 文件

创建 `input/` 文件夹，放入需要处理的 Excel 文件：

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

### 3. 运行处理

双击 **`run.bat`**

处理完成后自动生成：
- `output/` - 独立的 HQT 文件（每个 Excel 对应一个）
- `merged.HQT` - 合并所有输出（空行分隔）

---

## 环境要求

- **Windows** 7/10/11
- **JDK 17+**：https://adoptium.net/temurin/releases/?version=17

检查 Java 是否安装：
```batch
java -version
```

---

## 配置文件

### template.txt

文本模板，占位符会被 Excel 数据替换：

```txt
618A0900，石泉，1,1,{date},08:00:00,105,,,{station_codes},...
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
├── 安康 2026.4.20 墒情.HQT
├── 安康 2026.4.30 墒情.HQT
└── 安康 2026.5.10 墒情.HQT
```

### merged.HQT

所有输出合并为一个文件，文件之间用空行分隔：

```
618A0900，石泉，1,1,2026-04-20,...  ← 第一个文件内容
618 9800，马池，1,1,2026-04-20,...

618A0900，石泉，1,1,2026-04-30,...  ← 空行分隔
618 9800，马池，1,1,2026-04-30,...

618A0900，石泉，1,1,2026-05-10,...  ← 第三个文件内容
```

---

## 常见问题

### Q: 双击 run.bat 后闪退

**原因**：缺少必要文件

**解决**：
1. 确保 `soil-moisture-hqt-1.0.0.jar` 存在
2. 确保 `template.txt` 和 `config.json` 存在
3. 确保 `input/` 目录有 Excel 文件

### Q: 提示找不到 Java

**解决**：
1. 下载 JDK 17+：https://adoptium.net/
2. 安装后重启命令行
3. 运行 `java -version` 验证

### Q: 输出结果为空

**检查**：
1. Excel 文件是否有数据
2. `config.json` 的行号列号是否正确
3. 查看 run.bat 运行的错误信息

### Q: 中文乱码

**解决**：
1. 使用 UTF-8 编码保存 `template.txt` 和 `config.json`
2. Windows 系统区域设置使用 UTF-8

---

## 命令行参数

如需自定义处理，可使用命令行：

```batch
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
```batch
java -jar soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2
```

---

## 项目结构

```
SoilMoistureHQT/
├── run.bat                          # 运行脚本
├── soil-moisture-hqt-1.0.0.jar      # 程序文件
├── template.txt                     # 文本模板
├── config.json                      # 替换规则
├── input/                           # 放入 Excel 文件
├── output/                          # 输出目录
├── merged.HQT                       # 合并文件
└── src/
    └── main/
        ├── java/                    # Java 源代码
        └── resources/               # 资源文件
```

---

