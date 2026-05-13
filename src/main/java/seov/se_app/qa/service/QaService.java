package seov.se_app.qa.service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
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

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QaService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public String getDataExcel(MultipartFile file){
        LocalDateTime now = LocalDateTime.now();

        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // sheet đầu tiên
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println(sheet);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(file);
        return "ok";
    }




}
