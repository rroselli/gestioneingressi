package com.oleificiorenna.gestioneingressi.services;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExportExcelService {

    public <T> void exportToExcel(List<T> dataList, String[] headers, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export");

        // Header
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }

        // Data
        int rowNum = 1;
        for (T item : dataList) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            colNum = writeFields(item, row, colNum);
        }

        for (int i = 0; i < headers.length; i++) sheet.autoSizeColumn(i);

        workbook.write(outputStream);
        workbook.close();
    }

    /**
     * Scrive i campi di un oggetto nella riga.
     * Se un campo è un oggetto complesso, esplode i suoi campi.
     */
    private int writeFields(Object obj, Row row, int startCol) {
        if (obj == null) return startCol;

        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            // 🔴 Skip sempre i campi "id"
            if ("id".equalsIgnoreCase(field.getName())) {
                continue;
            }

            try {
                Object value = field.get(obj);

                if (isSimpleType(field.getType())) {
                    Cell cell = row.createCell(startCol++);
                    if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof java.time.LocalDateTime ldt) {
                        cell.setCellValue(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    } else if (value != null) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue("");
                    }
                } else {
                    // Oggetto complesso → scrivi i suoi campi nello stesso row
                    startCol = writeFields(value, row, startCol);
                }
            } catch (IllegalAccessException e) {
                log.error("Errore accesso campo {}", field.getName(), e);
            }
        }
        return startCol;
    }

    /**
     * Verifica se il tipo è "semplice" (primitivo, wrapper, stringa, data...).
     */
    private boolean isSimpleType(Class<?> type) {
        return type.isPrimitive()
                || Number.class.isAssignableFrom(type)
                || CharSequence.class.isAssignableFrom(type)
                || Boolean.class.equals(type)
                || java.time.temporal.Temporal.class.isAssignableFrom(type)
                || java.util.Date.class.isAssignableFrom(type);
    }
}
