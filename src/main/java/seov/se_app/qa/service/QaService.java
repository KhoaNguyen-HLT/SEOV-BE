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
import java.util.LinkedHashMap;
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
    public List<IqcRlmData> getDataExcel(MultipartFile file, String programName){
        List<IqcRlmData> iqcRlmData = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            iqcRlmData = getData(sheet, programName);
            iqcRlmDataRepository.deleteByProgramTypeM(programName);
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

//lấy data file đo đa tâm.
    public List<IqcRlmData> getData(Sheet sheet, String programName) {
        List<Row> rows =  processSheet(sheet);

        DataFormatter formatter = new DataFormatter();
        List<IqcRlmData> dataList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        String requestNo = "IQC" + now.format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );
        int bs = 1310;
        String pType = "M";

        for (Row row : rows) {
            String subCableSn = formatter.formatCellValue(row.getCell(0));
            String type = formatter.formatCellValue(row.getCell(2));
            String lotNo = formatter.formatCellValue(row.getCell(3));

            Cell cellOperationTime = row.getCell(4);
            String cellValue = formatter.formatCellValue(cellOperationTime).trim();

            DateTimeFormatter dtFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime operationTime =
                    LocalDateTime.parse(cellValue, dtFormatter);

            String userId = formatter.formatCellValue(row.getCell(5));

            int subCableNo = 0;
            int lastCol;
            if("24MT".equals(programName)) {
                lastCol = 76;
            } else {
                lastCol = 40;
            }

            for (int j = 7; j <= lastCol; j += 3) {
                subCableNo++;

                String value = formatter.formatCellValue(row.getCell(j)).trim();

                if (value.isEmpty()) {
                    value = "null";
                }

                IqcRlmData data = new IqcRlmData();
                data.setRequestNo(requestNo);
                data.setLotNo(lotNo);
                data.setSubCableSn(subCableSn);
                data.setType(type);
                data.setProgramType(pType);
                data.setProgramName(programName);
                data.setBs(bs);
                data.setUserCode(userId);
                data.setSubCableNo(subCableNo);
                data.setResultNo(value);
                data.setOperationTime(operationTime);
                data.setCreated_at(LocalDateTime.now());
                data.setUpdated_at(LocalDateTime.now());

                dataList.add(data);
            }
        }
        return dataList;
    }

//    xử lý file excel với trường hợp trùng dòng dữ liệu.
public List<Row> processSheet(Sheet sheet) {
    Map<String, Row> map = new LinkedHashMap<>();
    DataFormatter formatter = new DataFormatter();

    for (int i = 5; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) continue;

        String a = formatter.formatCellValue(row.getCell(0)).trim();
        String d = formatter.formatCellValue(row.getCell(3)).trim();

        if (a.isEmpty() && d.isEmpty()) {
            continue;
        }

        String key = a + "|" + d;

        // nếu trùng A+D thì lấy cả dòng mới nhất
        map.put(key, row);
    }

    List<Row> finalRows = new ArrayList<>(map.values());

    if (finalRows.size() != 148) {
        throw new RuntimeException(
                "Dữ liệu chuẩn phải có 148 dòng, hiện tại: " + finalRows.size()
        );
    }

    return finalRows;
}


//    get lotdata
    public List<Map<String, Object>> getLotData(String program){
        List<Map<String, Object>> data = iqcRlmDataRepository.getLotData(program);
        return data;
    }




    String filePath24MT = "/home/seov/QA-Inspection/6.Systems/1.report/MasterFile24MT.xlsx";
    String filePath12MT = "/home/seov/QA-Inspection/6.Systems/1.report/MasterFile12MT.xlsx";

//    String filePath24MT = "\\\\172.17.47.10\\Public\\04_QA\\IQC_SYSTEM\\report\\MasterFile24MT.xlsx";
//    String filePath12MT = "\\\\172.17.47.10\\Public\\04_QA\\IQC_SYSTEM\\report\\MasterFile12MT.xlsx";

// ==========================
// MASTER 24MT / 12MT
// ==========================

    public String getReportMt24MT(String lotA, String lotB, String programName) throws IOException {
        return getReportMt(lotA, lotB, programName, filePath24MT, 24);
    }

    public String getReportMt12MT(String lotA, String lotB, String programName) throws IOException {
        return getReportMt(lotA, lotB, programName, filePath12MT, 12);
    }

    private String getReportMt(
            String lotA,
            String lotB,
            String programName,
            String filePath,
            int rowsPerColumn
    ) throws IOException {

        List<SubCableSnResponse> dataLa =
                iqcRlmDataRepository.getReport(lotA, programName);

        List<SubCableSnResponse> dataLb =
                iqcRlmDataRepository.getReport(lotB, programName);

        List<String> valuesLa =
                iqcRlmDataRepository.getResultNo(lotA, programName);

        List<String> valuesLb =
                iqcRlmDataRepository.getResultNo(lotB, programName);

        if (dataLa.size() != 24 || dataLb.size() != 24
                || valuesLa.isEmpty() || valuesLb.isEmpty()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            fillColumnE(sheet, dataLa, "A");
            fillCellsByColumn(sheet, valuesLa, "J13", rowsPerColumn);

            fillColumnE(sheet, dataLb, "B");
            fillCellsByColumn(sheet, valuesLb, "AH13", rowsPerColumn);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }

        return "ok";
    }


// ==========================
// RANDOM 24MT / 12MT
// ==========================

    public String getReportRd24MT(String lotA, String lotB, String programName) throws IOException {
        return getReportRd(lotA, lotB, programName, filePath24MT, 24, 2400);
    }

    public String getReportRd12MT(String lotA, String lotB, String programName) throws IOException {
        return getReportRd(lotA, lotB, programName, filePath12MT, 12, 1200);
    }

    private String getReportRd(
            String lotA,
            String lotB,
            String programName,
            String filePath,
            int rowsPerColumn,
            int expectedSize
    ) throws IOException {

        String lotNo = lotA + "-" + lotB;

        List<String> values =
                iqcRlmDataRepository.getResultNoMtRd(lotNo, programName);

        if (values.isEmpty()) {
            return null;
        }

        if (values.size() != expectedSize) {
            return "Không đúng định dạng Data";
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(1);

            fillCellsByColumn(sheet, values, "U14", rowsPerColumn);

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }

        return "ok";
    }


// ==========================
// COMMON EXCEL FUNCTIONS
// ==========================

    private void fillColumnE(
            Sheet sheet,
            List<SubCableSnResponse> data,
            String lot
    ) {

        int startRow;
        int lotRowIndex;

        if ("A".equals(lot)) {
            startRow = 12;      // E13
            lotRowIndex = 4;    // E5
        } else {
            startRow = 36;      // E37
            lotRowIndex = 5;    // E6
        }

        String lotNo = data.get(0).getLotNo();
        setCellValue(sheet, lotRowIndex, 4, lotNo);

        for (int i = 0; i < data.size() && i < 24; i++) {
            setCellValue(
                    sheet,
                    startRow + i,
                    4,
                    data.get(i).getSubCableSn()
            );
        }
    }

    private void fillCellsByColumn(
            Sheet sheet,
            List<String> values,
            String startCell,
            int rowsPerColumn
    ) {

        CellReference ref = new CellReference(startCell);

        int startRow = ref.getRow();
        int startCol = ref.getCol();

        for (int i = 0; i < values.size(); i++) {

            int colOffset = i / rowsPerColumn;
            int rowOffset = i % rowsPerColumn;

            int rowIndex = startRow + rowOffset;
            int colIndex = startCol + colOffset;

            setCellValue(sheet, rowIndex, colIndex, values.get(i));
        }
    }

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

        cell.setCellValue(value != null ? value : "");
    }









}
