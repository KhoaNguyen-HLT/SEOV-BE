package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pm.shipping.entity.PmpsShippingPlan;
import seov.se_app.pm.shipping.entity.PmspPurchaseOrderAllocation;


public interface PmspPurchaseOrderAllocationRepository extends JpaRepository<PmspPurchaseOrderAllocation, Long> {

}
