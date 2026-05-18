@echo off
chcp 65001 >nul
echo ========================================
echo   项目初始化脚本
echo ========================================
echo.

REM 创建必要目录
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

REM 创建示例 template.txt
if not exist "template.txt" (
    (
        echo 618A0900,石泉,1,1,{date},08:00:00,105,,,{station_codes},,,,,,,,,,{moisture_10_values},,,,,,,,,,{moisture_20_values},,,,,,,,,,{temp_10_values},,,,,,,,,,{cond_10_values},,,,,,,,,,{ph_values},,,,,,,,,,{orp_values},,,,,,,,,,{do_values},,,,,,,,,,{turbidity},,,,,{flag},,,,{quality},{flow},{temp_air},3,3,{wind},{rain},,,,,,,,,6,,,1,{water_level},{flow_rate},{battery},,,,,1,,,,,,,安康市，陕西省，610900,{rain_1h},{rain_3h},{rain_6h},,,,30.7,30.7,30.7,,,
    ) > template.txt
    echo [完成] 创建 template.txt
) else (
    echo [跳过] template.txt 已存在
)

REM 创建示例 config.json
if not exist "config.json" (
    (
        echo [
        echo   {"row":2,"col":1,"regexPattern":"^((?:[^,]*,){4})[^,]+"},
        echo   {"row":2,"col":2,"regexPattern":"^((?:[^,]*,){27})[^,]+"},
        echo   {"row":2,"col":3,"regexPattern":"^((?:[^,]*,){28})[^,]+"},
        echo   {"row":2,"col":4,"regexPattern":"^((?:[^,]*,){29})[^,]+"},
        echo   {"row":2,"col":5,"regexPattern":"^((?:[^,]*,){30})[^,]+"},
        echo   {"row":2,"col":6,"regexPattern":"^((?:[^,]*,){31})[^,]+"},
        echo   {"row":2,"col":7,"regexPattern":"^((?:[^,]*,){32})[^,]+"},
        echo   {"row":2,"col":8,"regexPattern":"^((?:[^,]*,){33})[^,]+"},
        echo   {"row":2,"col":9,"regexPattern":"^((?:[^,]*,){34})[^,]+"},
        echo   {"row":2,"col":10,"regexPattern":"^((?:[^,]*,){35})[^,]+"},
        echo   {"row":2,"col":11,"regexPattern":"^((?:[^,]*,){36})[^,]+"},
        echo   {"row":2,"col":12,"regexPattern":"^((?:[^,]*,){37})[^,]+"},
        echo   {"row":2,"col":13,"regexPattern":"^((?:[^,]*,){38})[^,]+"},
        echo   {"row":2,"col":14,"regexPattern":"^((?:[^,]*,){39})[^,]+"},
        echo   {"row":2,"col":15,"regexPattern":"^((?:[^,]*,){40})[^,]+"},
        echo   {"row":2,"col":16,"regexPattern":"^((?:[^,]*,){41})[^,]+"},
        echo   {"row":2,"col":17,"regexPattern":"^((?:[^,]*,){42})[^,]+"},
        echo   {"row":2,"col":18,"regexPattern":"^((?:[^,]*,){43})[^,]+"},
        echo   {"row":2,"col":19,"regexPattern":"^((?:[^,]*,){44})[^,]+"},
        echo   {"row":2,"col":20,"regexPattern":"^((?:[^,]*,){45})[^,]+"},
        echo   {"row":2,"col":21,"regexPattern":"^((?:[^,]*,){46})[^,]+"},
        echo   {"row":2,"col":22,"regexPattern":"^((?:[^,]*,){47})[^,]+"},
        echo   {"row":2,"col":23,"regexPattern":"^((?:[^,]*,){48})[^,]+"},
        echo   {"row":2,"col":24,"regexPattern":"^((?:[^,]*,){49})[^,]+"},
        echo   {"row":2,"col":25,"regexPattern":"^((?:[^,]*,){50})[^,]+"},
        echo   {"row":2,"col":26,"regexPattern":"^((?:[^,]*,){51})[^,]+"},
        echo   {"row":2,"col":27,"regexPattern":"^((?:[^,]*,){52})[^,]+"},
        echo   {"row":2,"col":28,"regexPattern":"^((?:[^,]*,){53})[^,]+"},
        echo   {"row":2,"col":29,"regexPattern":"^((?:[^,]*,){54})[^,]+"},
        echo   {"row":2,"col":30,"regexPattern":"^((?:[^,]*,){55})[^,]+"},
        echo   {"row":2,"col":31,"regexPattern":"^((?:[^,]*,){56})[^,]+"},
        echo   {"row":2,"col":32,"regexPattern":"^((?:[^,]*,){57})[^,]+"},
        echo   {"row":2,"col":33,"regexPattern":"^((?:[^,]*,){58})[^,]+"},
        echo   {"row":2,"col":34,"regexPattern":"^((?:[^,]*,){59})[^,]+"},
        echo   {"row":2,"col":35,"regexPattern":"^((?:[^,]*,){60})[^,]+"},
        echo   {"row":2,"col":36,"regexPattern":"^((?:[^,]*,){61})[^,]+"},
        echo   {"row":2,"col":37,"regexPattern":"^((?:[^,]*,){62})[^,]+"},
        echo   {"row":2,"col":38,"regexPattern":"^((?:[^,]*,){63})[^,]+"},
        echo   {"row":2,"col":39,"regexPattern":"^((?:[^,]*,){64})[^,]+"},
        echo   {"row":2,"col":40,"regexPattern":"^((?:[^,]*,){65})[^,]+"},
        echo   {"row":2,"col":41,"regexPattern":"^((?:[^,]*,){66})[^,]+"},
        echo   {"row":2,"col":42,"regexPattern":"^((?:[^,]*,){67})[^,]+"},
        echo   {"row":2,"col":43,"regexPattern":"^((?:[^,]*,){68})[^,]+"},
        echo   {"row":2,"col":44,"regexPattern":"^((?:[^,]*,){69})[^,]+"},
        echo   {"row":2,"col":45,"regexPattern":"^((?:[^,]*,){70})[^,]+"},
        echo   {"row":2,"col":46,"regexPattern":"^((?:[^,]*,){71})[^,]+"},
        echo   {"row":2,"col":47,"regexPattern":"^((?:[^,]*,){72})[^,]+"},
        echo   {"row":2,"col":48,"regexPattern":"^((?:[^,]*,){73})[^,]+"},
        echo   {"row":2,"col":49,"regexPattern":"^((?:[^,]*,){74})[^,]+"},
        echo   {"row":2,"col":50,"regexPattern":"^((?:[^,]*,){75})[^,]+"},
        echo   {"row":2,"col":51,"regexPattern":"^((?:[^,]*,){76})[^,]+"},
        echo   {"row":2,"col":52,"regexPattern":"^((?:[^,]*,){77})[^,]+"},
        echo   {"row":2,"col":53,"regexPattern":"^((?:[^,]*,){78})[^,]+"},
        echo   {"row":2,"col":54,"regexPattern":"^((?:[^,]*,){79})[^,]+"},
        echo   {"row":2,"col":55,"regexPattern":"^((?:[^,]*,){80})[^,]+"},
        echo   {"row":2,"col":56,"regexPattern":"^((?:[^,]*,){81})[^,]+"},
        echo   {"row":2,"col":57,"regexPattern":"^((?:[^,]*,){82})[^,]+"},
        echo   {"row":2,"col":58,"regexPattern":"^((?:[^,]*,){83})[^,]+"},
        echo   {"row":2,"col":59,"regexPattern":"^((?:[^,]*,){84})[^,]+"},
        echo   {"row":2,"col":60,"regexPattern":"^((?:[^,]*,){85})[^,]+"},
        echo   {"row":2,"col":61,"regexPattern":"^((?:[^,]*,){86})[^,]+"},
        echo   {"row":2,"col":62,"regexPattern":"^((?:[^,]*,){87})[^,]+"},
        echo   {"row":2,"col":63,"regexPattern":"^((?:[^,]*,){88})[^,]+"}
        echo ]
    ) > config.json
    echo [完成] 创建 config.json
) else (
    echo [跳过] config.json 已存在
)

echo.
echo ========================================
echo   初始化完成!
echo ========================================
echo.
echo 使用说明:
echo   1. 将 Excel 文件放入 input 目录
echo   2. 运行 run.bat 开始处理
echo   3. 结果在 output 目录和 merged.HQT
echo.

pause
