package com.example.excelreplacer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextReplacer {
    private final List<ReplaceDetail> details;

    public TextReplacer() {
        this.details = new ArrayList<>();
    }

    public String replace(String textContent, List<ReplaceRule> rules, ExcelReader reader) {
        String result = textContent;
        for (ReplaceRule rule : rules) {
            Object cellValue = reader.getCellValue(rule.row(), rule.col());
            if (cellValue == null) {
                continue;
            }

            String cellStr = cellValue.toString().trim();
            String replacement = cellStr;

            if (rule.enumMap() != null && !rule.enumMap().isEmpty()) {
                replacement = applyEnumMapping(cellStr, rule.enumMap());
            } else if ("time".equalsIgnoreCase(rule.format())) {
                replacement = extractTime(cellStr, rule.timeRegex(), rule.timeOccurrence());
            }

            Pattern pattern = Pattern.compile(rule.regexPattern());
            Matcher matcher = pattern.matcher(result);
            
            if (matcher.find()) {
                String oldValue = matcher.group();
                int position = matcher.start();
                result = matcher.replaceFirst(replacement);
                details.add(new ReplaceDetail(rule.regexPattern(), oldValue, replacement, position));
            }
        }

        return result;
    }

    private String applyEnumMapping(String value, Map<String, String> enumMap) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return enumMap.getOrDefault(value, value);
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
