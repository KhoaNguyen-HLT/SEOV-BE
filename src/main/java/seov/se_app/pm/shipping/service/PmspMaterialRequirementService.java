package seov.se_app.pm.shipping.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seov.se_app.pm.shipping.entity.PmspMaterialDemand;
import seov.se_app.pm.shipping.entity.PmspMaterialRequirement;
import seov.se_app.pm.shipping.entity.PmspMaterialStandard;
import seov.se_app.pm.shipping.repository.PmspIvtSnapshotRepository;
import seov.se_app.pm.shipping.repository.PmspMaterialDemandRepository;
import seov.se_app.pm.shipping.repository.PmspMaterialRequirementRepository;
import seov.se_app.pm.shipping.repository.PmspMaterialStandardRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PmspMaterialRequirementService {

    private final PmspIvtSnapshotRepository pmspIvtSnapshotRepository;
    private final PmspMaterialDemandRepository materialDemandRepository;
    private final PmspMaterialRequirementRepository requirementRepository;
    private final PmspMaterialStandardRepository pmspMaterialStandardRepository;


    @Transactional
    public void calculateMaterialRequirement(
    ) {
        LocalDate planDate = LocalDate.of(2026, 7, 17);
        // Lấy danh sách NVL có nhu cầu trong khoảng thời gian kế hoạch
        List<String> materialCodes =
                materialDemandRepository
                        .findMaterialCodes(
                                planDate.plusDays(1),
                                planDate.plusDays(90)
                        );


        for (String materialCode : materialCodes) {
            PmspMaterialStandard standard =
                    pmspMaterialStandardRepository.findByMaterialCode(materialCode)
                            .orElseThrow(() -> new RuntimeException("Material standard not found"));
            BigDecimal minQty = standard.getMinQty();
            BigDecimal maxQty = standard.getMaxQty();
            Integer leadTime = standard.getLeadTime();
            // 1. Lấy tồn đầu kỳ
            BigDecimal stock =
                    pmspIvtSnapshotRepository
                            .findQuantityByMaterialCodeAndSnapshotDate(
                                    materialCode
                            );

            if (stock == null) {
//                stock = BigDecimal.ZERO;
                continue;
            }

            // 2. Lấy nhu cầu theo ngày
            List<PmspMaterialDemand> demands =
                    materialDemandRepository
                            .findByMaterialCodeAndDemandDateBetween(
                                    materialCode,
                                    planDate.plusDays(1),
                                    planDate.plusDays(90)
                            );

            // 3. Tính tồn chạy theo ngày
            for (PmspMaterialDemand demand : demands) {
//             lấy tồn kho
                BigDecimal openingQty = stock;
//           lây gia tri can sản xuat
                BigDecimal demandQty = demand.getDemandQty();
//           lay gia tri cuoi ngay
                BigDecimal closingQty =
                        openingQty.subtract(demandQty);
                BigDecimal shippingQty = BigDecimal.ZERO;

//             nếu tồn cuối ngày mà < min
                if (closingQty.compareTo(minQty) < 0) {
                    shippingQty =
                            maxQty.subtract(closingQty);
//              lam tron xuong de chia het cho moq
                    shippingQty = shippingQty
                            .divideToIntegralValue(BigDecimal.valueOf(80))
                            .multiply(BigDecimal.valueOf(80));

                    closingQty = closingQty.add(shippingQty);
                }
                PmspMaterialRequirement requirement =
                        new PmspMaterialRequirement();

                requirement.setDemandDate(
                        demand.getDemandDate()
                );

                requirement.setMaterialCode(materialCode);

                requirement.setOpeningQty(openingQty);

                requirement.setDemandQty(demandQty);

                requirement.setClosingQty(closingQty);

                requirement.setShippingQty(shippingQty);

                requirementRepository.save(requirement);

                stock = closingQty;


            }
        }
    }
}

