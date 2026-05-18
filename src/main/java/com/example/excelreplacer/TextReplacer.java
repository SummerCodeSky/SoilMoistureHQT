/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

import com.example.excelreplacer.ExcelReader;
import com.example.excelreplacer.ReplaceDetail;
import com.example.excelreplacer.ReplaceReport;
import com.example.excelreplacer.ReplaceRule;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReplacer {
    private ReplaceReport report;

    public String replace(String content, List<ReplaceRule> rules, ExcelReader reader) {
        ArrayList<ReplaceDetail> details = new ArrayList<ReplaceDetail>();
        int totalMatches = 0;
        int successfulReplacements = 0;
        int skippedMatches = 0;
        CharSequence[] parts = content.split(",", -1);
        for (ReplaceRule rule : rules) {
            String rawValue;
            try {
                rawValue = reader.getCellValue(rule.sheet(), rule.row(), rule.col());
            }
            catch (Exception e) {
                System.err.println("Warning: " + e.getMessage() + ". Skipping rule: row=" + rule.row() + ", col=" + rule.col() + (String)(rule.sheet() != null ? ", sheet=" + rule.sheet() : ""));
                ++skippedMatches;
                continue;
            }
            String replacementValue = rule.format() != null && !rule.format().isEmpty() ? this.formatCellValue(rawValue, rule.format()) : rawValue;
            Pattern indexPattern = Pattern.compile("\\{([0-9]+)\\}\\)\\[\\^,\\]\\+");
            Matcher indexMatcher = indexPattern.matcher(rule.regexPattern());
            if (indexMatcher.find()) {
                int index = Integer.parseInt(indexMatcher.group(1));
                if (index < parts.length) {
                    CharSequence oldValue = parts[index];
                    details.add(new ReplaceDetail(rule.regexPattern(), (String)oldValue, replacementValue, index));
                    parts[index] = replacementValue;
                    ++successfulReplacements;
                    ++totalMatches;
                    continue;
                }
                System.err.println("Warning: Index " + index + " out of bounds (total fields: " + parts.length + ")");
                continue;
            }
            System.err.println("Warning: Cannot parse index from regex: " + rule.regexPattern());
        }
        this.report = new ReplaceReport(totalMatches, successfulReplacements, skippedMatches, details);
        return String.join((CharSequence)",", parts);
    }

    private String formatCellValue(String value, String format) {
        Pattern datePattern;
        Matcher matcher;
        if (value == null || value.isEmpty()) {
            return value;
        }
        if ("YYYY-MM-DD".equals(format) && (matcher = (datePattern = Pattern.compile("(\\d{4})\\s*\u5e74\\s*(\\d{1,2})\\s*\u6708\\s*(\\d{1,2})\\s*\u65e5")).matcher(value)).find()) {
            String year = matcher.group(1);
            String month = String.format("%02d", Integer.parseInt(matcher.group(2)));
            String day = String.format("%02d", Integer.parseInt(matcher.group(3)));
            return year + "-" + month + "-" + day;
        }
        return value;
    }

    public ReplaceReport getReport() {
        if (this.report == null) {
            throw new IllegalStateException("No replacement has been performed yet.");
        }
        return this.report;
    }
}

