package com.example.excelreplacer;

import java.util.Map;

public record ReplaceRule(
    int row,
    int col,
    String regexPattern,
    String format,
    String sheet,
    Map<String, String> enumMap,
    String timeRegex,
    int timeOccurrence
) {
    public ReplaceRule(int row, int col, String regexPattern, String format) {
        this(row, col, regexPattern, format, null, null, null, 1);
    }

    public ReplaceRule(int row, int col, String regexPattern, String format, Map<String, String> enumMap) {
        this(row, col, regexPattern, format, null, enumMap, null, 1);
    }
}
