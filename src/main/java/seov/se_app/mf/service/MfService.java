package seov.se_app.mf.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seov.se_app.common.service.CommonQueryService;
import seov.se_app.mf.dto.request.MaterialRequestDataPu;
import seov.se_app.mf.dto.request.MaterialRequestList;
import seov.se_app.mf.dto.request.MaterialRequestSaveDto;
import seov.se_app.mf.entity.MfMaterialRequest;
import seov.se_app.mf.entity.MaterialRequestDetail;
import seov.se_app.mf.repository.MaterialRequestDetailRepository;
import seov.se_app.mf.repository.MfMaterialRequestRepository;
import seov.se_app.pe.repository.BomDataRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
    public class MfService {

    private final MaterialRequestDetailRepository materialRequestDetailRepository;
    private final MfMaterialRequestRepository materialRequestRepository;
    private final BomDataRepository bomDataRepository;
    private final CommonQueryService commonQueryService;


        public List<Map<String, Object>> getZCodeData() {
            String sql = """
            select A.* from (SELECT DISTINCT (production_number), registered_at FROM productinformation_parts_list limit 500) A order by registered_at desc
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

        String requestNo = generateRequestNo();
        if (dto.getZCode() != null && !dto.getZCode().isEmpty()) {
            zCodeString = String.join(",", dto.getZCode());
        }

        MfMaterialRequest request = MfMaterialRequest.builder()
                .requestNo(requestNo)
                .department(dto.getDepartment())
                .productionNumber(dto.getProductionNumber())
                .requestDate(dto.getRequestDate() != null ? dto.getRequestDate() : LocalDate.now())
                .status("SUBMITTED")
                .remark(dto.getRemark())
                .zCode(zCodeString)
                .createdBy(dto.getCreatedBy())
                .build();

        materialRequestRepository.save(request);

        List<MaterialRequestDetail> details = dto.getDetails().stream()
                .map(item -> MaterialRequestDetail.builder()
                        .requestNo(requestNo)
                        .itemCode(item.getItemCode())
                        .unit(item.getUnit())
                        .requestQty(item.getRequestQty())
                        .issuedQty(BigDecimal.ZERO)
                        .process(item.getProcess())
                        .materialType(item.getMaterialType())
                        .remark(item.getRemark())
                        .build())
                .toList();

        materialRequestDetailRepository.saveAll(details);

        return request;
    }


    private String generateRequestNo() {
        return "MR-" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + System.currentTimeMillis();
    }


    public  List<Map<String, Object>> getMaterialRequestData(MaterialRequestList request) {
        return  materialRequestRepository.getMaterialRequestData(request.getDepartment(), request.getFromDate(), request.getToDate());
    }


    public  List<Map<String, Object>> getBomData(String design_number) {
        return  bomDataRepository.getBomData(design_number);
    }



    }

