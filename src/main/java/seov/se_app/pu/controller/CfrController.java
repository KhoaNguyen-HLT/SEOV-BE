package seov.se_app.pu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pu.entity.CfrMaterial;
import seov.se_app.pu.entity.CfrOpenInventory;
import seov.se_app.pu.entity.CfrTransInventory;
import seov.se_app.pu.service.CfrService;
import seov.se_app.pu.service.CfrTransService;
import seov.se_app.pu.dto.response.puReportResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pu")
public class CfrController {
    @Autowired
    private CfrService cfrService;

    @Autowired
    private CfrTransService cfrTransService;


    @PostMapping("/cfr/getMasterData")
    public ResponseEntity<ApiResponse<String>> getMasterData(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("reportName") String reportName
            ) {

        boolean success = false;

        for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename();

            switch (fileName) {

                case "Masterlist_PU.xlsx" -> {
                    List<CfrMaterial> materials = cfrService.saveMaterialData(file);
                    if (!materials.isEmpty()) {
                        success = true;
                    }
                }

                case "FY25_CFR_PU.xlsx" -> {
                    List<CfrOpenInventory> openInventories = cfrService.saveOpenInventory(file, reportName);
                    if (!openInventories.isEmpty()) {
                        success = true;
                    }
                }

                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "error", null)
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
            @RequestParam("month") String month
            ) {

        boolean success = false;

        for (MultipartFile file : files) {

            String fileName = file.getOriginalFilename().toLowerCase();
            String baseName = fileName
                    .replace(".xlsx", "")
                    .replace(".xls", "");

            switch (baseName) {

                case "customs_data_pu" -> {
                    List<CfrTransInventory> materials = cfrTransService.saveTransData(file, "DATA_PU", month);
                    if (!materials.isEmpty()) {
                        success = true;
                    }
                }

                case "inventory_mf" -> {
                    List<CfrTransInventory> openInventories = cfrTransService.saveTransData(file, "IVT_MF", month);
                    if (!openInventories.isEmpty()) {
                        success = true;
                    }
                }
                case "others_mf" -> {
                    List<CfrTransInventory> openInventories = cfrTransService.saveTransData(file, "OTHER_MF", month);
                    if (!openInventories.isEmpty()) {
                        success = true;
                    }
                }

                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "error", null)
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

            return ResponseEntity.ok(
                    puReportResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );

    }


}
