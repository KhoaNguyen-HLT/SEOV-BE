package seov.se_app.material.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.material.entity.InventoryTransactionFlow;

public interface InventoryTransactionFlowRepository extends JpaRepository<InventoryTransactionFlow, Long> {
    @Query("SELECT a FROM InventoryTransactionFlow a WHERE a.flowCode = :flowCode")
    InventoryTransactionFlow getTransactionFlow(@Param("flowCode") String flowCode);
}
