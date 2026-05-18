/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

import com.example.excelreplacer.CliArgs;

public class CliParser {
    public static CliArgs parse(String[] args) {
        String excelPath = null;
        String textPath = null;
        String configPath = null;
        Object outputPath = null;
        String sheetName = null;
        Integer sheetIndex = null;
        block26: for (int i = 0; i < args.length; ++i) {
            switch (args[i]) {
                case "--help": 
                case "-h": {
                    throw new HelpRequestedException();
                }
                case "--excel": 
                case "-e": {
                    if (i + 1 < args.length) {
                        excelPath = args[++i];
                        continue block26;
                    }
                    throw new IllegalArgumentException("Missing value for --excel");
                }
                case "--text": 
                case "-t": {
                    if (i + 1 < args.length) {
                        textPath = args[++i];
                        continue block26;
                    }
                    throw new IllegalArgumentException("Missing value for --text");
                }
                case "--config": 
                case "-c": {
                    if (i + 1 < args.length) {
                        configPath = args[++i];
                        continue block26;
                    }
                    throw new IllegalArgumentException("Missing value for --config");
                }
                case "--output": 
                case "-o": {
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                        continue block26;
                    }
                    throw new IllegalArgumentException("Missing value for --output");
                }
                case "--sheet": 
                case "-s": {
                    if (i + 1 < args.length) {
                        sheetName = args[++i];
                        continue block26;
                    }
                    throw new IllegalArgumentException("Missing value for --sheet");
                }
                case "--sheet-index": {
                    if (i + 1 < args.length) {
                        try {
                            sheetIndex = Integer.parseInt(args[++i]);
                            continue block26;
                        }
                        catch (NumberFormatException e) {
                            throw new IllegalArgumentException("--sheet-index must be a number");
                        }
                    }
                    throw new IllegalArgumentException("Missing value for --sheet-index");
                }
                default: {
                    throw new IllegalArgumentException("Unknown argument: " + args[i]);
                }
            }
        }
        if (excelPath == null || textPath == null || configPath == null) {
            throw new IllegalArgumentException("Missing required arguments. Required: --excel, --text, --config. Use --help for usage information.");
        }
        if (outputPath == null) {
            int lastSep = textPath.lastIndexOf(47);
            if (lastSep == -1) {
                lastSep = textPath.lastIndexOf(92);
            }
            outputPath = lastSep != -1 ? textPath.substring(0, lastSep + 1) + "SoilMoisture.HQT" : "SoilMoisture.HQT";
        }
        return new CliArgs(excelPath, textPath, configPath, (String)outputPath, sheetName, sheetIndex);
    }

    public static void printHelp() {
        System.out.println("Excel Text Replacer - Replace text content using Excel cell values and regex patterns\n\nUsage:\n  java -jar excel-text-replacer.jar [OPTIONS]\n\nRequired Options:\n  -e, --excel <path>    Path to Excel file (.xlsx)\n  -t, --text <path>     Path to text file to be replaced\n  -c, --config <path>   Path to JSON config file\n\nOptional Options:\n  -o, --output <path>   Path to output file (default: <text>.hqt)\n   -s, --sheet <name>    Sheet name in Excel file (default: first sheet with data)\n   --sheet-index <idx>   Select sheet by 0-based index\n   -h, --help            Show this help message\n\nConfig File Format (JSON):\n  [\n    {\n      \"row\": 2,\n      \"col\": 1,\n      \"regex\": \"\\\\{name\\\\}\"\n    }\n  ]\n\nNote: row and col are 1-based indexing.\n");
    }

    public static class HelpRequestedException
    extends RuntimeException {
        public HelpRequestedException() {
            super("Help requested");
        }
    }
}

