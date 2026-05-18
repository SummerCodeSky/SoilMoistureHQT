/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

import com.example.excelreplacer.ReplaceDetail;
import java.util.List;

public record ReplaceReport(int totalMatches, int successfulReplacements, int skippedMatches, List<ReplaceDetail> details) {
}

