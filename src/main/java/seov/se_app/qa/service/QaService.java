package seov.se_app.qa.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.service.printExcelData;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.dto.request.DeviceGetRequest;
import seov.se_app.device.dto.request.DeviceUpdateRequest;
import seov.se_app.device.entity.Device;
import seov.se_app.device.mapper.DeviceMapper;
import seov.se_app.device.repository.DeviceRepository;
import seov.se_app.qa.entity.IqcRlmData;
import seov.se_app.qa.repository.IqcRlmDataRepository;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class QaService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private IqcRlmDataRepository iqcRlmDataRepository;
    public List<IqcRlmData> getDataExcel(MultipartFile file){
        List<IqcRlmData> iqcRlmData = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            iqcRlmData = getData(sheet);
            iqcRlmDataRepository.saveAll(iqcRlmData);
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
        String bs = "1315";

        for (int i = 5; i < 100; i++) {
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




}
