package seov.se_app.mf.service;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import seov.Config.properties.FileProperties;
import seov.se_app.device.entity.Device;
import seov.se_app.mf.dto.request.MaterialRequestReportProjection;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PrintMaterialExcelData {
    private final FileProperties fileProperties;
    public byte[] exportExcel(List<MaterialRequestReportProjection> data) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String path = fileProperties.getMfNvl();

        try (InputStream is = new FileInputStream(path);
             Workbook workbook = new XSSFWorkbook(is);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.getSheetAt(0);

            MaterialRequestReportProjection infoData = data.get(0);

            setCellValueF(sheet, "J5", infoData.getRequestNo());
            setCellValueF(sheet, "D7", infoData.getCreatedBy());
            setCellValueF(sheet, "D8", infoData.getDepartment());
            setCellValueF(sheet, "D9", infoData.getRemark());
            setCellValueF(sheet, "D10", infoData.getProductionNumber());
            setCellValueF(sheet, "D11", infoData.getQtyRequest());
            setCellValueF(sheet, "H7", infoData.getCreateAt());
            setCellValueF(sheet, "H8", infoData.getRequestNeedDate());
            setCellValueF(sheet, "H9", infoData.getRequiredTime());
            setCellValueF(sheet, "H10", infoData.getProductName());
//            setCellValueF(sheet, "L7", footer.get);
            setCellValueF(sheet, "D18", infoData.getCreatedBy());
            setCellValueF(sheet, "E18", infoData.getApprovedBy());
            setCellValueF(sheet, "H18", infoData.getIssuedBy());


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

                    
                    CellStyle style = workbook.createCellStyle();
                    style.cloneStyleFrom(templateCell.getCellStyle());

                    DataFormat format = workbook.createDataFormat();
                    style.setDataFormat(format.getFormat("#,##0.00"));

                    cell.setCellStyle(style);
                }

                MaterialRequestReportProjection item = data.get(i);

                setCellValue(sheet, currentRowIndex, 1, BigDecimal.valueOf(i + 1));
                setCellValue(sheet, currentRowIndex, 2, item.getMaterialCode());
                setCellValue(sheet, currentRowIndex, 3, item.getMaterialName());
                setCellValue(sheet, currentRowIndex, 4, item.getUnit());
                setCellValue(sheet, currentRowIndex, 5, item.getMaterialType());
                setCellValue(sheet, currentRowIndex, 6, item.getQtyOrder());
                setCellValue(sheet, currentRowIndex, 7, item.getQtyOrder());
                if (item.getIssuedQty() != null && item.getIssuedQty().compareTo(BigDecimal.ZERO) != 0) {
                    setCellValue(sheet, currentRowIndex, 8, item.getIssuedQty());
                }
                setCellValue(sheet, currentRowIndex, 11, item.getPerson());
                setCellValue(sheet, currentRowIndex, 12, item.getLayout());
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
