package seov.se_app.pu.cfr.service;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.pu.cfr.entity.CfrTransInventory;
import seov.se_app.pu.cfr.repository.CfrMaterialsRepository;
import seov.se_app.pu.cfr.repository.CfrOpenInventoryRepository;
import seov.se_app.pu.cfr.repository.CfrTransInventoryRepository;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CfrTrans15aService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CfrMaterialsRepository cfrMaterialsRepository;
    @Autowired
    private CfrOpenInventoryRepository cfrOpenInventoryRepository;

    @Autowired
    private CfrTransInventoryRepository cfrTransInventoryRepository;


    @Transactional
    public List<CfrTransInventory> saveTransData(MultipartFile file, String documentType, String month, String reportName) {
        List<CfrTransInventory> cfrTransInventories = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            return cfrTransInventories;
        }

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator =
                    workbook.getCreationHelper().createFormulaEvaluator();
//            lấy data trans
            cfrTransInventories = getDataTrans(sheet, documentType, month, evaluator, reportName);
            if (cfrTransInventories.isEmpty()) {
                return cfrTransInventories;
            }
//            check neu da có data thì xóa đi trước
//            String transType = cfrTransInventories.get(0).getTransactionType();
            cfrTransInventoryRepository.deleteTransInventoryData(month, documentType, reportName);
//            sau khi xóa xong se lưu lại
            return cfrTransInventoryRepository.saveAll(cfrTransInventories);

        } catch (Exception e) {
            throw new RuntimeException("Import material data failed", e);
        }
    }

    public List<CfrTransInventory> getDataTrans(Sheet sheet,String documentType, String month, FormulaEvaluator evaluator, String reportName) {
        DataFormatter formatter = new DataFormatter();
        List<CfrTransInventory> dataList = new ArrayList<>();
        if ("DATA_PU".equals(documentType)) {
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(20)).trim();
                String customsTypeCode = formatter.formatCellValue(row.getCell(3)).trim();
                BigDecimal quantity =  getFormulaBigDecimal(row.getCell(26), evaluator);

                // bỏ qua dòng trống
                if (itemCode.isEmpty()) {
                    continue;
                }

                CfrTransInventory cfrTransInventory = CfrTransInventory.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode(customsTypeCode)
                        .reportType(reportName)
                        .build();

                dataList.add(cfrTransInventory);
            }
        } else if ("IVT_MF".equals(documentType)) {
            for (int i = 9; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                BigDecimal quantity =  getFormulaBigDecimal(row.getCell(5), evaluator);

                // bỏ qua dòng trống
                if (itemCode.isEmpty()) {
                    continue;
                }

                CfrTransInventory cfrTransInventory = CfrTransInventory.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode("MF")
                        .transactionType("IN")
                        .reportType(reportName)
                        .build();

                dataList.add(cfrTransInventory);
            }

        } else if ("OTHER_MF".equals(documentType)) {
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                BigDecimal quantity =  getFormulaBigDecimal(row.getCell(5), evaluator);

                // bỏ qua dòng trống và dòng có giá trị <= 0
                if (itemCode.isEmpty() || quantity.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }


                CfrTransInventory cfrTransInventory = CfrTransInventory.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode("OTHER")
                        .transactionType("OUT")
                        .reportType(reportName)
                        .build();

                dataList.add(cfrTransInventory);
            }

        }

        return dataList;
    }


    //    hàm xử lý công thức
    private BigDecimal getFormulaBigDecimal(
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