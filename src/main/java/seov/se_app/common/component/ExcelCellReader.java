package seov.se_app.common.component;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

@Component
public class ExcelCellReader {

    private final DataFormatter formatter = new DataFormatter();

    public String getString(Row row, String column) {
        if (row == null) return "";

        Cell cell = row.getCell(CellReference.convertColStringToIndex(column));
        return formatter.formatCellValue(cell).trim();
    }

    public LocalDate getLocalDate(Row row, String column) {
        Cell cell = getCell(row, column);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String value = formatter.formatCellValue(cell).trim();
        if (value.isEmpty()) return null;

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );

        for (DateTimeFormatter f : formatters) {
            try {
                return LocalDate.parse(value, f);
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Không parse được ngày: " + value + " cột " + column);
    }

// get cell with localdate fomat - Formula
public LocalDate getFormulaLocalDate(Row row, String column) {
    Cell cell = getCell(row, column);
    if (cell == null) {
        return null;
    }

    try {
        if (cell.getCellType() == CellType.FORMULA) {
            switch (cell.getCachedFormulaResultType()) {
                case NUMERIC:
                    return DateUtil.getLocalDateTime(cell.getNumericCellValue()).toLocalDate();

                case STRING:
                    return parseLocalDate(cell.getStringCellValue().trim(), column);

                default:
                    return null;
            }
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        String value = formatter.formatCellValue(cell).trim();
        return parseLocalDate(value, column);

    } catch (Exception e) {
        throw new IllegalArgumentException(
                "Không đọc được ngày tại cột " + column + ", row " + (row.getRowNum() + 1),
                e
        );
    }
}

    private LocalDate parseLocalDate(String value, String column) {
        if (value == null || value.isBlank() || "-".equals(value)) {
            return null;
        }

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("d-MMM-yy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("dd-MMM-yy", Locale.ENGLISH),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new IllegalArgumentException(
                "Không parse được ngày: \"" + value + "\" cột " + column
        );
    }

//-------------------------------end--------------------------------------


    public LocalDateTime getLocalDateTime(Row row, String column) {
        Cell cell = getCell(row, column);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue();
        }

        String value = formatter.formatCellValue(cell).trim();
        if (value.isEmpty()) return null;

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a"),
                DateTimeFormatter.ofPattern("M/d/yyyy h:mm a"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        );

        for (DateTimeFormatter f : formatters) {
            try {
                return LocalDateTime.parse(value, f);
            } catch (DateTimeParseException ignored) {}
        }

        throw new IllegalArgumentException("Không parse được ngày giờ: " + value + " cột " + column);
    }

    private Cell getCell(Row row, String column) {
        if (row == null) return null;
        int columnIndex = CellReference.convertColStringToIndex(column);
        return row.getCell(columnIndex);
    }

    public BigDecimal getBigDecimal(Row row, String column, FormulaEvaluator evaluator) {
        Cell cell = getCell(row, column);

        if (cell == null) {
            return BigDecimal.ZERO;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cell.getNumericCellValue());
            }

            if (cell.getCellType() == CellType.FORMULA) {
                CellValue cellValue = evaluator.evaluate(cell);

                if (cellValue != null && cellValue.getCellType() == CellType.NUMERIC) {
                    return BigDecimal.valueOf(cellValue.getNumberValue());
                }

                if (cellValue != null && cellValue.getCellType() == CellType.STRING) {
                    String value = cellValue.getStringValue().trim();
                    return value.isEmpty()
                            ? BigDecimal.ZERO
                            : new BigDecimal(value.replace(",", ""));
                }
            }

            String value = formatter.formatCellValue(cell).replace(",", "").trim();
            return value.isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }



    //    hàm xử lý công thức
    public BigDecimal getFormulaBigDecimal(
            Cell cell,
            FormulaEvaluator evaluator) {

        try {
            if (cell == null) {
                return BigDecimal.ZERO;
            }

            CellValue cellValue = evaluator.evaluate(cell);

            if (cellValue == null) {
                return BigDecimal.ZERO;
            }

            if (cellValue.getCellType() == CellType.NUMERIC) {
                return BigDecimal.valueOf(cellValue.getNumberValue());
            }

            if (cellValue.getCellType() == CellType.STRING) {
                String value = cellValue.getStringValue().trim();

                if (value.isEmpty()) {
                    return BigDecimal.ZERO;
                }

                return new BigDecimal(value.replace(",", ""));
            }

            return BigDecimal.ZERO;

        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
