package seov.se_app.pm.shipping.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.Now;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.component.ExcelCellReader;
import seov.se_app.common.repository.CompanyCalendarRepository;
import seov.se_app.pm.shipping.entity.*;
import seov.se_app.pm.shipping.repository.*;
import seov.se_app.pu.cfr.entity.CfrMaterial;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

    public class ShippingGetDataService {
    private final PmspMaterialDemandRepository pmspMaterialDemandRepository;
    private final PmspIvtSnapshotRepository pmspIvtSnapshotRepository;
    private final ExcelCellReader excelCellReader;



    @Transactional
    public int saveDemandData(MultipartFile file, String userName) {

        if (file == null || file.isEmpty()) {
            return 0;
        }

        final int BATCH_SIZE = 10000;

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {

            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            List<PmspMaterialDemand> demands = new ArrayList<>(BATCH_SIZE);

            pmspMaterialDemandRepository.deleteAll();

            int total = 0;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(0)).trim();

                if (itemCode.isBlank()) {
                    continue;
                }

                demands.add(
                        PmspMaterialDemand.builder()
                                .materialCode(itemCode)
                                .demandDate(excelCellReader.getLocalDate(row, "B"))
                                .demandQty(excelCellReader.getBigDecimal(row, "C", evaluator))
                                .build()
                );

                if (demands.size() == BATCH_SIZE) {
                    pmspMaterialDemandRepository.saveAll(demands);
                    demands.clear();
                }

                total++;
            }

            if (!demands.isEmpty()) {
                pmspMaterialDemandRepository.saveAll(demands);
            }

            return total;

        } catch (IOException e) {
            throw new RuntimeException("Import demand data failed.", e);
        }
    }

    @Transactional
    public int saveIvtData(MultipartFile file, String userName) {

        LocalDate now = LocalDate.now();

        if (file == null || file.isEmpty()) {
            return 0;
        }

        final int BATCH_SIZE = 1000;

        try (
                InputStream inputStream = file.getInputStream();
                Workbook workbook = new XSSFWorkbook(inputStream)
        ) {

            Sheet sheet = workbook.getSheetAt(0);
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            DataFormatter formatter = new DataFormatter();

            List<PmpsIvtSnapshot> ivData = new ArrayList<>(BATCH_SIZE);

            pmspIvtSnapshotRepository.deleteAll();

            int total = 0;

            for (int i = 2; i <= sheet.getLastRowNum(); i++) {

                Row row = sheet.getRow(i);

                if (row == null) {
                    continue;
                }

                String itemCode = formatter.formatCellValue(row.getCell(0)).trim();

                if (itemCode.isBlank()) {
                    continue;
                }

                ivData.add(
                        PmpsIvtSnapshot.builder()
                                .materialCode(itemCode)
                                .snapshotDate(now)
                                .quantity(excelCellReader.getBigDecimal(row, "B", evaluator))
                                .build()
                );

                if (ivData.size() == BATCH_SIZE) {
                    pmspIvtSnapshotRepository.saveAll(ivData);
                    ivData.clear();
                }

                total++;
            }

            if (!ivData.isEmpty()) {
                pmspIvtSnapshotRepository.saveAll(ivData);
            }

            return total;

        } catch (IOException e) {
            throw new RuntimeException("Import ivt data failed.", e);
        }
    }




}

