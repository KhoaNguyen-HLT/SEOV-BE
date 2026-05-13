package seov.se_app.qa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.auth.dto.respone.ApiResponse;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.dto.request.DeviceGetRequest;
import seov.se_app.device.dto.request.DeviceUpdateRequest;
import seov.se_app.device.entity.Device;
import seov.se_app.device.service.DeviceService;
import seov.se_app.qa.service.QaService;

import java.util.List;

@RestController
@RequestMapping("/qa")
public class QaController {
    @Autowired
    private QaService qaService;
    @PostMapping("/iqc/getDataExcel")
    public String getDataExcel(@RequestParam("file") MultipartFile file) {
       String device =  qaService.getDataExcel(file);
//        return ResponseEntity.ok(
//                new ApiResponse<>(200, "success", device)
//        );
        return "ok";
    }
}
