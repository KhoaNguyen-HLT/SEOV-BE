package seov.se_app.pu.dpm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.component.ExcelCellReader;
import seov.se_app.pu.cfr.entity.CfrMaterial;
import seov.se_app.pu.dpm.entity.DpmGscmData;
import seov.se_app.pu.dpm.entity.DpmShippingPlanData;
import seov.se_app.pu.dpm.repository.DpmGscmDataRepository;
import seov.se_app.pu.dpm.repository.DpmShippingPlanDataRepository;


import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DpmService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final DpmGscmDataRepository dpmGscmDataRepository;
    private final DpmShippingPlanDataRepository dpmShippingPlanDataRepository;
    private final DataFormatter formatter = new DataFormatter();
    private final ExcelCellReader excel;

    @Transactional
    public List<DpmGscmData> saveGscmData(MultipartFile file, String reportName) {
        List<DpmGscmData> saveList = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            return saveList;
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream());) {

                Sheet sheet = workbook.getSheetAt(0);
//            lấy data material
                List<DpmGscmData> DpmGscmDatas = getGscmData(sheet, reportName);
                if (DpmGscmDatas.isEmpty()) {
                    return saveList;
                }

                dpmGscmDataRepository.deleteAll();
                return dpmGscmDataRepository.saveAll(DpmGscmDatas);


        } catch (Exception e) {
            throw new RuntimeException("Import data failed", e);
        }
    }


//lấy gscm Data
    public List<DpmGscmData> getGscmData(Sheet sheet, String reportName) {
        List<DpmGscmData> dataList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                String po = excel.getString(row, "A");

                // bỏ qua dòng trống
                if (po.isEmpty()) {
                    continue;
                }

                DpmGscmData dpmGscmData = DpmGscmData.builder()
                        .po(po)
                        .lineNo(excel.getString(row, "C"))
                        .requestNo(excel.getString(row, "E"))
                        .approvalDate(excel.getLocalDateTime(row, "M"))
                        .vendorC(excel.getString(row, "AB"))
                        .paymentTerms(excel.getString(row, "AL"))
                        .tradeTerms(excel.getString(row, "AN"))
                        .itemC(excel.getString(row, "AQ"))
                        .itemName(excel.getString(row, "AR"))
                        .orderQuantity(excel.getString(row, "BC"))
                        .uom(excel.getString(row, "BD"))
                        .unitPrice(excel.getString(row, "BF"))
                        .expenseName(excel.getString(row, "BT"))
                        .deliverySd(excel.getLocalDate(row, "BU"))
                        .build();

                dataList.add(dpmGscmData);
            }

        return dataList;
    }


//    Lây data shpping Plan

    @Transactional
    public List<DpmShippingPlanData> saveShippingPlanData(MultipartFile file, String reportName) {
        List<DpmShippingPlanData> saveList = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            return saveList;
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream());) {

            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator =
                    workbook.getCreationHelper().createFormulaEvaluator();
//            lấy data material
            List<DpmShippingPlanData> dpmShippingPlanDatas = getShippingPlanData(sheet, reportName, evaluator);
            if (dpmShippingPlanDatas.isEmpty()) {
                return saveList;
            }

            dpmShippingPlanDataRepository.deleteAll();
            return dpmShippingPlanDataRepository.saveAll(dpmShippingPlanDatas);


        } catch (Exception e) {
            throw new RuntimeException("Import data failed", e);
        }
    }


    //lấy gscm Data
    public List<DpmShippingPlanData> getShippingPlanData(Sheet sheet, String reportName, FormulaEvaluator evaluator) {
        List<DpmShippingPlanData> dataList = new ArrayList<>();
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            String po = excel.getString(row, "A");
            String asNo = (excel.getString(row, "G"));

            // bỏ qua dòng trống
            if (po.isEmpty()) {
                continue;
            }


            int qIndex = CellReference.convertColStringToIndex("Q");
            int sIndex = CellReference.convertColStringToIndex("S");
            int tIndex = CellReference.convertColStringToIndex("T");

            int lastIndex = CellReference.convertColStringToIndex("FJ");

            for (int j = 0; qIndex + j <= lastIndex; j += 5) {

                String qCol = CellReference.convertNumToColString(qIndex + j);
                LocalDate shipmentDate = excel.getLocalDate(row, qCol);

                if (shipmentDate == null) {
                    continue;
                }

                String sCol = CellReference.convertNumToColString(sIndex + j);
                LocalDate eta = excel.getFormulaLocalDate(row, sCol);

                String tCol = CellReference.convertNumToColString(tIndex + j);
                BigDecimal shippingQty = excel.getBigDecimal(row, tCol, evaluator);

                if (shippingQty == null || shippingQty.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                DpmShippingPlanData data = DpmShippingPlanData.builder()
                        .po(po)
                        .asNo(asNo)
                        .shipmentDate(shipmentDate)
                        .eta(eta)
                        .shippingQty(shippingQty)
                        .build();

                dataList.add(data);
            }



        }

        return dataList;
    }





}
