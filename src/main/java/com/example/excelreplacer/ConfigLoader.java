package com.example.excelreplacer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ConfigLoader {
    public static List<ReplaceRule> load(String configPath) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<JsonObject>>(){}.getType();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(configPath), StandardCharsets.UTF_8)) {
            List<JsonObject> jsonList = gson.fromJson((java.io.Reader)reader, listType);
            if (jsonList == null) {
                throw new IllegalArgumentException("Config file is empty or invalid JSON format.");
            }
            List<ReplaceRule> rules = parseRules(jsonList);
            validateRules(rules);
            return rules;
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read config file: " + configPath, e);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Invalid JSON syntax in config file: " + configPath, e);
        }
    }

    private static List<ReplaceRule> parseRules(List<JsonObject> jsonList) {
        List<ReplaceRule> rules = new ArrayList<>();
        for (JsonObject json : jsonList) {
            int row = json.has("row") ? json.get("row").getAsInt() : 0;
            int col = json.has("col") ? json.get("col").getAsInt() : 0;
            String regexPattern = json.has("regexPattern") ? json.get("regexPattern").getAsString() : null;
            String format = json.has("format") ? json.get("format").getAsString() : null;
            String sheet = json.has("sheet") ? json.get("sheet").getAsString() : null;
            
            Map<String, String> enumMap = null;
            if (json.has("enumMap") && json.get("enumMap").isJsonObject()) {
                enumMap = new HashMap<>();
                JsonObject enumObj = json.get("enumMap").getAsJsonObject();
                for (Map.Entry<String, com.google.gson.JsonElement> entry : enumObj.entrySet()) {
                    enumMap.put(entry.getKey(), entry.getValue().getAsString());
                }
            }
            
            String timeRegex = json.has("timeRegex") ? json.get("timeRegex").getAsString() : null;
            int timeOccurrence = json.has("timeOccurrence") ? json.get("timeOccurrence").getAsInt() : 1;
            
            rules.add(new ReplaceRule(row, col, regexPattern, format, sheet, enumMap, timeRegex, timeOccurrence));
        }
        return rules;
    }

    private static void validateRules(List<ReplaceRule> rules) {
        for (int i = 0; i < rules.size(); ++i) {
            ReplaceRule rule = rules.get(i);
            if (rule.row() < 1) {
                throw new IllegalArgumentException(String.format("Rule %d: row must be >= 1, got %d", i + 1, rule.row()));
            }
            if (rule.col() < 1) {
                throw new IllegalArgumentException(String.format("Rule %d: col must be >= 1, got %d", i + 1, rule.col()));
            }
            if (rule.regexPattern() == null || rule.regexPattern().isEmpty()) {
                throw new IllegalArgumentException(String.format("Rule %d: regex pattern cannot be empty", i + 1));
            }
            try {
                Pattern.compile(rule.regexPattern());
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException(String.format("Rule %d: invalid regex pattern '%s'", i + 1, rule.regexPattern()), e);
            }
        }
    }
}
