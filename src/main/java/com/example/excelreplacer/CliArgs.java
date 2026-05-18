package com.example.excelreplacer;

public record CliArgs(
    String excelPath,
    String textPath,
    String configPath,
    String outputPath,
    String sheetName,
    Integer sheetIndex,
    boolean batchMode,
    String mergeOutputPath
) {
    public CliArgs {
        if (sheetName != null && sheetIndex != null) {
            sheetIndex = null;
        }
    }
}
