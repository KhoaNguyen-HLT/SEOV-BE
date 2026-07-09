package seov.se_app.mf.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seov.se_app.common.service.CommonQueryService;
import seov.se_app.mf.dto.request.*;
import seov.se_app.mf.entity.MfMaterialRequest;
import seov.se_app.mf.entity.MaterialRequestDetail;
import seov.se_app.mf.enums.MaterialRequestStatus;
import seov.se_app.mf.repository.MaterialRequestDetailRepository;
import seov.se_app.mf.repository.MfMaterialRequestRepository;
import seov.se_app.pe.repository.BomDataRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static seov.se_app.mf.enums.MaterialRequestStatus.SUBMITTED;

@Service
@RequiredArgsConstructor
    public class MfService {

    private final MaterialRequestDetailRepository materialRequestDetailRepository;
    private final MfMaterialRequestRepository materialRequestRepository;
    private final BomDataRepository bomDataRepository;
    private final CommonQueryService commonQueryService;
    private final PrintMaterialExcelData printMaterialExcelData;



        public List<Map<String, Object>> getZCodeData() {
            String sql = """
            select A.* from (SELECT DISTINCT (production_number) FROM productinformation_parts_list limit 10000) A order by production_number desc
        """;

            Map<String, Object> params = new HashMap<>();
//            params.put("productionNumber", productionNumber);

            return commonQueryService.queryThirdDb(sql, params);
        }

    public List<Map<String, Object>> getDataPu(MaterialRequestDataPu request) {
        String sql = """
            SELECT distinct(A.design_number)
                    FROM order_process_use_parts A
                    WHERE production_number IN (:zCode)
        """;

        Map<String, Object> params = new HashMap<>();
            params.put("zCode", request.getZCodes());
        List<Map<String, Object>> data = commonQueryService.queryThirdDb(sql, params);

        return data;

    }


    @Transactional
    public MfMaterialRequest saveRequest(MaterialRequestSaveDto dto) {
        String zCodeString = null;
        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new RuntimeException("Chi tiết yêu cầu NVL không được trống");
        }

        String requestNo = generateRequestNo( dto.getDepartment() );
        if (dto.getZCode() != null && !dto.getZCode().isEmpty()) {
            zCodeString = String.join(",", dto.getZCode());
        }

        MfMaterialRequest request = MfMaterialRequest.builder()
                .requestNo(requestNo)
                .department(dto.getDepartment())
                .productionNumber(dto.getProductionNumber())
                .requestNeedDate(dto.getRequestDate() != null ? dto.getRequestDate() : LocalDateTime.now())
                .requiredTime(dto.getRequestDate().toLocalTime())
                .qtyRequest(dto.getQtyRequest())
                .status(SUBMITTED)
                .remark(dto.getRemark())
                .zCode(zCodeString)
                .createdBy(dto.getCreatedBy())
                .build();

        materialRequestRepository.save(request);

        List<MaterialRequestDetail> details = dto.getDetails().stream()
                .map(item -> MaterialRequestDetail.builder()
                        .requestNo(requestNo)
                        .materialCode(item.getMaterialCode())
                        .unit(item.getUnit())
                        .qtyOrder(item.getQtyOrder())
                        .issuedQty(BigDecimal.ZERO)
                        .process(item.getProcess())
                        .materialType(item.getMaterialType())
                        .remark(item.getRemark())
                        .build())
                .toList();

        materialRequestDetailRepository.saveAll(details);

        return request;
    }


    @Transactional
    public MfMaterialRequest updateIssuedMaterial(MaterialRequestUpdateDto dto) {

        if (dto.getDetails() == null || dto.getDetails().isEmpty()) {
            throw new RuntimeException("Chi tiết yêu cầu NVL không được trống");
        }

        MfMaterialRequest request = materialRequestRepository
                .findByRequestNo(dto.getRequestNo())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        request.setStatus(MaterialRequestStatus.COMPLETED);
        request.setUpdatedBy(dto.getUpdatedBy());
        request.setIssuedBy(dto.getUpdatedBy());
        request.setIssuedAt(LocalDateTime.now());

        for (MaterialRequestDetailDto item : dto.getDetails()) {
            MaterialRequestDetail detail = materialRequestDetailRepository
                    .findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy dòng detail id: " + item.getId()));

            detail.setIssuedQty(item.getIssuedQty());
            detail.setRemark(item.getRemark());
        }


        return request;
    }


    @Transactional
    public MfMaterialRequest rejectRequest(MaterialRequestRejectDto dto) {

        MfMaterialRequest request = materialRequestRepository
                .findByRequestNo(dto.getRequestNo())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        request.setStatus(MaterialRequestStatus.REJECTED);
        request.setUpdatedBy(dto.getRejectedBy());
        request.setRejectedBy(dto.getRejectedBy());
        request.setRejectedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());
        request.setRejectReason(dto.getRejectReason());


        return request;
    }





    @Transactional
    public MfMaterialRequest approvalMaterialRequest(MaterialRequestUpdateDto dto) {

        MfMaterialRequest request = materialRequestRepository
                .findByRequestNo(dto.getRequestNo())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        request.setStatus(MaterialRequestStatus.APPROVED);
        request.setUpdatedBy(dto.getUpdatedBy());
        request.setApprovedBy(dto.getUpdatedBy());
        request.setApprovedAt(LocalDateTime.now());
        request.setUpdatedAt(LocalDateTime.now());


        return request;
    }










    private String generateRequestNo(String department) {
        LocalDate today = LocalDate.now();

        Long count = materialRequestRepository.countRequestInDay(
                today.atStartOfDay(),
                today.plusDays(1).atStartOfDay()
        );
        return department+ "-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" +  String.format("%03d", count + 1);
    }


    public  List<Map<String, Object>> getMaterialRequestData(MaterialRequestList request) {
        LocalDateTime from = request.getFromDate().atStartOfDay();
        LocalDateTime to = request.getToDate().plusDays(1).atStartOfDay();
        return  materialRequestRepository.getMaterialRequestData(request.getDepartment(), from, to, request.getStatus());
    }


    public  List<Map<String, Object>> prepareMaterialRequestData(String design_number) {
        return  materialRequestRepository.prepareMaterialRequestData(design_number);
    }

    public  List<Map<String, Object>> getDetailMaterialRequest(String requestNo) {
        return  materialRequestDetailRepository.getDetailMaterialRequest(requestNo);
    }

    public  List<Map<String, Object>> getHeaderMaterialRequest(String requestNo) {
        return  materialRequestRepository.getHeaderMaterialRequest(requestNo);
    }

    public byte[] printMaterialExcelData(String requestNo) throws Exception {

        List<MaterialRequestReportProjection> reportData =
                materialRequestDetailRepository.getReportMaterialRequest(requestNo);
        return printMaterialExcelData.exportExcel(reportData);
    }








    }

