package com.example.springConverter.excel.util;

import org.apache.poi.ss.usermodel.*;

import java.text.SimpleDateFormat;

public class ExcelHelper {
    private static final DataFormatter dataFormatter = new DataFormatter();
    private static final SimpleDateFormat sdf = new SimpleDateFormat();

    public static String getCellStringValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            switch (cell.getCellStyle().getDataFormatString()) {
                case "m/d/yy" -> {
                    sdf.applyPattern("d/M/yy");
                    return sdf.format(cell.getDateCellValue());
                }
                case "m/d/yy h:mm" -> {
                    sdf.applyPattern("d/M/yy h:mm");
                    return sdf.format(cell.getDateCellValue());
                }
            }
        }
        return dataFormatter.formatCellValue(cell);
    }

    public static void copyCellValue(Cell srcCell, Cell destCell) {
        switch (srcCell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(srcCell))
                    destCell.setCellValue(srcCell.getDateCellValue());
                else
                    destCell.setCellValue(srcCell.getNumericCellValue());
                break;
            case STRING:
                destCell.setCellValue(srcCell.getRichStringCellValue());
                break;
            case FORMULA:
                destCell.setCellFormula(srcCell.getCellFormula());
                break;
            case BLANK:
                destCell.setBlank();
                break;
            case BOOLEAN:
                destCell.setCellValue(srcCell.getBooleanCellValue());
                break;
            case ERROR:
                destCell.setCellErrorValue(srcCell.getErrorCellValue());
                break;

            default:
                throw new IllegalArgumentException("Invalid cell type " + srcCell.getCellType());
        }
    }

    public static String[][] toStringMatrix(Sheet sheet) {
        var height = sheet.getPhysicalNumberOfRows();
        var width = sheet.getRow(0).getLastCellNum();
        var result = new String[height][width];
        var resultRowNum = 0;
        for (var row : sheet) {
            for (var cellNum = 0; cellNum < width; cellNum++) {
                var cell = row.getCell(cellNum);
                result[resultRowNum][cellNum] = cell == null ? "" : getCellStringValue(cell);
            }
            resultRowNum++;
        }

        return result;
    }

    public static void copyCellStyle(Cell srcCell, Cell destCell, CellCopyContext context) {
        if (srcCell.getSheet() != null && destCell.getSheet() != null &&
            destCell.getSheet().getWorkbook() == srcCell.getSheet().getWorkbook()) {
            destCell.setCellStyle(srcCell.getCellStyle());
        } else {
            CellStyle srcStyle = srcCell.getCellStyle();
            CellStyle destStyle = context == null ? null : context.getMappedStyle(srcStyle);
            if (destStyle == null) {
                destStyle = destCell.getSheet().getWorkbook().createCellStyle();
                destStyle.cloneStyleFrom(srcStyle);
                if (context != null)
                    context.putMappedStyle(srcStyle, destStyle);
            }
            destCell.setCellStyle(destStyle);
        }
    }
}
