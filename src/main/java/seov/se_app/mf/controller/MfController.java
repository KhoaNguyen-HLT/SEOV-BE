package seov.se_app.mf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seov.se_app.common.dto.response.ApiResponse;
import seov.se_app.common.service.CommonQueryService;
import seov.se_app.mf.dto.response.ZCodeResponse;
import seov.se_app.mf.service.mfService;


import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mf")
public class MfController {
    private final mfService mfService;


    @GetMapping("/getZCodeData")
    ResponseEntity<ZCodeResponse<List<Map<String, Object>>>> getLotData(
            @RequestParam("zCode") String zCode ) {
        try {
            List<Map<String, Object>> LotData =
                    mfService.getZCodeData(zCode);

            return ResponseEntity.ok(
                    ZCodeResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(LotData)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ZCodeResponse.<List<Map<String, Object>>>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }




}
