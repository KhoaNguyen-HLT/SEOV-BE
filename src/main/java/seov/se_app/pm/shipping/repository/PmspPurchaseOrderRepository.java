package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pm.shipping.entity.PmspPurchaseOrder;

import java.util.List;


public interface PmspPurchaseOrderRepository extends JpaRepository<PmspPurchaseOrder, Long> {

    List<PmspPurchaseOrder> findByMaterialCodeOrderByPoDateAsc(String materialCode
    );
}
