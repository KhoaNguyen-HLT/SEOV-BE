package seov.se_app.pu.cfr.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.component.ExcelCellReader;
import seov.se_app.pu.cfr.entity.CfrCrossInOut;
import seov.se_app.pu.cfr.entity.CfrCrossInventory;
import seov.se_app.pu.cfr.repository.*;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CfrCrossService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final CfrCrossInOutRepository cfrCrossInOutRepository;
    private final CfrCrossInventoryRepository cfrCrossInventoryRepository;
    private final ExcelCellReader excel;

    LocalDate localDate = LocalDate.now();
    int fiscalYear = localDate.getMonthValue() >= 4
            ? localDate.getYear()
            : localDate.getYear() - 1;
    String period = fiscalYear + "-04";

    @Transactional
    public List<CfrCrossInOut> saveCrossInOutData(MultipartFile file, String documentType, String month, String reportName) {
        List<CfrCrossInOut> cfrCrossInOuts = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            return cfrCrossInOuts;
        }

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
//            nếu là báo cáo 15a thì cầnlaaysy sheet(1)
            if("15a".equals(reportName) && "IE_DATA_PU".equals(documentType)) {
                sheet = workbook.getSheetAt(1);
            }
            FormulaEvaluator evaluator =
                    workbook.getCreationHelper().createFormulaEvaluator();
//            lấy data trans
            cfrCrossInOuts = getCrossInOutData(sheet, documentType, month, evaluator, reportName);
            if (cfrCrossInOuts.isEmpty()) {
                return cfrCrossInOuts;
            }
//            check neu da có data thì xóa đi trước
            cfrCrossInOutRepository.deleteCrossInOutData(month, documentType, reportName, period);
//            sau khi xóa xong se lưu lại
            return cfrCrossInOutRepository.saveAll(cfrCrossInOuts);
//            return cfrCrossInOuts;

        } catch (Exception e) {
            throw new RuntimeException("Import material data failed", e);
        }
    }

    public List<CfrCrossInOut> getCrossInOutData(Sheet sheet,String documentType, String month, FormulaEvaluator evaluator, String reportName) {
        DataFormatter formatter = new DataFormatter();
        List<CfrCrossInOut> dataList = new ArrayList<>();
        if ("IE_DATA_PU".equals(documentType) && "15".equals(reportName)) {
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String customsTypeCode = formatter.formatCellValue(row.getCell(4)).trim();
                String itemCode = formatter.formatCellValue(row.getCell(5)).trim();
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(8), evaluator);

                // bỏ qua dòng trống
                if (itemCode.isEmpty() || "End".equals(itemCode)) {
                    continue;
                }

                CfrCrossInOut cfrCrossInOut = CfrCrossInOut.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode(customsTypeCode)
                        .transactionType("IN")
                        .reportType(reportName)
                        .period(period)
                        .build();

                dataList.add(cfrCrossInOut);
            }
        }else if ("IE_DATA_PU".equals(documentType) && "15a".equals(reportName)) {
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }


                String customsTypeCode = formatter.formatCellValue(row.getCell(3)).trim();
                String itemCode = formatter.formatCellValue(row.getCell(4)).trim();
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(6), evaluator);

                // bỏ qua dòng trống
                if (itemCode.isEmpty() || "End".equals(itemCode)) {
                    continue;
                }

                CfrCrossInOut cfrCrossInOut = CfrCrossInOut.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode(customsTypeCode)
                        .transactionType("OUT")
                        .reportType(reportName)
                        .period(period)
                        .build();

                dataList.add(cfrCrossInOut);
            }

        }else if ("ACCEPTANCE_GSCM".equals(documentType)) {
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }
                String Expense = formatter.formatCellValue(row.getCell(73)).trim();
                //                bỏ qua dong spare part, intener using
                if("Spare parts".equals(Expense) ||"Internal using".equals(Expense) ) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(44)).trim();
                String customsTypeCode = "GSCM";
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(55), evaluator);


                // bỏ qua dòng trống
                if (itemCode.isEmpty() || "End".equals(itemCode)) {
                    continue;
                }


                CfrCrossInOut cfrCrossInOut = CfrCrossInOut.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode(customsTypeCode)
                        .transactionType("IN")
                        .reportType(reportName)
                        .period(period)
                        .build();

                dataList.add(cfrCrossInOut);
            }

        } else if ("FG_PM".equals(documentType)) {
            for (int i = 8; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                String customsTypeCode = "FG_PM";
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(8), evaluator);

                // bỏ qua dòng trống
                if (itemCode.isEmpty() || "End".equals(itemCode)) {
                    continue;
                }

                CfrCrossInOut cfrCrossInOut = CfrCrossInOut.builder()
                        .month(month)
                        .itemCode(itemCode)
                        .quantity(quantity)
                        .documentType(documentType)
                        .customsTypeCode(customsTypeCode)
                        .transactionType("OUT")
                        .reportType(reportName)
                        .period(period)
                        .build();

                dataList.add(cfrCrossInOut);
            }

        }

        return dataList;
    }




//    lay data ivt
@Transactional
public List<CfrCrossInventory> saveCrossIvtData(MultipartFile file, String documentType, String month, String reportName) {
    List<CfrCrossInventory> cfrCrossInventories = new ArrayList<>();
    if (file == null || file.isEmpty()) {
        return cfrCrossInventories;
    }

    try (
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream)
    ) {
        Sheet sheet = workbook.getSheetAt(0);

        FormulaEvaluator evaluator =
                workbook.getCreationHelper().createFormulaEvaluator();
//            lấy data trans
        cfrCrossInventories = getCrossIvtData(sheet, documentType, month, evaluator, reportName, period);
        if (cfrCrossInventories.isEmpty()) {
            return cfrCrossInventories;
        }
//            check neu da có data thì xóa đi trước
        cfrCrossInventoryRepository.deleteCrossIvtData(month, documentType, reportName, period);
//            sau khi xóa xong se lưu lại
        return cfrCrossInventoryRepository.saveAll(cfrCrossInventories);

    } catch (Exception e) {
        throw new RuntimeException("Import material data failed", e);
    }
}

    public List<CfrCrossInventory> getCrossIvtData(Sheet sheet,String documentType, String month, FormulaEvaluator evaluator, String reportName, String period) {
        DataFormatter formatter = new DataFormatter();
        List<CfrCrossInventory> dataList = new ArrayList<>();
        if ("IVT_MF".equals(documentType) && "15".equals(reportName)) {
            for (int i = 7; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(13), evaluator);

                // bỏ qua dòng trống
                if (itemCode.isEmpty() || "End".equals(itemCode) || quantity == null
                        || quantity.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                CfrCrossInventory cfrCrossInventory = CfrCrossInventory.builder()
                        .reportMonth(month)
                        .itemCode(itemCode)
                        .period(period)
                        .quantity(quantity)
                        .documentType(documentType)
                        .reportType(reportName)
                        .build();

                dataList.add(cfrCrossInventory);
            }
        }else if ("ENDING_PM".equals(documentType) && "15".equals(reportName)) {
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }


                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(8), evaluator);

                // skip row null or ZERO quantity number now
                if (itemCode.isEmpty() || "End".equals(itemCode) || quantity == null
                        || quantity.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                CfrCrossInventory cfrCrossInventory = CfrCrossInventory.builder()
                        .reportMonth(month)
                        .itemCode(itemCode)
                        .period(period)
                        .quantity(quantity)
                        .documentType(documentType)
                        .reportType(reportName)
                        .build();

                dataList.add(cfrCrossInventory);
            }

        }else if ("FG_MF".equals(documentType) && "15a".equals(reportName)) {
            for (int i = 9; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(7), evaluator);

                // skip row null or ZERO quantity number now
                if (itemCode.isEmpty() || "End".equals(itemCode) || quantity == null
                        || quantity.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                CfrCrossInventory cfrCrossInventory = CfrCrossInventory.builder()
                        .reportMonth(month)
                        .itemCode(itemCode)
                        .period(period)
                        .quantity(quantity)
                        .documentType(documentType)
                        .reportType(reportName)
                        .build();

                dataList.add(cfrCrossInventory);
            }

        } else if ("FG_PM".equals(documentType) && "15a".equals(reportName)) {
            for (int i = 8; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
                BigDecimal quantity =  excel.getFormulaBigDecimal(row.getCell(9), evaluator);

                // skip row null or ZERO quantity number now
                if (itemCode.isEmpty() || "End".equals(itemCode) || quantity == null
                        || quantity.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                CfrCrossInventory cfrCrossInventory = CfrCrossInventory.builder()
                        .reportMonth(month)
                        .itemCode(itemCode)
                        .period(period)
                        .quantity(quantity)
                        .documentType(documentType)
                        .reportType(reportName)
                        .build();

                dataList.add(cfrCrossInventory);
            }

        }

        return dataList;
    }



    public List<Map<String, Object>> checkExistedCrossInOutData(String month, String reportName, String documentType) {
        return cfrCrossInOutRepository.checkExistedData(month,reportName, documentType, period  );
    }


    public List<Map<String, Object>> checkExistedCrossIvtData(String month, String reportName, String documentType) {
        return cfrCrossInventoryRepository.checkExistedCrossIvtData(month,reportName, documentType, period );
    }





}
