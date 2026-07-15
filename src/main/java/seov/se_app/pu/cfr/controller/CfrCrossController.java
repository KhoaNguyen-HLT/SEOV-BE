package seov.se_app.pu.cfr.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pu.cfr.dto.request.CfrUpdateIvtRequest;
import seov.se_app.pu.cfr.dto.response.puReportResponse;
import seov.se_app.pu.cfr.entity.*;
import seov.se_app.pu.cfr.service.CfrCrossService;
import seov.se_app.pu.cfr.service.CfrService;
import seov.se_app.pu.cfr.service.CfrTrans15aService;
import seov.se_app.pu.cfr.service.CfrTransService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pu/cfr/cross")
public class CfrCrossController {
    @Autowired
    private CfrService cfrService;
    private final CfrCrossService cfrCrossService;

    @PostMapping("/getCrossInOutData")
    public ResponseEntity<ApiResponse<String>> getCrossInOutData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName
    ) {

        boolean success = false;
        String fileName = "";
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (baseName) {

                case "ie_data_pu" -> {
                    List<CfrCrossInOut> cfrCrossInOuts = cfrCrossService.saveCrossInOutData(file, "IE_DATA_PU", month, reportName);
                    if (!cfrCrossInOuts.isEmpty()) {
                        success = true;
                    }
                }

                case "acceptance_gscm" -> {
                    List<CfrCrossInOut> cfrCrossInOuts = cfrCrossService.saveCrossInOutData(file, "ACCEPTANCE_GSCM", month, reportName);
                    if (!cfrCrossInOuts.isEmpty()) {
                        success = true;
                    }
                }

                case "fg_pm" -> {
                    List<CfrCrossInOut> cfrCrossInOuts = cfrCrossService.saveCrossInOutData(file, "FG_PM", month, reportName);
                    if (!cfrCrossInOuts.isEmpty()) {
                        success = true;
                    }
                }
                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "Sai tên File import:" + fileName , null)
                    );
                }
            }
        }

        if (success) {
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", null)
            );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(400, "Lỗi định dạng hoặc sai tên File: " + fileName, null)
        );
    }


    @PostMapping("/getCrossIvtData")
    public ResponseEntity<ApiResponse<String>> getCrossIvtData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName
    ) {

        boolean success = false;
        String fileName = "";
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (baseName) {

                case "inventory_mf" -> {
                    List<CfrCrossInventory> cfrCrossInventories = cfrCrossService.saveCrossIvtData(file, "IVT_MF", month, reportName);
                    if (!cfrCrossInventories.isEmpty()) {
                        success = true;
                    }
                }

                case "ending_pm" -> {
                    List<CfrCrossInventory> cfrCrossInventories = cfrCrossService.saveCrossIvtData(file, "ENDING_PM", month, reportName);
                    if (!cfrCrossInventories.isEmpty()) {
                        success = true;
                    }
                }

                case "fg_mf" -> {
                    List<CfrCrossInventory> cfrCrossInventories = cfrCrossService.saveCrossIvtData(file, "FG_MF", month, reportName);
                    if (!cfrCrossInventories.isEmpty()) {
                        success = true;
                    }
                }

                case "fg_pm" -> {
                    List<CfrCrossInventory> cfrCrossInventories = cfrCrossService.saveCrossIvtData(file, "FG_PM", month, reportName);
                    if (!cfrCrossInventories.isEmpty()) {
                        success = true;
                    }
                }
                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "Sai tên File import:" + fileName , null)
                    );
                }
            }
        }

        if (success) {
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", null)
            );
        }

        return ResponseEntity.ok(
                new ApiResponse<>(400, "Lỗi định dạng hoặc sai tên File: " + fileName, null)
        );
    }




    @PostMapping("/checkExistedCrossInOutData")
    public ResponseEntity<ApiResponse<String>> checkExistedData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName
    ) {
        String fileName = "";
        String documentType = "";
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (baseName) {

                case "ie_data_pu" -> {
                    documentType = "IE_DATA_PU";
                }

                case "acceptance_gscm" -> {
                    documentType = "ACCEPTANCE_GSCM";
                }

                case "fg_pm" -> {
                   documentType = "FG_PM";
                }
                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "Sai tên File import:" + fileName , null)
                    );
                }
            }
        }


        List<Map<String, Object>> data = cfrCrossService.checkExistedCrossInOutData(month, reportName,documentType );
        if(data.size() > 0) {
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code(200)
                            .message("existed")
                            .data(null)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code(200)
                            .message("no_exist")
                            .data(null)
                            .build()
            );
        }

    }


    @PostMapping("/checkExistedCrossIvtData")
    public ResponseEntity<ApiResponse<String>> checkExistedCrossIvtData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName

    ) {
        String fileName = "";
        String documentType = "";
        for (MultipartFile file : files) {
            fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (baseName) {

                case "inventory_mf" -> {
                    documentType = "IVT_MF";
                }

                case "ending_pm" -> {
                     documentType = "ENDING_PM";
                }

                case "fg_mf" -> {
                    documentType = "FG_MF";
                }

                case "fg_pm" -> {
                    documentType = "FG_PM";
                }
                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "Sai tên File import:" + fileName , null)
                    );
                }
            }
        }

        List<Map<String, Object>> data = cfrCrossService.checkExistedCrossIvtData(month, reportName,documentType );
        if(data.size() > 0) {
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code(200)
                            .message("existed")
                            .data(null)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.<String>builder()
                            .code(200)
                            .message("no_exist")
                            .data(null)
                            .build()
            );
        }

    }





}
