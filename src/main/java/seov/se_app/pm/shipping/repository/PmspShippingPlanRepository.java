package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pm.shipping.entity.PmpsShippingPlan;


public interface PmspShippingPlanRepository extends JpaRepository<PmpsShippingPlan, Long> {

}
