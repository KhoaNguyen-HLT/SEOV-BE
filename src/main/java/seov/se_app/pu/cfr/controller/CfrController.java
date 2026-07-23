package seov.se_app.pu.cfr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pu.cfr.dto.request.CfrUpdateIvtRequest;
import seov.se_app.pu.cfr.entity.CfrMaterial;
import seov.se_app.pu.cfr.entity.CfrOpenInventory;
import seov.se_app.pu.cfr.entity.CfrTransInventory;
import seov.se_app.pu.cfr.service.CfrService;
import seov.se_app.pu.cfr.service.CfrTrans15aService;
import seov.se_app.pu.cfr.service.CfrTransService;
import seov.se_app.pu.cfr.dto.response.puReportResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pu")
public class CfrController {
    @Autowired
    private CfrService cfrService;

    @Autowired
    private CfrTransService cfrTransService;

    @Autowired
    private CfrTrans15aService cfrTrans15aService;


    @PostMapping("/cfr/getMasterData")
    public ResponseEntity<ApiResponse<String>> getMasterData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("reportName") String reportName,
            @RequestParam("userName") String userName
            ) {

        boolean success = false;

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (fileName) {

                case "Masterlist_PU" -> {
                    List<CfrMaterial> materials = cfrService.saveMaterialData(file,reportName, userName);
                    if (!materials.isEmpty()) {
                        success = true;
                    }
                }

                case "CFR_PU" -> {
                    List<CfrOpenInventory> openInventories = cfrService.saveOpenInventory(file, reportName, userName);
                    if (!openInventories.isEmpty()) {
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
                new ApiResponse<>(400, "error", null)
        );
    }


    @PostMapping("/cfr/getTransData")
    public ResponseEntity<ApiResponse<String>> getTransData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName,
            @RequestParam("userName") String userName
            ) {

        boolean success = false;

        for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (baseName) {

                case "customs_data_pu" -> {
                    List<CfrTransInventory> materials = cfrTransService.saveTransData(file, "DATA_PU", month, reportName, userName);
                    if (!materials.isEmpty()) {
                        success = true;
                    }
                }

                case "inventory_mf" -> {
                    List<CfrTransInventory> openInventories = cfrTransService.saveTransData(file, "IVT_MF", month, reportName, userName);
                    if (!openInventories.isEmpty()) {
                        success = true;
                    }
                }
                case "others_mf" -> {
                    List<CfrTransInventory> openInventories = cfrTransService.saveTransData(file, "OTHER_MF", month, reportName, userName);
                    if (!openInventories.isEmpty()) {
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
                new ApiResponse<>(400, "error", null)
        );
    }

    @PostMapping("/cfr/getTransData15a")
    public ResponseEntity<ApiResponse<String>> getTransData15a(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName,
            @RequestParam("userName") String userName
    ) {

        boolean success = false;

        for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");
            baseName = baseName.substring(5);

            switch (baseName) {

                case "customs_data_pu" -> {
                    List<CfrTransInventory> materials = cfrTrans15aService.saveTransData(file, "DATA_PU", month, reportName, userName);
                    if (!materials.isEmpty()) {
                        success = true;
                    }
                }

                case "fg_mf" -> {
                    List<CfrTransInventory> openInventories = cfrTrans15aService.saveTransData(file, "FG_MF", month, reportName, userName);
                    if (!openInventories.isEmpty()) {
                        success = true;
                    }
                }
                case "others_mf" -> {
                    List<CfrTransInventory> openInventories = cfrTrans15aService.saveTransData(file, "OTHER_MF", month, reportName, userName);
                    if (!openInventories.isEmpty()) {
                        success = true;
                    }
                }

                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "Sai tên File import:" + fileName, null)
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
                new ApiResponse<>(400, "error", null)
        );
    }

    @GetMapping("/cfr/getData")
    ResponseEntity<puReportResponse<List<Map<String,Object>>>> getData(
            @RequestParam String reportName,
            @RequestParam String month ) {
            List<Map<String, Object>> data = cfrService.getData(reportName, month);
            if(data.size() > 0) {
                return ResponseEntity.ok(
                        puReportResponse.<List<Map<String, Object>>>builder()
                                .code(200)
                                .message("success")
                                .data(data)
                                .build()
                );
            } else {
                return ResponseEntity.ok(
                        puReportResponse.<List<Map<String, Object>>>builder()
                                .code(400)
                                .message("error")
                                .data(null)
                                .build()
                );
            }


    }


    @GetMapping("/cfr/getHisData")
    ResponseEntity<puReportResponse<List<Map<String,Object>>>> getHisData(
            @RequestParam String reportName) {
        List<Map<String, Object>> data = cfrService.getHisData(reportName);
        if(data.size() > 0) {
            return ResponseEntity.ok(
                    puReportResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    puReportResponse.<List<Map<String, Object>>>builder()
                            .code(400)
                            .message("error")
                            .data(null)
                            .build()
            );
        }


    }

    @PostMapping("/cfr/updateOpenInventory")
    public ResponseEntity<ApiResponse<List<CfrOpenInventory>>> updateOpenInventory(
           @RequestBody CfrUpdateIvtRequest request
    ) {

        List<CfrOpenInventory> data = cfrService.updateOpenInventory(request.getReportName(), request.getMonth());
        if (data.size() >= 0) {
            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", data)
            );
        } else {
            return ResponseEntity.ok(
                    new ApiResponse<>(400, "error", null)
            );
        }


    }


    @GetMapping("/cfr/checkExistedData")
    public ResponseEntity<ApiResponse<String>> checkExistedData(
            @RequestParam("month") String month,
            @RequestParam("reportName") String reportName
    ) {
        List<Map<String, Object>> data = cfrService.checkExistedData(month, reportName);
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
