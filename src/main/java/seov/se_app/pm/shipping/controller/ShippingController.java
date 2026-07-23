package seov.se_app.pm.shipping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pm.shipping.service.PmspMaterialRequirementService;
import seov.se_app.pm.shipping.service.ShippingService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shipping")
public class ShippingController {
private final PmspMaterialRequirementService pmspMaterialRequirementService;
    private final ShippingService shippingService;
    @GetMapping("/calculateMaterialRequirement")
    public ResponseEntity<ApiResponse<String>> calculateMaterialRequirement(
    ) {
        pmspMaterialRequirementService.calculateMaterialRequirement();
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
}
