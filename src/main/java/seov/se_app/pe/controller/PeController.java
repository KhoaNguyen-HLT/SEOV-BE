package seov.se_app.pe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.pe.dto.response.PeResponse;
import seov.se_app.pe.entity.BomData;
import seov.se_app.pe.service.BomService;
import seov.se_app.pu.service.CfrTransService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pe")
public class PeController {
    @Autowired
    private BomService BomService;



    @PostMapping("/getBomData")
    public ResponseEntity<ApiResponse<String>> getBomData(
            @RequestParam("file") MultipartFile file
            ) {

        try {
            BomService.saveBomData(file);
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
