package seov.se_app.mf.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import seov.se_app.device.entity.Device;
import seov.se_app.mf.dto.request.MaterialRequestReportProjection;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class PrintMaterialExcelData {
    public byte[] exportExcel(List<MaterialRequestReportProjection> data) throws Exception {

//        ClassPathResource resource =
//                new ClassPathResource("templates/material_confirm.xlsx");
        String path = "D:\\REPORT\\material_report.xlsx";

        try (InputStream is = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            MaterialRequestReportProjection footer = data.get(0);


            setCellValueF(sheet, "F7", footer.getCreatedBy());
            setCellValueF(sheet, "F10", footer.getProductionNumber());
            setCellValueF(sheet, "F11", "10");
//            setCellValueF(sheet, "L7", footer.get);
            setCellValueF(sheet, "F18", footer.getCreatedBy());
            setCellValueF(sheet, "G18", footer.getApprovedBy());
            setCellValueF(sheet, "L18", footer.getIssuedBy());


            int templateRowIndex = 14;
            Row templateRow = sheet.getRow(templateRowIndex);

            if (templateRow == null) {
                throw new RuntimeException("Không tìm thấy dòng template trong file Excel");
            }

            // Nếu data nhiều hơn 1 dòng thì đẩy phần dưới xuống
            if (data.size() > 1) {
                sheet.shiftRows(
                        templateRowIndex + 1,
                        sheet.getLastRowNum(),
                        data.size() - 1
                );
            }

            for (int i = 0; i < data.size(); i++) {

                int currentRowIndex = templateRowIndex + i;

                Row row = sheet.getRow(currentRowIndex);
                if (row == null) {
                    row = sheet.createRow(currentRowIndex);
                }

                // copy style từ dòng template
                for (int j = 0; j < templateRow.getLastCellNum(); j++) {
                    Cell templateCell = templateRow.getCell(j);
                    if (templateCell == null) {
                        continue;
                    }

                    Cell cell = row.getCell(j);
                    if (cell == null) {
                        cell = row.createCell(j);
                    }

                    cell.setCellStyle(templateCell.getCellStyle());
                }

                MaterialRequestReportProjection item = data.get(i);

                setCellValue(sheet, currentRowIndex, 1, BigDecimal.valueOf(i + 1));
                setCellValue(sheet, currentRowIndex, 2, item.getMaterialCode());
                setCellValue(sheet, currentRowIndex, 3, item.getMaterialCode());
                setCellValue(sheet, currentRowIndex, 4, item.getMaterialName());
                setCellValue(sheet, currentRowIndex, 5, item.getMaterialName());
                setCellValue(sheet, currentRowIndex, 6, item.getUnit());
                setCellValue(sheet, currentRowIndex, 7, item.getMaterialType());
                setCellValue(sheet, currentRowIndex, 9, item.getQtyOrder());
                setCellValue(sheet, currentRowIndex, 11, item.getQtyOrder());
                setCellValue(sheet, currentRowIndex, 12, item.getIssuedQty());
                setCellValue(sheet, currentRowIndex, 15, item.getLayout());
            }

            workbook.write(out);
            return out.toByteArray();
        }
    }



    // helper method
    private void setCellValue(Sheet sheet, int rowIndex, int colIndex, BigDecimal value) {

        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        if (value == null) {
            cell.setBlank();
        } else {
            cell.setCellValue(value.doubleValue());
        }
    }

    private void setCellValue(
            Sheet sheet,
            int rowIndex,
            int colIndex,
            String value) {

        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(colIndex);
        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        cell.setCellValue(value == null ? "" : value);
    }

    private void setCellValueF(Sheet sheet, String cellAddress, String value) {

        CellReference ref = new CellReference(cellAddress);

        Row row = sheet.getRow(ref.getRow());
        if (row == null) {
            row = sheet.createRow(ref.getRow());
        }

        Cell cell = row.getCell(ref.getCol());
        if (cell == null) {
            cell = row.createCell(ref.getCol());
        }

        if (value == null) {
            cell.setBlank();
        } else {
            cell.setCellValue(value);
        }
    }


}
