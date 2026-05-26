/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.io.BufferedWriter;

public class FileWriter {
    public static void write(String content, String outputPath, String encoding) {
        Charset charset;
        try {
            charset = Charset.forName(encoding);
        }
        catch (Exception e) {
            System.err.println("Warning: Invalid encoding '" + encoding + "', using UTF-8 instead.");
            charset = StandardCharsets.UTF_8;
        }
        try {
            Path path = Path.of(outputPath, new String[0]);
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent, new LinkOption[0])) {
                Files.createDirectories(parent, new FileAttribute[0]);
            }
            
            // Always write UTF-8 BOM for compatibility
            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
            byte[] bom = new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF};
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(path.toFile())) {
                fos.write(bom);
                fos.write(contentBytes);
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Cannot write to output file: " + outputPath, e);
        }
    }

    public static void write(String content, String outputPath) {
        FileWriter.write(content, outputPath, "UTF-8");
    }
}

