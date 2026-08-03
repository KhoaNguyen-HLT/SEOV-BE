package seov.se_app.pm.shipping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pm.shipping.service.PmspMaterialRequirementService;
import seov.se_app.pm.shipping.service.ShippingGetDataService;
import seov.se_app.pm.shipping.service.ShippingService;
import seov.se_app.pu.cfr.dto.response.puReportResponse;
import seov.se_app.pu.cfr.entity.CfrTransInventory;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pm/shipping")
public class ShippingController {
    private final PmspMaterialRequirementService pmspMaterialRequirementService;
    private final ShippingService shippingService;
    private final ShippingGetDataService shippingGetDataService;

    @PostMapping("/getShippingData")
    public ResponseEntity<ApiResponse<String>> getShippingData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("month") String month,
            @RequestParam("userName") String userName,
            @RequestParam("fileName") String fileName
    ) {

        boolean success = false;

            switch (fileName) {

                case "DEMAND" -> {
                    int num = shippingGetDataService.saveDemandData(file, userName);
                    if (num > 0) {
                        success = true;
                    }
                }

                case "STOCK" -> {
                    int num = shippingGetDataService.saveIvtData(file, userName);
                    if (num > 0) {
                        success = true;
                    }
                }

                default -> {
                    return ResponseEntity.ok(
                            new ApiResponse<>(400, "Sai tên File import:" + fileName , null)
                    );
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


    @GetMapping("/calculateMaterialRequirement")
    public ResponseEntity<ApiResponse<String>> calculateMaterialRequirement(
    ) {

        try {
            pmspMaterialRequirementService.calculateMaterialRequirement();
//            String spNo = shippingService.generateShippingPlan();

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", "1")
            );

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new ApiResponse<>(400, e.getMessage(), null)
            );
        }
    }

    @GetMapping("/generateShippingPlan")
    public ResponseEntity<ApiResponse<String>> generateShippingPlan(
    ) {
        String spNo = shippingService.generateShippingPlan();
        try {

            return ResponseEntity.ok(
                    new ApiResponse<>(200, "success", null)
            );

        } catch (Exception e) {
            return ResponseEntity.ok(
                    new ApiResponse<>(400, e.getMessage(), null)
            );
        }
    }


    @GetMapping("/getShippingPlanData")
    ResponseEntity<ApiResponse<List<Map<String,Object>>>> getHisData(
            @RequestParam String supplier,
            @RequestParam String date) {
        List<Map<String,Object>> data = shippingService.getShippingPlanData(supplier,date );
        if(data.size() > 0) {
            return ResponseEntity.ok(
                    ApiResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.<List<Map<String, Object>>>builder()
                            .code(400)
                            .message("error")
                            .data(null)
                            .build()
            );
        }


    }
}
