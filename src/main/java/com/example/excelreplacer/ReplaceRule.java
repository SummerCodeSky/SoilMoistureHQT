/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

public record ReplaceRule(int row, int col, String regexPattern, String format, String sheet) {
    public ReplaceRule(int row, int col, String regexPattern, String format) {
        this(row, col, regexPattern, format, null);
    }
}

