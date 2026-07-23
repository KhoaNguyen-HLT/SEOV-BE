package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pm.shipping.entity.PmpsShippingPlanDetail;

import java.util.List;


public interface PmspShippingPlanDetailRepository extends JpaRepository<PmpsShippingPlanDetail, Long> {
    List<PmpsShippingPlanDetail> findBySpNo(String spNo);
}
