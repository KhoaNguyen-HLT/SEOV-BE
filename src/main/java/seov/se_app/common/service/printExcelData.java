package seov.se_app.common.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import seov.se_app.device.entity.Device;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class printExcelData {
    public byte[] exportExcel(List<Device> devices) throws Exception {

//        InputStream is = getClass()
//                .getResourceAsStream("/templates/material_confirm.xlsx");
        ClassPathResource resource =
                new ClassPathResource("templates/material_confirm.xlsx");

        InputStream is = resource.getInputStream();

        Workbook workbook = new XSSFWorkbook(is);

        Sheet sheet = workbook.getSheetAt(0);

        // Header
        setCellValue(sheet, 1, 1, "WO123456");
        setCellValue(sheet, 2, 1, "MODEL-A");
        setCellValue(sheet, 3, 1, "LINE-01");

        // Detail

        int templateRowIndex = 9;

        Row templateRow = sheet.getRow(templateRowIndex);

// shift phần dưới xuống
        if (devices.size() > 1) {

            sheet.shiftRows(
                    templateRowIndex + 1,
                    sheet.getLastRowNum(),
                    devices.size() - 1
            );
        }

        for (int i = 0; i < devices.size(); i++) {
            int currentRowIndex = templateRowIndex + i;
            Row row = sheet.getRow(currentRowIndex);
            if (row == null) {
                row = sheet.createRow(currentRowIndex);
            }
            // copy style
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
            Device device = devices.get(i);
            setCellValue(sheet, currentRowIndex, 0, String.valueOf(i + 1));
            setCellValue(sheet, currentRowIndex, 1, device.getLocation());
            setCellValue(sheet, currentRowIndex, 2, device.getSupplier());
            setCellValue(sheet, currentRowIndex, 3, device.getSerialNumber());
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }



    // helper method
    private void setCellValue(
            Sheet sheet,
            int rowIndex,
            int colIndex,
            String value
    ) {

        Row row = sheet.getRow(rowIndex);

        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(colIndex);

        if (cell == null) {
            cell = row.createCell(colIndex);
        }

        cell.setCellValue(value);
    }


}
