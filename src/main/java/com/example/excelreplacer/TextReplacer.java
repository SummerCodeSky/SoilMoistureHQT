package com.example.excelreplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReplacer {
    private final List<ReplaceDetail> details;
    // 匹配模式: ^((?:[^,]*,){N})[^,]+ 提取N
    private static final Pattern INDEX_PATTERN = Pattern.compile("^\\(\\?:\\[\\^,\\]\\*,\\)\\{(\\d+)\\}");

    public TextReplacer() {
        this.details = new ArrayList<>();
    }

    public String replace(String textContent, List<ReplaceRule> rules, ExcelReader reader) {
        String[] lines = textContent.split("\n", -1);
        StringBuilder result = new StringBuilder();
        
        for (int lineIdx = 0; lineIdx < lines.length; lineIdx++) {
            System.out.println("[LINE] " + lineIdx + " head=" + lines[lineIdx].substring(0, Math.min(50, lines[lineIdx].length())));
            String line = lines[lineIdx];
            String replacedLine = processLine(lineIdx, line, rules, reader);
            result.append(replacedLine);
            if (lineIdx < lines.length - 1) {
                result.append("\n");
            }
        }
        
        return result.toString();
    }
    
    private boolean isRuleForLine(int lineIdx, int ruleRow) {
        // 模板行索引到 Excel 行范围的映射
        // Line 1: 石泉 (Shiquan) -> row 1-18
        // Line 2: 马池 (Machi) -> row 21-37
        // Line 3: 恒口 (Hengkou) -> row 58-75
        // Line 4: 长枪铺 (Changqiangpu) -> row 39-56
        if (lineIdx == 0) return ruleRow >= 1 && ruleRow <= 18;
        if (lineIdx == 1) return ruleRow >= 21 && ruleRow <= 37;
        if (lineIdx == 2) return ruleRow >= 58 && ruleRow <= 75;
        if (lineIdx == 3) return ruleRow >= 39 && ruleRow <= 56;
        return false;
    }
    
    private String processLine(int lineIdx, String line, List<ReplaceRule> rules, ExcelReader reader) {
        // 将行按逗号分割
        String[] fields = line.split(",", -1);
        boolean modified = false;
        
        for (ReplaceRule rule : rules) {
            if (!isRuleForLine(lineIdx, rule.row())) {
                // continue; // DEBUG
            } else {
                System.out.println("[DEBUG] LineIdx " + lineIdx + " matches rule row " + rule.row() + " (idx " + extractFieldIndex(rule.regexPattern()) + ")");
            }
            
            Object cellValue = reader.getCellValue(rule.row(), rule.col());
            String valStr = (cellValue == null) ? "NULL" : cellValue.toString().trim();
            if (lineIdx == 1 && rule.row() == 21) System.out.println("[DEBUG] LineIdx 1 rule 21 val=[" + valStr + "]");
            if (cellValue == null || valStr.isEmpty()) {
                continue;
            }
            
            String cellStr = cellValue.toString().trim();
            String replacement = cellStr;
            
            if (rule.enumMap() != null && !rule.enumMap().isEmpty()) {
                replacement = applyEnumMapping(cellStr, rule.enumMap());
            } else if ("time".equalsIgnoreCase(rule.format())) {
                replacement = extractTime(cellStr, rule.timeRegex(), rule.timeOccurrence());
            } else if ("YYYY-MM-DD".equalsIgnoreCase(rule.format())) {
                replacement = extractDate(cellStr);
            } else if ("time-only".equalsIgnoreCase(rule.format())) {
                replacement = extractTimeOnly(cellStr);
            }
            
            // 从正则中提取字段索引
            int fieldIndex = extractFieldIndex(rule.regexPattern());
            if (fieldIndex >= 0) {
                // 如果索引超出当前字段数组长度，扩展数组
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
        }
        
        if (modified) {
            String res = String.join(",", fields);
            System.out.println("[OUT] LineIdx " + lineIdx + " len=" + res.length() + " head=" + res.substring(0, Math.min(50, res.length())));
            return res;
        }
        return line;
    }
    
    // 从正则 ^((?:[^,]*,){N})[^,]+ 中提取 N（即要替换的字段索引）
    private int extractFieldIndex(String regexPattern) {
        // 匹配 {数字}
        Pattern p = Pattern.compile("\\{(\\d+)\\}");
        Matcher m = p.matcher(regexPattern);
        if (m.find()) {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    private String applyEnumMapping(String value, Map<String, String> enumMap) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        
        // 1. Exact match
        if (enumMap.containsKey(value)) {
            return enumMap.get(value);
        }
        
        // 2. Fuzzy match (contains)
        for (Map.Entry<String, String> entry : enumMap.entrySet()) {
            String key = entry.getKey();
            if (value.contains(key) || key.contains(value)) {
                return entry.getValue();
            }
        }
        
        // 3. Return empty string if no match
        return "";
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
        Pattern p = Pattern.compile("(\\d{1,2})\\s*时\\s*(\\d{1,2})\\s*分");
        Matcher m = p.matcher(cellStr);
        if (m.find()) {
            int hour = Integer.parseInt(m.group(1));
            int minute = Integer.parseInt(m.group(2));
            return String.format("%02d:%02d:00", hour, minute);
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
        
        // occurrence 1 means last one, 2 means 2nd last, etc.
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
