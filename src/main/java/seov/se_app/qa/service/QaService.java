package seov.se_app.qa.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.qa.dto.response.SubCableSnResponse;
import seov.se_app.qa.entity.IqcRlmData;
import seov.se_app.qa.entity.IqcRlmDataHis;
import seov.se_app.qa.repository.IqcRlmDataHisRepository;
import seov.se_app.qa.repository.IqcRlmDataRepository;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class QaService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private IqcRlmDataRepository iqcRlmDataRepository;
    @Autowired
    private IqcRlmDataHisRepository iqcRlmDataHisRepository;
    public List<IqcRlmData> getDataExcel(MultipartFile file){
        List<IqcRlmData> iqcRlmData = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            iqcRlmData = getData(sheet);
            iqcRlmDataRepository.deleteAllInBatch();
            iqcRlmDataRepository.saveAll(iqcRlmData);

            List<IqcRlmDataHis> hisData = iqcRlmData.stream()
                    .map(IqcRlmDataHis::new)
                    .toList();

            iqcRlmDataHisRepository.saveAll(hisData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return iqcRlmData;
    }


    public List<IqcRlmData> getData(Sheet sheet) {
        DataFormatter formatter = new DataFormatter();
        List<IqcRlmData> dataList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String requestNo = "IQC" + now.format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );
        String bs = "1310";

        for (int i = 5; i < 500; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            Cell firstCell = row.getCell(0);
            if (firstCell == null) {
                break;
            }
            String firstValue = formatter.formatCellValue(firstCell);
            // ô đầu tiên rỗng -> dừng
            if (firstValue.trim().isEmpty()) {
                break;
            }
//            đọc các giá trị từ trái qua phải của file để lấy thông tin chung
            String subCableSn = formatter.formatCellValue(row.getCell(0));
            String type = formatter.formatCellValue(row.getCell(2));
            String lotNo = formatter.formatCellValue(row.getCell(3));
            Cell cell_operation_time = row.getCell(4);
            String cell_value = cell_operation_time.getStringCellValue();
            DateTimeFormatter dt_formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime operationTime =
                    LocalDateTime.parse(cell_value, dt_formatter);
            String userId = formatter.formatCellValue(row.getCell(5));

            // đọc từ cột 7 -> 77 để lấy kết quả đo
            int a = 0;
            for (int j = 7; j <= 76; j += 3) {
                a = a+1;
                Cell cell = row.getCell(j);
                String value = formatter.formatCellValue(cell);
                // bỏ qua ô rỗng
                if (value.trim().isEmpty()) {
                    value = "null";
                }
                IqcRlmData data = new IqcRlmData();
                data.setRequestNo(requestNo);
                data.setLotNo(lotNo);
                data.setSubCableSn(subCableSn);
                data.setType(type);
                data.setBs(bs);
                data.setUserCode(userId);
                data.setSubCableNo(a);
                data.setResultNo(value);
                data.setOperationTime(operationTime);
                data.setCreated_at(LocalDateTime.now());
                data.setUpdated_at(LocalDateTime.now());
                dataList.add(data);
            }
        }
        return dataList;
    }


//    get lotdata
    public List<Map<String, Object>> getLotData(){
        List<Map<String, Object>> data = iqcRlmDataRepository.getLotData();
        return data;
    }



// lấy data ghi vào báo cáo excel với kiểu đo đa tâm master
    public String getReportMt(String lotA, String lotB) throws IOException {

//        String filePath = "D:\\PROJECT\\2.IQC_Project\\MasterFile.xlsx";
//        String filePath = "\\\\172.17.47.10\\Public\\04_QA\\IQC_SYSTEM\\report\\MasterFile.xlsx";
        String filePath = "/home/seov/QA-Inspection/6.Systems/1.report/MasterFile.xlsx";
        List<SubCableSnResponse> dataLa =
                iqcRlmDataRepository.getReport(lotA);
        List<SubCableSnResponse> dataLb =
                iqcRlmDataRepository.getReport(lotB);

        List<String> valuesLa =
                iqcRlmDataRepository.getResultNo(lotA);
        List<String> valuesLb =
                iqcRlmDataRepository.getResultNo(lotB);



        if (dataLa.size() != 24 || valuesLa.isEmpty() || dataLb.size() != 24 || valuesLb.isEmpty() ) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            fillColumnE(sheet, dataLa, "A");
            fillCellJ13(sheet, valuesLa, "A");
            fillColumnE(sheet, dataLb, "B");
            fillCellJ13(sheet, valuesLb, "B");

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }

        return "ok";
    }


    private void fillColumnE(Sheet sheet, List<SubCableSnResponse> data, String lot) {

        int startRow;
        int lotRowIndex;

        if ("A".equals(lot)) {
            startRow = 12;     // E13
            lotRowIndex = 4;   // E5
        } else {
            startRow = 36;     // E37
            lotRowIndex = 5;   // E6
        }

        // gán lot_no vào E5 hoặc E6
        Row lotRow = sheet.getRow(lotRowIndex);
        if (lotRow == null) {
            lotRow = sheet.createRow(lotRowIndex);
        }

        Cell lotCell = lotRow.getCell(4); // E
        if (lotCell == null) {
            lotCell = lotRow.createCell(4);
        }

        String lotNo = data.get(0).getLotNo();
        lotCell.setCellValue(lotNo);

        // đổ subCableSn vào cột E
        for (int i = 0; i < data.size() && i < 24; i++) {

            Row row = sheet.getRow(startRow + i);
            if (row == null) {
                row = sheet.createRow(startRow + i);
            }

            Cell cell = row.getCell(4); // E
            if (cell == null) {
                cell = row.createCell(4);
            }

            String value = data.get(i).getSubCableSn();
            cell.setCellValue(value != null ? value : "");
        }
    }


    private void fillCellJ13(Sheet sheet, List<String> values, String lot) {

        String startCell = "A".equals(lot) ? "J13" : "AH13";

        CellReference ref = new CellReference(startCell);

        int startRow = ref.getRow();
        int startCol = ref.getCol();

        int rowsPerColumn = 24;

        for (int i = 0; i < values.size(); i++) {

            int colOffset = i / rowsPerColumn;
            int rowOffset = i % rowsPerColumn;

            int rowIndex = startRow + rowOffset;
            int colIndex = startCol + colOffset;

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }

            cell.setCellValue(values.get(i) != null ? values.get(i) : "");
        }
    }
//    Kết thúc

// lấy dữ liệu data ra báo cáo đa tâm master random
    public String getReportRd(String lotA, String lotB) throws IOException {

//        String filePath = "\\\\172.17.47.15\\QA-Inspection\\6.Systems\\1.report\\MasterFile.xlsx";
        String filePath = "/home/seov/QA-Inspection/6.Systems/1.report/MasterFile.xlsx";
        String lotNo = lotA +"-"+lotB;

        List<String> values = iqcRlmDataRepository.getResultNoMtRd(lotNo);



        if (values.isEmpty() ) {
            return null;
        } else if(values.size() != 2400) {
            return "Không đúng định dạng Data";
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(1);

            fillCellU14MtRd(sheet, values);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }

        return "ok";
    }


    private void fillCellU14MtRd(Sheet sheet, List<String> values) {

        String startCell = "U14";

        CellReference ref = new CellReference(startCell);

        int startRow = ref.getRow();
        int startCol = ref.getCol();

        int rowsPerColumn = 24;

        for (int i = 0; i < values.size(); i++) {

            int colOffset = i / rowsPerColumn;
            int rowOffset = i % rowsPerColumn;

            int rowIndex = startRow + rowOffset;
            int colIndex = startCol + colOffset;

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }

            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                cell = row.createCell(colIndex);
            }

            sheet.getRow(13).getCell(20).setCellValue("TEST");

            cell.setCellValue(values.get(i) != null ? values.get(i) : "");
        }
    }









}
