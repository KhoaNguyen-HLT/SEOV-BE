package seov.se_app.pu.cfr.service;

import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.pu.cfr.entity.CfrMaterial;
import seov.se_app.pu.cfr.entity.CfrOpenInventory;
import seov.se_app.pu.cfr.repository.CfrMaterialsRepository;
import seov.se_app.pu.cfr.repository.CfrOpenInventoryRepository;
import seov.se_app.pu.cfr.repository.CfrTransInventoryRepository;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CfrService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private CfrMaterialsRepository cfrMaterialsRepository;
    @Autowired
    private CfrOpenInventoryRepository cfrOpenInventoryRepository;

    @Autowired
    private CfrTransInventoryRepository cfrTransInventoryRepository;


    @Transactional
    public List<CfrMaterial> saveMaterialData(MultipartFile file, String reportName) {
        List<CfrMaterial> saveList = new ArrayList<>();
        int noSheet;
        if (file == null || file.isEmpty()) {
            return saveList;
        }

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {
            if("15".equals(reportName)) {
                noSheet = 0;
            } else {
                noSheet = 1;
            }

            Sheet sheet = workbook.getSheetAt(noSheet);
//            lấy data material
            List<CfrMaterial> cfrMaterials = getMaterialData(sheet, reportName);
            if (cfrMaterials.isEmpty()) {
                return saveList;
            }


            for (CfrMaterial excelData : cfrMaterials) {

                Optional<CfrMaterial> optionalOld =
                        cfrMaterialsRepository.findByItemCode(excelData.getItemCode());

                if (optionalOld.isPresent()) {

                    CfrMaterial oldData = optionalOld.get();

                    oldData.setItemNameE(excelData.getItemNameE());
                    oldData.setItemNameV(excelData.getItemNameV());
                    oldData.setHqUnit(excelData.getHqUnit());
                    oldData.setCfrUnit(excelData.getCfrUnit());
                    oldData.setGscmUnit(excelData.getGscmUnit());
                    oldData.setMaterialType(excelData.getMaterialType());
                    oldData.setCustomMode(excelData.getCustomMode());
                    oldData.setHsCode(excelData.getHsCode());
                    oldData.setSupplier(excelData.getSupplier());
                    oldData.setGscmType(excelData.getGscmType());
                    oldData.setType(excelData.getType());

                    saveList.add(oldData);

                } else {

                    saveList.add(excelData);
                }
            }

            cfrMaterialsRepository.saveAll(saveList);

            return saveList ;

        } catch (Exception e) {
            throw new RuntimeException("Import material data failed", e);
        }
    }

//    //lấy data file material list
    public List<CfrMaterial> getMaterialData(Sheet sheet, String reportName) {
        DataFormatter formatter = new DataFormatter();
        List<CfrMaterial> dataList = new ArrayList<>();
        String materialType;
        if("15".equals(reportName)) {
            materialType = "NVL";
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();

                // bỏ qua dòng trống
                if (itemCode.isEmpty()) {
                    continue;
                }

                CfrMaterial material = CfrMaterial.builder()
                        .itemCode(itemCode)
                        .itemNameE(formatter.formatCellValue(row.getCell(2)).trim())
                        .itemNameV(formatter.formatCellValue(row.getCell(3)).trim())
                        .hqUnit(formatter.formatCellValue(row.getCell(4)).trim())
                        .cfrUnit(formatter.formatCellValue(row.getCell(5)).trim())
                        .gscmUnit(formatter.formatCellValue(row.getCell(6)).trim())
                        .materialType(formatter.formatCellValue(row.getCell(7)).trim())
                        .customMode(formatter.formatCellValue(row.getCell(8)).trim())
                        .hsCode(formatter.formatCellValue(row.getCell(9)).trim())
                        .supplier(formatter.formatCellValue(row.getCell(10)).trim())
                        .type(materialType)
                        .build();

                dataList.add(material);
            }

        } else if("15a".equals(reportName)) {
            materialType = "TP";
            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(1)).trim();

                // bỏ qua dòng trống
                if (itemCode.isEmpty()) {
                    continue;
                }

                CfrMaterial material = CfrMaterial.builder()
                        .itemCode(itemCode)
                        .itemNameE(formatter.formatCellValue(row.getCell(2)).trim())
                        .itemNameV(formatter.formatCellValue(row.getCell(3)).trim())
                        .gscmUnit(formatter.formatCellValue(row.getCell(4)).trim())
                        .cfrUnit(formatter.formatCellValue(row.getCell(4)).trim())
                        .hsCode(formatter.formatCellValue(row.getCell(5)).trim())
                        .materialType(formatter.formatCellValue(row.getCell(6)).trim())
                        .gscmType(formatter.formatCellValue(row.getCell(7)).trim())
                        .type(materialType)
                        .build();

                dataList.add(material);
            }
        }



        return dataList;
    }



//    lưu hoặc update bảng tồn đầu
    public List<CfrOpenInventory> saveOpenInventory(MultipartFile file, String reportName) {
        List<CfrOpenInventory> data = new ArrayList<>();
        int noSheet;
        if (file == null || file.isEmpty()) {
            return data;
        }

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {
            if("15".equals(reportName)) {noSheet = 0; }
            else {noSheet = 1; }
            Sheet sheet = workbook.getSheetAt(noSheet);
            FormulaEvaluator evaluator =
                    workbook.getCreationHelper().createFormulaEvaluator();
//            lấy data
            List<CfrOpenInventory> CfrOpenInventorys = getDataInventory(sheet, evaluator, reportName);
            if (CfrOpenInventorys.isEmpty()) {
                return data;
            }
            List<CfrOpenInventory> saveList = new ArrayList<>();

            for (CfrOpenInventory excelData : CfrOpenInventorys) {

                Optional<CfrOpenInventory> optionalOld =
                        cfrOpenInventoryRepository.findByItemCode(excelData.getItemCode());

                if (optionalOld.isPresent()) {

                    CfrOpenInventory oldData = optionalOld.get();

                    oldData.setItemCode(excelData.getItemCode());
                    oldData.setPeriod(excelData.getPeriod());
                    oldData.setQuantity(excelData.getQuantity());
                    oldData.setReportType(excelData.getReportType());

                    saveList.add(oldData);

                } else {

                    saveList.add(excelData);
                }
            }
            cfrOpenInventoryRepository.saveAll(saveList);
            return saveList;


        } catch (Exception e) {
            throw new RuntimeException("Import open inventory data failed", e);
        }
    }


    //lấy data file tồn đầu
    public List<CfrOpenInventory> getDataInventory(Sheet sheet, FormulaEvaluator evaluator, String reportName) {
        DataFormatter formatter = new DataFormatter();
        List<CfrOpenInventory> dataList = new ArrayList<>();

        for (int i = 9; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            if (row == null) {
                continue;
            }

            String itemCode = formatter.formatCellValue(row.getCell(1)).trim();
            BigDecimal quantity =  getFormulaBigDecimal(row.getCell(10), evaluator);


            // bỏ qua dòng trống
            if (itemCode.isEmpty()) {
                continue;
            }

            if (quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }



            CfrOpenInventory openInventory = CfrOpenInventory.builder()
                    .period("2026-04")
                    .reportMonth("2026-04")
                    .itemCode(itemCode)
                    .quantity(quantity)
                    .reportType(reportName)
                    .build();

            dataList.add(openInventory);
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

    public List<Map<String, Object>> getData(String reportName, String month) {
        if("15".equals(reportName)) {
            return cfrTransInventoryRepository.getData(month);
        } else if ("15a".equals(reportName)) {
            return cfrTransInventoryRepository.getData15a(month);
        } else {
            return cfrTransInventoryRepository.getData16(month);
        }

    }


    public List<Map<String, Object>> checkExistedData(String month, String reportName) {
        return cfrTransInventoryRepository.checkExistedData(month,reportName );
    }



    public List<CfrOpenInventory> updateOpenInventory(String reportName, String month) {
        String month1 = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM"));
        if("15".equals(reportName)) {
            List<Map<String, Object>> data = cfrOpenInventoryRepository.updateOpenInventory15();
            List<CfrOpenInventory> inventories = data.stream()
                    .map(row -> CfrOpenInventory.builder()
                            .itemCode((String) row.get("item_code"))
                            .quantity(BigDecimal.valueOf(0))
                            .period("2026-04")
                            .reportMonth((String) row.get("month"))
                            .reportType((String) row.get("report_type"))
                            .build())
                    .toList();

            return  cfrOpenInventoryRepository.saveAll(inventories);
        } else {
            List<Map<String, Object>> data = cfrOpenInventoryRepository.updateOpenInventory15a();
            List<CfrOpenInventory> inventories = data.stream()
                    .map(row -> CfrOpenInventory.builder()
                            .itemCode((String) row.get("item_code"))
                            .quantity(BigDecimal.valueOf(0))
                            .period("2026-04")
                            .reportMonth((String) row.get("month"))
                            .reportType((String) row.get("report_type"))
                            .build())
                    .toList();

            return  cfrOpenInventoryRepository.saveAll(inventories);
        }



    };


}
