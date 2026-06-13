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
import java.util.*;

@Service
public class QaServiceSigle {

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
            iqcRlmDataRepository.deleteByProgramTypeS();
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

//lấy data file đo đơn tâm.
    public List<IqcRlmData> getData(Sheet sheet) {
        List<Row> rows = processSheet(sheet);

        DataFormatter formatter = new DataFormatter();
        List<IqcRlmData> dataList = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        String requestNo = "IQC" + now.format(
                DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        );

        String pType = "S";

        String[] columns = {"H", "I", "K", "L", "EV", "EW", "EY", "EZ"};

        Set<String> cable1Cols = Set.of("H", "I", "EV", "EW");
        Set<String> bs1310Cols = Set.of("H", "I", "K", "L");
        Set<String> ilCols = Set.of("H", "K", "EV", "EY");

        DateTimeFormatter dtFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Row row : rows) {

            String subCableSn = formatter.formatCellValue(row.getCell(0)).trim();
            String type = formatter.formatCellValue(row.getCell(2)).trim();
            String lotNo = formatter.formatCellValue(row.getCell(3)).trim();
            String userId = formatter.formatCellValue(row.getCell(5)).trim();

            Cell operationTimeCell = row.getCell(4);
            LocalDateTime operationTime = null;

            if (operationTimeCell != null) {
                if (operationTimeCell.getCellType() == CellType.NUMERIC
                        && DateUtil.isCellDateFormatted(operationTimeCell)) {

                    operationTime = operationTimeCell.getLocalDateTimeCellValue();

                } else {

                    String cellValue =
                            formatter.formatCellValue(operationTimeCell).trim();

                    if (!cellValue.isEmpty()) {
                        operationTime = LocalDateTime.parse(
                                cellValue,
                                dtFormatter
                        );
                    }
                }
            }

            for (String col : columns) {

                int colIndex = CellReference.convertColStringToIndex(col);
                Cell cell = row.getCell(colIndex);

                String value = formatter.formatCellValue(cell).trim();

                if (value.isEmpty()) {
                    value = null;
                }

                int subCableNo = cable1Cols.contains(col) ? 1 : 2;
                int bs = bs1310Cols.contains(col) ? 1310 : 1550;
                String msType = ilCols.contains(col) ? "IL" : "RL";

                IqcRlmData data = new IqcRlmData();
                data.setRequestNo(requestNo);
                data.setLotNo(lotNo);
                data.setSubCableSn(subCableSn);
                data.setType(type);
                data.setProgramType(pType);
                data.setMeasureType(msType);
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

        if (finalRows.size() != 240) {
            throw new RuntimeException(
                    "Dữ liệu chuẩn phải có 240 dòng, hiện tại: " + finalRows.size()
            );
        }

        return finalRows;
    }

    //        String filePath = "D:\\PROJECT\\2.IQC_Project\\MasterFile.xlsx";
        String filePath = "\\\\172.17.47.10\\Public\\04_QA\\IQC_SYSTEM\\report\\MasterFileS.xlsx";
//    String filePath = "/home/seov/QA-Inspection/6.Systems/1.report/MasterFileS.xlsx";
// lấy data ghi vào báo cáo excel với kiểu đo đơn tâm master
    public String getReportMt(String lotA, String lotB) throws IOException {


        List<SubCableSnResponse> dataLa =
                iqcRlmDataRepository.getReportS(lotA);
        List<SubCableSnResponse> dataLb =
                iqcRlmDataRepository.getReportS(lotB);
        List<String> values =
                iqcRlmDataRepository.getResultNoS(lotA, lotB);
        List<String> values1310IL =
                iqcRlmDataRepository.getResultNoSRd(1310, "IL");
        List<String> values1310RL =
                iqcRlmDataRepository.getResultNoSRd(1310, "RL");
        List<String> values1550IL =
                iqcRlmDataRepository.getResultNoSRd(1550, "IL");
        List<String> values1550RL =
                iqcRlmDataRepository.getResultNoSRd(1550, "RL");




        if (dataLa.size() != 20 || dataLb.size() != 20 || values.size() != 320) {
            return "Data lỗi vui lòng kiểm tra lại";
        }

        if (values1310IL.size() != 200 || values1310RL.size() != 200 || values1550IL.size() != 200 || values1550RL.size() != 200) {
            return "Data lỗi vui lòng kiểm tra lại";
        }

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            fillColumnE(sheet, dataLa, "A");
            fillColumnE(sheet, dataLb, "B");
            fillCellJ12(sheet, values, "B");
            getReportSRd(sheet,values1310IL,1310,"IL");
            getReportSRd(sheet,values1310RL,1310,"RL");
            getReportSRd(sheet,values1550IL,1550,"IL");
            getReportSRd(sheet,values1550RL,1550,"RL");




            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
        }

        return "ok";
    }

//lấy data do vao cot E
    private void fillColumnE(Sheet sheet, List<SubCableSnResponse> data, String lot) {

        int startRow;
        int lotRowIndex;

        if ("A".equals(lot)) {
            startRow = 15;     // E16
            lotRowIndex = 4;   // E5
        } else {
            startRow = 35;     // E36
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
        for (int i = 0; i < data.size() && i < 20; i++) {

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


    private void fillCellJ12(Sheet sheet, List<String> values, String lot) {

        String startCell = "J12";

        CellReference ref = new CellReference(startCell);

        int startRow = ref.getRow();
        int startCol = ref.getCol();

        int rowsPerColumn = 40;

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


    // lấy dữ liệu data ra báo cáo đơn tâm random
    public void getReportSRd(Sheet sheet,List<String> values, int bs, String msType) throws IOException {
        String startCell;

        if (bs == 1310 && "IL".equals(msType)) {
            startCell = "U6";
        } else if (bs == 1310 && "RL".equals(msType)) {
            startCell = "AI6";
        } else if (bs == 1550 && "IL".equals(msType)) {
            startCell = "U33";
        } else if (bs == 1550 && "RL".equals(msType)) {
            startCell = "AI33";
        } else {
            throw new IllegalArgumentException(
                    "Không hỗ trợ bs=" + bs + ", msType=" + msType
            );
        }

        CellReference ref = new CellReference(startCell);

        int startRow = ref.getRow();
        int startCol = ref.getCol();

        int colsPerRow = 10;

        for (int i = 0; i < values.size(); i++) {

            int rowOffset = i / colsPerRow;
            int colOffset = i % colsPerRow;

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









}
