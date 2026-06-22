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
import seov.se_app.qa.service.QaServiceSigle;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qa")
public class QaController {
    @Autowired
    private QaService qaService;
    @Autowired
    private QaServiceSigle qaServiceSigle;


    @GetMapping("/iqc/getLotData/{program}")
    ResponseEntity<qaResponse<List<Map<String, Object>>>> getLotData(@PathVariable String program) {
        try {
            List<Map<String, Object>> LotData =
                    qaService.getLotData(program);

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
            @RequestParam("program") String program) {

        if("24MT".equals(program)) {
            List<IqcRlmData> iqcRlmData =  qaService.getDataExcel(file, program);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", iqcRlmData)
            );
        }
        else if ("12MT".equals(program)) {
            List<IqcRlmData> iqcRlmData =  qaService.getDataExcel(file, program);
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", iqcRlmData)
            );
        } else if ("LC-ULC".equals(program)) {
            List<IqcRlmData> iqcRlmData =  qaServiceSigle.getDataExcel(file, "LC-ULC");
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", iqcRlmData)
            );

        } else if ("LC-SC".equals(program)) {
            List<IqcRlmData> iqcRlmData =  qaServiceSigle.getDataExcel(file, "LC-SC");
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", iqcRlmData)
            );

        }
        else {
            return ResponseEntity.ok(
                    new ApiResponse<>(500, "error", null)
            );
        }

    }

    @PostMapping("/iqc/getReport")
    public ResponseEntity<ApiResponse<String>> getReport(
            @RequestParam String lotA,
            @RequestParam String lotB,
            @RequestParam String msTypeRp,
            @RequestParam String program) throws IOException {
//        check xem là loại chương trình đơn tâm hay đa tâm
//        nếu là đa tâm check tiếp xem loại báo cáo nào?

        if("24MT".equals(program)) {
//    bao cao hang da tam 24MT
            if ("M".equals(msTypeRp)) {
                String result = qaService.getReportMt24MT(lotA, lotB, program);
                if (result == null) {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "error", result)
                    );
                }
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", result)
                );
            } else if ("R".equals(msTypeRp)) {
                String result1 = qaService.getReportRd24MT(lotA, lotB, program);
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", result1)
                );
            } else {
                return ResponseEntity.ok(
                        new ApiResponse<>(500, "error", null)
                );
            }
        }
 // kieu do da tam 12MT
        else if("12MT".equals(program)){

            if ("M".equals(msTypeRp)) {
                String result = qaService.getReportMt12MT(lotA, lotB, program);
                if (result == null) {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "error", result)
                    );
                }
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", result)
                );
            } else if ("R".equals(msTypeRp)) {
                String result1 = qaService.getReportRd12MT(lotA, lotB, program);
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", result1)
                );
            } else {
                return ResponseEntity.ok(
                        new ApiResponse<>(500, "error", null)
                );
            }

// kieu do dơn tam LC-ULC
        } else if("LC-ULC".equals(program)) {
                String result =  qaServiceSigle.getReportULC(lotA, lotB);
                if (result == null) {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "error", result)
                    );
                }
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", result)
                );
  // kieu do dơn tam LC-SC
        } else if ("LC-SC".equals(program)) {
                String result =  qaServiceSigle.getReportSC(lotA, lotB);
                if (result == null) {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "error", result)
                    );
                }
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", result)
                );
        } else {
            return ResponseEntity.ok(
                    new ApiResponse<>(500, "error", null)
            );
        }

    }


}
