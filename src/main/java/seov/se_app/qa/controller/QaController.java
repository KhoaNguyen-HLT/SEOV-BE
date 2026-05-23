package seov.se_app.qa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.qa.service.QaService;
import seov.se_app.qa.entity.IqcRlmData;


import java.util.List;

@RestController
@RequestMapping("/qa")
public class QaController {
    @Autowired
    private QaService qaService;
    @PostMapping("/iqc/getDataExcel")
    public ResponseEntity<ApiResponse<List<IqcRlmData>>> getDataExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("user") String user,
            @RequestParam("status") String status) {
       List<IqcRlmData> iqcRlmData =  qaService.getDataExcel(file);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", iqcRlmData)
        );
    }
}
