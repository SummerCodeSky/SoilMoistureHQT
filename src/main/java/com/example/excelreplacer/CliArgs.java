/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

public record CliArgs(String excelPath, String textPath, String configPath, String outputPath, String sheetName, Integer sheetIndex) {
    public CliArgs {
        if (sheetName != null && sheetIndex != null) {
            sheetIndex = null;
        }
    }
}

