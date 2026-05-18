#!/bin/bash
echo "Checking files..."
[ ! -d "input" ] && echo "ERROR: input folder not found" && exit 1
[ ! -f "template.txt" ] && echo "ERROR: template.txt not found" && exit 1
[ ! -f "config.json" ] && echo "ERROR: config.json not found" && exit 1
[ ! -f "soil-moisture-hqt-1.0.0.jar" ] && echo "ERROR: jar not found" && exit 1
echo "Processing..."
java -jar soil-moisture-hqt-1.0.0.jar -b -e input -t template.txt -c config.json -o output -m merged.HQT --sheet-index 2
