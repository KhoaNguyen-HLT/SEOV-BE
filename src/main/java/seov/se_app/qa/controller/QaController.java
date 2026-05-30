package seov.se_app.qa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.respon.andonPendingResponse;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.qa.dto.response.qaResponse;
import seov.se_app.qa.service.QaService;
import seov.se_app.qa.entity.IqcRlmData;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qa")
public class QaController {
    @Autowired
    private QaService qaService;

    @GetMapping("/iqc/getLotData")
    ResponseEntity<qaResponse<List<Map<String, Object>>>> getLotData() {
        try {
            List<Map<String, Object>> LotData =
                    qaService.getLotData();

            return ResponseEntity.ok(
                    qaResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(LotData)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    qaResponse.<List<Map<String, Object>>>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }

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

    @PostMapping("/iqc/getReport")
    public ResponseEntity<ApiResponse<String>> getReport(
            @RequestParam String lotA,
            @RequestParam String lotB,
            @RequestParam String program) throws IOException {

        if("M".equals(program)) {
            String result =  qaService.getReportMt(lotA, lotB);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", result)
            );
        } else if("R".equals(program)) {
            String result1 =  qaService.getReportRd(lotA, lotB);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", result1)
            );
        } else {
            return ResponseEntity.ok(
                    new ApiResponse<>(500, "error", null)
            );
        }



    }


}
