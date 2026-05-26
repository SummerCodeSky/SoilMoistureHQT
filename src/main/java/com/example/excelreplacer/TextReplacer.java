package com.example.excelreplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReplacer {
    private final List<ReplaceDetail> details;
    private static final Pattern INDEX_PATTERN = Pattern.compile("^\\(\\?:\\[\\^,\\]\\*,\\)\\{(\\d+)\\}");

    public TextReplacer() {
        this.details = new ArrayList<>();
    }

    public String replace(String textContent, List<ReplaceRule> rules, ExcelReader reader) {
        String cleanContent = textContent.replace("\r", "");
        String[] lines = cleanContent.split("\n", -1);
        StringBuilder result = new StringBuilder();
        
        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            String line = lines[lineIdx];
            String replacedLine = processLine(lineIdx, line, rules, reader);
            result.append(replacedLine);
            if (lineIdx < lines.length - 1) {
                result.append("\n");
            }
        }
        
        return result.toString();
    }
    
    private int getLineOffset(int lineIdx) {
        return lineIdx * 192;
    }

    private String processLine(int lineIdx, String line, List<ReplaceRule> rules, ExcelReader reader) {
        String[] fields = line.split(",", -1);
        boolean modified = false;
        int lineOffset = getLineOffset(lineIdx);
        int nextLineOffset = lineOffset + 192;
        
        for (ReplaceRule rule : rules) {
            int configIndex = extractFieldIndex(rule.regexPattern());
            
            if (configIndex < lineOffset || configIndex >= nextLineOffset) {
                continue;
            }
            
            Object cellValue = reader.getCellValue(rule.row(), rule.col());
            String valStr = (cellValue == null) ? "NULL" : cellValue.toString().trim();
            if (cellValue == null || valStr.isEmpty()) {
                continue;
            }
            
            String cellStr = cellValue.toString().trim();
            String replacement = cellStr;
            
            if (rule.enumMap() != null && !rule.enumMap().isEmpty()) {
                replacement = applyEnumMapping(cellStr, rule.enumMap());
                
                // 特殊规则：主要作物 (142, 334, 526, 718) 匹配失败时默认为 "d" (其他)
                if (replacement.isEmpty()) {
                    int currentFieldIndex = extractFieldIndex(rule.regexPattern());
                    if (currentFieldIndex == 142 || currentFieldIndex == 334 || currentFieldIndex == 526 || currentFieldIndex == 718) {
                        replacement = "d";
                    }
                }
            } else if ("time".equalsIgnoreCase(rule.format())) {
                replacement = extractTime(cellStr, rule.timeRegex(), rule.timeOccurrence());
            } else if ("YYYY-MM-DD".equalsIgnoreCase(rule.format())) {
                replacement = extractDate(cellStr);
            } else if ("time-only".equalsIgnoreCase(rule.format())) {
                replacement = extractTimeOnly(cellStr);
            }
            
            int fieldIndex = configIndex - lineOffset;
            
            if (fieldIndex >= fields.length) {
                String[] newFields = new String[fieldIndex + 1];
                System.arraycopy(fields, 0, newFields, 0, fields.length);
                for (int i = fields.length; i <= fieldIndex; i++) {
                    newFields[i] = "";
                }
                fields = newFields;
            }
            
            String oldValue = fields[fieldIndex];
            if (!oldValue.equals(replacement)) {
                fields[fieldIndex] = replacement;
                modified = true;
                details.add(new ReplaceDetail(rule.regexPattern(), oldValue, replacement, fieldIndex));
            }
        }
        
        if (modified) {
            return String.join(",", fields);
        }
        return line;
    }
    
    private int extractFieldIndex(String regexPattern) {
        Pattern p = Pattern.compile("\\{(\\d+)\\}");
        Matcher m = p.matcher(regexPattern);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    private String applyEnumMapping(String value, Map<String, String> enumMap) {
        if (value == null) return "";
        return FuzzyMatcher.match(value, enumMap);
    }

    private String extractDate(String cellStr) {
        Pattern p = Pattern.compile("(\\d{4})\\s*年\\s*(\\d{1,2})\\s*月\\s*(\\d{1,2})\\s*日");
        Matcher m = p.matcher(cellStr);
        if (m.find()) {
            int year = Integer.parseInt(m.group(1));
            int month = Integer.parseInt(m.group(2));
            int day = Integer.parseInt(m.group(3));
            return String.format("%04d-%02d-%02d", year, month, day);
        }
        return "";
    }

    private String extractTimeOnly(String cellStr) {
        int hongganIdx = cellStr.indexOf("烘干");
        String samplePart = hongganIdx >= 0 ? cellStr.substring(0, hongganIdx) : cellStr;
        
        Pattern pColon = Pattern.compile("(\\d{1,2})[：:](\\d{1,2})");
        Matcher mColon = pColon.matcher(samplePart);
        if (mColon.find()) {
            int hour = Integer.parseInt(mColon.group(1));
            int minute = Integer.parseInt(mColon.group(2));
            return String.format("%02d:%02d:00", hour, minute);
        }
        
        Pattern p = Pattern.compile("(\\d{1,2})\\s*时\\s*(\\d{1,2})\\s*分");
        Matcher m = p.matcher(samplePart);
        if (m.find()) {
            int hour = Integer.parseInt(m.group(1));
            int minute = Integer.parseInt(m.group(2));
            return String.format("%02d:%02d:00", hour, minute);
        }
        
        Pattern pHourOnly = Pattern.compile("(\\d{1,2})\\s*时");
        Matcher mHour = pHourOnly.matcher(samplePart);
        if (mHour.find()) {
            int hour = Integer.parseInt(mHour.group(1));
            return String.format("%02d:00:00", hour);
        }
        
        return "00:00:00";
    }

    private String extractTime(String cellStr, String timeSlotRegex, int occurrence) {
        Pattern p = Pattern.compile("(\\d+)\\s*时.*?(\\d+)\\s*分");
        Matcher m = p.matcher(cellStr);
        List<int[]> matches = new ArrayList<>();
        while (m.find()) {
            matches.add(new int[]{Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2))});
        }
        
        if (matches.isEmpty()) {
            return "00:00:00";
        }
        
        int idx = matches.size() - occurrence;
        if (idx < 0) idx = 0;
        
        int[] pair = matches.get(idx);
        return String.format("%02d:%02d:00", pair[0], pair[1]);
    }

    public ReplaceReport getReport() {
        return new ReplaceReport(details.size(), details.size(), 0, details);
    }

    public List<ReplaceDetail> getDetails() {
        return details;
    }
}
