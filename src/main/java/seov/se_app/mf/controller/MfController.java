package seov.se_app.mf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seov.se_app.mf.dto.request.MaterialRequestDataPu;
import seov.se_app.mf.dto.request.MaterialRequestList;
import seov.se_app.mf.dto.request.MaterialRequestSaveDto;
import seov.se_app.mf.dto.response.MaterialRequestListResponse;
import seov.se_app.mf.dto.response.MaterialRequestResponse;
import seov.se_app.mf.dto.response.ZCodeResponse;
import seov.se_app.mf.entity.MfMaterialRequest;
import seov.se_app.mf.service.MfService;


import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mf")
public class MfController {
    private final MfService mfService;


    @GetMapping("/getZCodeData")
    ResponseEntity<ZCodeResponse<List<Map<String, Object>>>> getLotData() {
        try {
            List<Map<String, Object>> LotData =
                    mfService.getZCodeData();

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


    @PostMapping("/getDataPu")
    ResponseEntity<ZCodeResponse<List<Map<String, Object>>>> getDataPu(
            @RequestBody MaterialRequestDataPu request
            ) {
        System.out.println(request.getZCodes());
        try {
            String Result =
                    mfService.getDataPu(request);
            if("OK".equals(Result)) {

            }

            return ResponseEntity.ok(
                    ZCodeResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .text(Result)
                            .data(null)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ZCodeResponse.<List<Map<String, Object>>>builder()
                            .code(500)
                            .message("error")
                            .text("null")
                            .data(null)
                            .build()
            );
        }
    }


    @PostMapping("/createOrder")
    ResponseEntity<MaterialRequestResponse<MfMaterialRequest>> saveDataRequest(
            @RequestBody MaterialRequestSaveDto request
    ) {
        try {
            MfMaterialRequest Result =
                    mfService.saveRequest(request);

            return ResponseEntity.ok(
                    MaterialRequestResponse.<MfMaterialRequest>builder()
                            .code(200)
                            .message("success")
                            .text(null)
                            .data(Result)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    MaterialRequestResponse.<MfMaterialRequest>builder()
                            .code(500)
                            .message("error")
                            .text("null")
                            .data(null)
                            .build()
            );
        }
    }


    @PostMapping("/getMaterialRequestData")
    ResponseEntity<MaterialRequestListResponse<List<Map<String, Object>>>> getMaterialRequestData(
            @RequestBody MaterialRequestList request
            ) {
        try {
            List<Map<String, Object>> Result =
                    mfService.getMaterialRequestData(request);

            return ResponseEntity.ok(
                    MaterialRequestListResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .text(null)
                            .data(Result)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    MaterialRequestListResponse.<List<Map<String, Object>>>builder()
                            .code(500)
                            .message("error")
                            .text("null")
                            .data(null)
                            .build()
            );
        }
    }




}
