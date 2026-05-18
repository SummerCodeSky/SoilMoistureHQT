package com.example.excelreplacer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int exitCode = Main.run(args);
        System.exit(exitCode);
    }

    public static int run(String[] args) {
        try {
            CliArgs cliArgs = CliParser.parse(args);

            if (cliArgs.batchMode()) {
                return runBatchMode(cliArgs);
            } else {
                return runSingleMode(cliArgs);
            }
        } catch (CliParser.HelpRequestedException e) {
            CliParser.printHelp();
            return 0;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Cause: " + e.getCause().getMessage());
            }
            return 1;
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private static int runSingleMode(CliArgs cliArgs) {
        validateInputFiles(cliArgs);
        String effectiveSheetName = cliArgs.sheetName();
        Integer effectiveSheetIndex = cliArgs.sheetIndex();
        if (effectiveSheetName != null) {
            effectiveSheetIndex = null;
        }
        List<ReplaceRule> rules = ConfigLoader.load(cliArgs.configPath());
        String textContent;
        try {
            textContent = Files.readString(Path.of(cliArgs.textPath()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Cannot read text file: " + cliArgs.textPath(), e);
        }
        try (ExcelReader reader = new ExcelReader(cliArgs.excelPath(), effectiveSheetName, effectiveSheetIndex)) {
            if (effectiveSheetIndex != null) {
                System.out.println("Using sheet index: " + effectiveSheetIndex);
            }
            TextReplacer replacer = new TextReplacer();
            String result = replacer.replace(textContent, rules, reader);
            FileWriter.write(result, cliArgs.outputPath());
            printReport(replacer.getReport());
            System.out.println("Output written to: " + cliArgs.outputPath());
            return 0;
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    private static int runBatchMode(CliArgs cliArgs) throws IOException {
        File excelDir = new File(cliArgs.excelPath());
        if (!excelDir.exists() || !excelDir.isDirectory()) {
            throw new IllegalArgumentException("Excel path must be a directory in batch mode: " + cliArgs.excelPath());
        }

        File[] excelFiles = excelDir.listFiles((dir, name) -> name.endsWith(".xlsx") || name.endsWith(".xls"));
        if (excelFiles == null || excelFiles.length == 0) {
            throw new IllegalArgumentException("No Excel files found in directory: " + cliArgs.excelPath());
        }

        System.out.println("Found " + excelFiles.length + " Excel file(s) in directory: " + cliArgs.excelPath());

        List<ReplaceRule> rules = ConfigLoader.load(cliArgs.configPath());
        String textContent = Files.readString(Path.of(cliArgs.textPath()));

        File outputDir = new File(cliArgs.outputPath());
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        List<String> individualOutputs = new ArrayList<>();
        List<String> processedFiles = new ArrayList<>();

        for (File excelFile : excelFiles) {
            String baseName = getBaseName(excelFile.getName());
            String individualOutputPath = new File(outputDir, baseName + ".HQT").getPath();

            System.out.println("Processing: " + excelFile.getName());
            
            try (ExcelReader reader = new ExcelReader(excelFile.getPath(), cliArgs.sheetName(), cliArgs.sheetIndex())) {
                TextReplacer replacer = new TextReplacer();
                String result = replacer.replace(textContent, rules, reader);
                FileWriter.write(result, individualOutputPath);
                
                individualOutputs.add(result);
                processedFiles.add(excelFile.getName());
                
                ReplaceReport report = replacer.getReport();
                System.out.println("  -> " + baseName + ".HQT (" + report.successfulReplacements() + " replacements)");
            } catch (Exception e) {
                System.err.println("  ERROR: " + e.getMessage());
            }
        }

        System.out.println("\n=== Batch Summary ===");
        System.out.println("Processed: " + processedFiles.size() + " file(s)");
        for (String fileName : processedFiles) {
            System.out.println("  - " + fileName);
        }

        if (cliArgs.mergeOutputPath() != null && !individualOutputs.isEmpty()) {
            System.out.println("\nMerging " + individualOutputs.size() + " output(s) into: " + cliArgs.mergeOutputPath());
            StringBuilder merged = new StringBuilder();
            for (int i = 0; i < individualOutputs.size(); i++) {
                if (i > 0) {
                    merged.append("\n");
                }
                merged.append(individualOutputs.get(i));
            }
            FileWriter.write(merged.toString(), cliArgs.mergeOutputPath());
            System.out.println("Merge complete!");
        }

        return 0;
    }

    private static String getBaseName(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
    }

    private static void validateInputFiles(CliArgs cliArgs) {
        if (!Files.exists(Path.of(cliArgs.excelPath()), LinkOption.NOFOLLOW_LINKS)) {
            throw new IllegalArgumentException("Excel file not found: " + cliArgs.excelPath());
        }
        if (!Files.exists(Path.of(cliArgs.textPath()), LinkOption.NOFOLLOW_LINKS)) {
            throw new IllegalArgumentException("Text file not found: " + cliArgs.textPath());
        }
        if (!Files.exists(Path.of(cliArgs.configPath()), LinkOption.NOFOLLOW_LINKS)) {
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
            System.out.printf("  Pattern: %s | Position: %d | '%s' -> '%s'%n", 
                detail.regex(), detail.position(), detail.oldValue(), detail.newValue());
        }
    }
}
