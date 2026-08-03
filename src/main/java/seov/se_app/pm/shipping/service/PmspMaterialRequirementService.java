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
import java.util.ArrayList;
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

            LocalDate date = LocalDate.now();
            LocalDate pDate = date.plusDays(90);

        List<PmspMaterialRequirement> requirements = new ArrayList<>();


            // Lấy danh sách NVL có nhu cầu trong khoảng thời gian kế hoạch
            List<String> materialCodes =
                    materialDemandRepository
                            .findMaterialCodes(
                                    date,pDate
                            );

//        xóa dữ liệu đã tính toán cũ.
//            requirementRepository.deleteAllInBatch();
        requirementRepository.truncateTable();

            for (String materialCode : materialCodes) {
                PmspMaterialStandard standard =
                        pmspMaterialStandardRepository.findByMaterialCode(materialCode)
                                .orElseThrow(() -> new RuntimeException("Material standard not found:" + materialCode ));
                BigDecimal minQty = standard.getMinQty();
                BigDecimal maxQty = standard.getMaxQty();
//                Integer leadTime = standard.getLeadTime();
                BigDecimal moq = standard.getMoq();
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
                                        date,
                                        pDate
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
                                .divideToIntegralValue(moq)
                                .multiply(moq);

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
                    requirement.setPlanDate(date);

                    requirements.add(requirement);
                    if (requirements.size() == 5000) {
                        requirementRepository.saveAll(requirements);
                        requirements.clear();
                    }
                    stock = closingQty;

                }
            }
        if (!requirements.isEmpty()) {
            requirementRepository.saveAll(requirements);
        }
    }
}

