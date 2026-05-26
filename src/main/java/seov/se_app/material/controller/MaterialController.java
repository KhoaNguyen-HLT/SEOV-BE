package seov.se_app.material.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seov.auth.dto.respone.ApiResponse;
import seov.se_app.device.entity.Device;
import seov.se_app.material.dto.request.MaterialRqNoRequest;
import seov.se_app.material.entity.MaterialRequest;
import seov.se_app.material.service.MaterialService;
import seov.se_app.material.entity.InventoryTransactionFlow;

import java.util.List;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @GetMapping("/getTransactionFlow/{flowCode}")
    ResponseEntity<ApiResponse<InventoryTransactionFlow>>  getTransactionFlow(@PathVariable String flowCode) {
        InventoryTransactionFlow InventoryTransactionFlow =  materialService.getTransactionFlow(flowCode);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", InventoryTransactionFlow)
        );
    }

    @GetMapping("/getMaterialRequest/{flowCode}")
    ResponseEntity<ApiResponse<List<MaterialRequest>>> getMaterialRequest(@PathVariable String flowCode) {
        List<MaterialRequest> MaterialRequest =  materialService.getMaterialRequest(flowCode);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", MaterialRequest)
        );
    }


    @PostMapping("/createMaterialRequest")
    private ResponseEntity<ApiResponse<MaterialRequest>>  createMaterialRequest(@RequestBody MaterialRqNoRequest request) {
        MaterialRequest materialRequest =  materialService.CreatMaterialRequest(request);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", materialRequest)
        );
    }


}
