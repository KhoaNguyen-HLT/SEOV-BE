package seov.se_app.pm.shipping.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hpsf.Decimal;
import org.springframework.stereotype.Service;
import seov.se_app.common.repository.CompanyCalendarRepository;
import seov.se_app.pm.shipping.entity.*;
import seov.se_app.pm.shipping.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

    public class ShippingService {
    private final PmspMaterialRequirementRepository pmspMaterialRequirementRepository;
    private final PmspShippingPlanRepository pmpsShippingPlanRepository;
    private final PmspShippingPlanDetailRepository pmpsShippingPlanDetailRepository;
    private final CompanyCalendarRepository companyCalendarRepository;
    private final PmspPurchaseOrderRepository pmspPurchaseOrderRepository;
    private final PmspPurchaseOrderAllocationRepository pmspPurchaseOrderAllocationRepository;


    @Transactional
    public String generateShippingPlan() {

//      lấy được thông tin danh sách ngày đến hạn cần giao
        List<PmspMaterialRequirement> materialRequirements =
                pmspMaterialRequirementRepository.findByShippingQtyGreaterThan(BigDecimal.ZERO);
        String spNo = generatePlanNo();
        PmpsShippingPlan plan = new PmpsShippingPlan();
        plan.setSpNo(spNo);

        pmpsShippingPlanRepository.save(plan);

        List<PmpsShippingPlanDetail> details = new ArrayList<>();

        for (PmspMaterialRequirement materialRequirement : materialRequirements) {
            LocalDate deliveryDate = companyCalendarRepository.getDeliveryDate(materialRequirement.getDemandDate(), "SP01");

            PmpsShippingPlanDetail planDetail = new PmpsShippingPlanDetail();
            planDetail.setSpNo(spNo);
            planDetail.setMaterialCode(materialRequirement.getMaterialCode());
            planDetail.setNeedDate(materialRequirement.getDemandDate());
            planDetail.setDeliveryDate(deliveryDate);
            planDetail.setSupplierCode("SP01");
            planDetail.setDeliveryQty(materialRequirement.getShippingQty());

            details.add(planDetail);

        }
        pmpsShippingPlanDetailRepository.saveAll(details);
        allocate(spNo);

        return spNo;

    }


    public String generatePlanNo() {

        return "SP" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    @Transactional
    public void allocate(String spNo) {

        // Xóa allocation cũ nếu generate lại
//        pmspPurchaseOrderAllocationRepository.deleteBySpNo(spNo);

        List<PmpsShippingPlanDetail> details =
                pmpsShippingPlanDetailRepository.findBySpNo(spNo);

        List<PmspPurchaseOrderAllocation> allocations = new ArrayList<>();

        for (PmpsShippingPlanDetail detail : details) {

            List<PmspPurchaseOrder> purchaseOrders =
                    pmspPurchaseOrderRepository
                            .findByMaterialCodeOrderByPoDateAsc(
                                    detail.getMaterialCode());

            // Không có PO
            if (purchaseOrders.isEmpty()) {

                PmspPurchaseOrderAllocation allocation =
                        new PmspPurchaseOrderAllocation();

                allocation.setSpNo(detail.getSpNo());
                allocation.setPo(null);
                allocation.setAllocatedQty(BigDecimal.ZERO);

                allocations.add(allocation);
                continue;
            }

            BigDecimal remain = detail.getDeliveryQty();

            for (PmspPurchaseOrder po : purchaseOrders) {
//          So sánh giá trị với 0
                if (remain.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
// lấy giá trị remain trong Po còn có thể phân bổ
                BigDecimal poRemain = po.getOrderQty();

                if (poRemain.compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

// kiểm tra xem giá trị nào nhỏ thì lấy giá trị đó
                BigDecimal allocated = remain.min(poRemain);

                PmspPurchaseOrderAllocation allocation =
                        new PmspPurchaseOrderAllocation();

                allocation.setSpNo(detail.getSpNo());
                allocation.setPo(po.getPoNo());
                allocation.setAllocatedQty(allocated);

                allocations.add(allocation);

                remain = remain.subtract(allocated);

                po.setRemainQty(poRemain.subtract(allocated));

            }

            // Không đủ PO
            if (remain.compareTo(BigDecimal.ZERO) > 0) {

                PmspPurchaseOrderAllocation allocation =
                        new PmspPurchaseOrderAllocation();

                allocation.setSpNo(detail.getSpNo());
                allocation.setPo(null);
                allocation.setAllocatedQty(remain);

                allocations.add(allocation);
            }
        }

        pmspPurchaseOrderAllocationRepository.saveAll(allocations);
    }


    }

