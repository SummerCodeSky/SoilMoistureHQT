package com.example.excelreplacer;

import java.util.HashMap;
import java.util.Map;

public class FuzzyMatcher {
    public static String match(String input, Map<String, String> enumMap) {
        if (input == null || input.isEmpty()) return "";
        
        // 1. 精确匹配优先（解决单字如“无”、“涝”等因 overlap<2 无法匹配的问题）
        if (enumMap.containsKey(input)) {
            return enumMap.get(input);
        }

        String bestKey = null;
        double maxRatio = -1.0;
        int maxOverlap = -1;

        for (String key : enumMap.keySet()) {
            int overlap = calculateOverlap(input, key);
            int minLen = Math.min(input.length(), key.length());
            double ratio = minLen > 0 ? (double) overlap / minLen : 0.0;

            // 2. 模糊匹配判定：绝对重叠数 >= 2 且 相对比例 >= 60%
            if (overlap >= 2 && ratio >= 0.6) {
                if (ratio > maxRatio || (ratio == maxRatio && overlap > maxOverlap)) {
                    maxRatio = ratio;
                    maxOverlap = overlap;
                    bestKey = key;
                }
            }
        }
        return bestKey != null ? enumMap.get(bestKey) : "";
    }

    private static int calculateOverlap(String s1, String s2) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s2.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        int count = 0;
        for (char c : s1.toCharArray()) {
            if (freq.getOrDefault(c, 0) > 0) {
                count++;
                freq.put(c, freq.get(c) - 1);
            }
        }
        return count;
    }
}
