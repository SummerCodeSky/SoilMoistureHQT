/*
 * Decompiled with CFR 0.152.
 */
package com.example.excelreplacer;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader
implements AutoCloseable {
    private final Workbook workbook;
    private final Sheet sheet;
    private final DataFormatter dataFormatter = new DataFormatter();

    public ExcelReader(String filePath) {
        this(filePath, null, null);
    }

    public ExcelReader(String filePath, String sheetName) {
        this(filePath, sheetName, null);
    }

    public ExcelReader(String filePath, String sheetName, Integer sheetIndex) {
        try {
            FileInputStream fis = new FileInputStream(filePath);
            this.workbook = new XSSFWorkbook(fis);
            if (sheetName != null && !sheetName.isEmpty()) {
                this.sheet = this.workbook.getSheet(sheetName);
                if (this.sheet == null) {
                    throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in Excel file.");
                }
            } else if (sheetIndex != null) {
                if (sheetIndex < 0 || sheetIndex >= this.workbook.getNumberOfSheets()) {
                    throw new IllegalArgumentException("Sheet index " + sheetIndex + " out of bounds.");
                }
                this.sheet = this.workbook.getSheetAt(sheetIndex);
            } else {
                this.sheet = this.findSheetWithData();
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Cannot read Excel file: " + filePath, e);
        }
    }

    private Sheet findSheetWithData() {
        Sheet bestSheet = null;
        int maxRows = 0;
        for (int i = 0; i < this.workbook.getNumberOfSheets(); ++i) {
            Sheet sheet = this.workbook.getSheetAt(i);
            int rowCount = sheet.getPhysicalNumberOfRows();
            if (rowCount <= maxRows) continue;
            maxRows = rowCount;
            bestSheet = sheet;
        }
        return bestSheet != null ? bestSheet : this.workbook.getSheetAt(0);
    }

    public String getCellValue(int row, int col) {
        return this.getCellValue(null, row, col);
    }

    public String getCellValue(String sheetName, int row, int col) {
        Sheet targetSheet;
        if (row < 1 || col < 1) {
            throw new IllegalArgumentException(String.format("Row and column must be >= 1, got row=%d, col=%d", row, col));
        }
        int rowIndex = row - 1;
        int colIndex = col - 1;
        Sheet sheet = targetSheet = sheetName != null && !sheetName.isEmpty() ? this.workbook.getSheet(sheetName) : this.sheet;
        if (targetSheet == null) {
            throw new IllegalArgumentException("Sheet '" + sheetName + "' not found.");
        }
        Row sheetRow = targetSheet.getRow(rowIndex);
        if (sheetRow == null) {
            return "";
        }
        Cell cell = sheetRow.getCell(colIndex);
        if (cell == null) {
            return "";
        }
        return this.getCellStringValue(cell);
    }

    private String getCellStringValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING: {
                return cell.getStringCellValue();
            }
            case NUMERIC: {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                double numValue = cell.getNumericCellValue();
                String formatted = this.dataFormatter.formatCellValue(cell);
                if (!formatted.isEmpty()) {
                    return formatted;
                }
                return String.valueOf(numValue);
            }
            case BOOLEAN: {
                return String.valueOf(cell.getBooleanCellValue());
            }
            case FORMULA: {
                try {
                    return cell.getStringCellValue();
                }
                catch (IllegalStateException e) {
                    try {
                        double numValue = cell.getNumericCellValue();
                        String formatted = this.dataFormatter.formatCellValue(cell);
                        if (!formatted.isEmpty()) {
                            return formatted;
                        }
                        return String.valueOf(numValue);
                    }
                    catch (IllegalStateException e2) {
                        return "";
                    }
                }
            }
            case BLANK: {
                return "";
            }
        }
        return "";
    }

    @Override
    public void close() {
        try {
            this.workbook.close();
        }
        catch (IOException e) {
            System.err.println("Warning: Failed to close Excel file properly.");
        }
    }
}

