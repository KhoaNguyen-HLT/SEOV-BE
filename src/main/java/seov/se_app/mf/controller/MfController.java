package seov.se_app.mf.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seov.se_app.mf.dto.request.*;
import seov.se_app.mf.dto.response.*;
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


    @GetMapping("/getConsumptionData")
    ResponseEntity<ApiResponse<List<Map<String, Object>>>> getConsumptionData() {
        try {
            List<Map<String, Object>> Data =
                    mfService.getConsumptionData();

            return ResponseEntity.ok(
                    ApiResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(Data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<List<Map<String, Object>>>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }


    @GetMapping("/getProductData")
    ResponseEntity<ApiResponse<List<Map<String, Object>>>> getProductData() {
        try {
            List<Map<String, Object>> Data =
                    mfService.getProductData();

            return ResponseEntity.ok(
                    ApiResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .data(Data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<List<Map<String, Object>>>builder()
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
        try {
            List<Map<String, Object>> Result =
                    mfService.getDataPu(request);
            if(Result.size() == 1) {
                return ResponseEntity.ok(
                        ZCodeResponse.<List<Map<String, Object>>>builder()
                                .code(200)
                                .message("success")
                                .text(null)
                                .data(Result)
                                .build()
                );
            } else {
                return ResponseEntity.ok(
                        ZCodeResponse.<List<Map<String, Object>>>builder()
                                .code(200)
                                .message("error")
                                .text("Có nhiều hơn 1 model được")
                                .data(null)
                                .build()
                );
            }



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

    @PostMapping("/getMaterial")
    ResponseEntity<MaterialProductRequestResponse<List<MaterialProductDetail>>> getMaterial(
            @RequestBody List<productRequest> request
    ) {
        try {
            List<MaterialProductDetail> Result =
                    mfService.getMaterial(request);

            return ResponseEntity.ok(
                    MaterialProductRequestResponse.<List<MaterialProductDetail>>builder()
                            .code(200)
                            .message("success")
                            .text(null)
                            .data(Result)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    MaterialProductRequestResponse.<List<MaterialProductDetail>>builder()
                            .code(500)
                            .message("error")
                            .text("null")
                            .data(null)
                            .build()
            );
        }
    }


    @PostMapping("/updateIssuedMaterial")
    ResponseEntity<MaterialRequestResponse<MfMaterialRequest>> updateIssuedMaterial(
            @RequestBody MaterialRequestUpdateDto request
    ) {
        try {
            MfMaterialRequest Result =
                    mfService.updateIssuedMaterial(request);

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


    @PostMapping("/rejectRequest")
    ResponseEntity<MaterialRequestResponse<MfMaterialRequest>> rejectRequest(
            @RequestBody MaterialRequestRejectDto request
    ) {
        try {
            MfMaterialRequest Result =
                    mfService.rejectRequest(request);

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

    @PostMapping("/approveRequest")
    ResponseEntity<MaterialRequestResponse<MfMaterialRequest>> approveRequest(
            @RequestBody MaterialRequestUpdateDto request
    ) {
        try {
            MfMaterialRequest Result =
                    mfService.approvalMaterialRequest(request);

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


    @GetMapping("/prepareMaterialRequestData")
    ResponseEntity<MaterialRequestListResponse<List<Map<String, Object>>>> prepareMaterialRequestData(
            @RequestParam String design_number
    ) {
        try {
            List<Map<String, Object>> Result =
                    mfService.prepareMaterialRequestData(design_number);

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


    @GetMapping("/getDetailMaterialRequest")
    ResponseEntity<MaterialRequestListResponse<List<Map<String, Object>>>> getDetailMaterialRequest(
            @RequestParam String requestNo
    ) {
        try {
            List<Map<String, Object>> Result =
                    mfService.getDetailMaterialRequest(requestNo);
            List<Map<String, Object>> hdData =
                    mfService.getHeaderMaterialRequest(requestNo);
            List<Map<String, Object>> prData =
                    mfService.getMaterialAnRequest(requestNo);

            return ResponseEntity.ok(
                    MaterialRequestListResponse.<List<Map<String, Object>>>builder()
                            .code(200)
                            .message("success")
                            .text(null)
                            .data(Result)
                            .hdData(hdData)
                            .prData(prData)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    MaterialRequestListResponse.<List<Map<String, Object>>>builder()
                            .code(500)
                            .message("error")
                            .text(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }




    @GetMapping("/exportMaterialRequestExcel")
    public ResponseEntity<byte[]> exportMaterialRequestExcel(@RequestParam String requestNo) throws Exception {

        byte[] fileBytes = mfService.printMaterialExcelData(requestNo);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"Material_Request_" + requestNo + ".xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(fileBytes);
    }








}
