package seov.se_app.pu.dpm.controller;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pu.dpm.entity.DpmGscmData;
import seov.se_app.pu.dpm.entity.DpmShippingPlanData;
import seov.se_app.pu.dpm.service.DpmService;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/pu/dpm")
@RequiredArgsConstructor
public class DpmController {
    private final DpmService dpmService;


    @PostMapping("/getGscmData")
    private ResponseEntity<ApiResponse<String>> getGscmData(
            @RequestParam("files") MultipartFile files,
            @RequestParam("dataType") String dataType
            ) {
        if("step1".equals(dataType)) {
            List<DpmGscmData> dpmGscmData = dpmService.saveGscmData(files, dataType);

            if(!dpmGscmData.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", "get  gscm data success")
                );
            } else {
                return ResponseEntity.ok(
                        new ApiResponse<>(400, "error", null)
                );
            }


        } else {
            List<DpmShippingPlanData> dpmShippingPlanData = dpmService.saveShippingPlanData(files, dataType);

            if(!dpmShippingPlanData.isEmpty()) {
                return ResponseEntity.ok(
                        new ApiResponse<>(200, "success", "get shiping plan success")
                );
            } else {
                return ResponseEntity.ok(
                        new ApiResponse<>(400, "error", null)
                );
            }


        }

    }




}
