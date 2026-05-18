/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

import com.example.excelreplacer.CliArgs;
import com.example.excelreplacer.CliParser;
import com.example.excelreplacer.ConfigLoader;
import com.example.excelreplacer.ExcelReader;
import com.example.excelreplacer.FileWriter;
import com.example.excelreplacer.ReplaceDetail;
import com.example.excelreplacer.ReplaceReport;
import com.example.excelreplacer.ReplaceRule;
import com.example.excelreplacer.TextReplacer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int exitCode = Main.run(args);
        System.exit(exitCode);
    }

    public static int run(String[] args) {
        try {
            CliArgs cliArgs = CliParser.parse(args);
            Main.validateInputFiles(cliArgs);
            String effectiveSheetName = cliArgs.sheetName();
            Integer effectiveSheetIndex = cliArgs.sheetIndex();
            if (effectiveSheetName != null) {
                effectiveSheetIndex = null;
            }
            List<ReplaceRule> rules = ConfigLoader.load(cliArgs.configPath());
            String textContent = Files.readString(Path.of(cliArgs.textPath(), new String[0]));
            try (ExcelReader reader = new ExcelReader(cliArgs.excelPath(), effectiveSheetName, effectiveSheetIndex);){
                if (effectiveSheetIndex != null) {
                    System.out.println("Using sheet index: " + effectiveSheetIndex);
                }
                TextReplacer replacer = new TextReplacer();
                String result = replacer.replace(textContent, rules, reader);
                FileWriter.write(result, cliArgs.outputPath());
                Main.printReport(replacer.getReport());
            }
            System.out.println("Output written to: " + cliArgs.outputPath());
            return 0;
        }
        catch (CliParser.HelpRequestedException e) {
            CliParser.printHelp();
            return 0;
        }
        catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            return 1;
        }
        catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private static void validateInputFiles(CliArgs cliArgs) {
        if (!Files.exists(Path.of(cliArgs.excelPath(), new String[0]), new LinkOption[0])) {
            throw new IllegalArgumentException("Excel file not found: " + cliArgs.excelPath());
        }
        if (!Files.exists(Path.of(cliArgs.textPath(), new String[0]), new LinkOption[0])) {
            throw new IllegalArgumentException("Text file not found: " + cliArgs.textPath());
        }
        if (!Files.exists(Path.of(cliArgs.configPath(), new String[0]), new LinkOption[0])) {
            throw new IllegalArgumentException("Config file not found: " + cliArgs.configPath());
        }
    }

    private static void printReport(ReplaceReport report) {
        System.out.println("\n=== Replacement Report ===");
        System.out.println("Total matches: " + report.totalMatches());
        System.out.println("Successful replacements: " + report.successfulReplacements());
        System.out.println("Skipped matches: " + report.skippedMatches());
        System.out.println("\nDetails:");
        for (ReplaceDetail detail : report.details()) {
            System.out.printf("  Pattern: %s | Position: %d | '%s' -> '%s'%n", detail.regex(), detail.position(), detail.oldValue(), detail.newValue());
        }
    }
}

