package com.example.excelreplacer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ConfigLoader {
    public static List<ReplaceRule> load(String configPath) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ReplaceRule>>(){}.getType();
        try (FileReader reader = new FileReader(configPath)) {
            List<ReplaceRule> rules = (List)gson.fromJson((Reader)reader, listType);
            if (rules == null) {
                throw new IllegalArgumentException("Config file is empty or invalid JSON format.");
            }
            validateRules(rules);
            return rules;
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read config file: " + configPath, e);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Invalid JSON syntax in config file: " + configPath, e);
        }
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
