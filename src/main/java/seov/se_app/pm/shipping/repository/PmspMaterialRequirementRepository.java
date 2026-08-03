package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pm.shipping.entity.PmspMaterialDemand;
import seov.se_app.pm.shipping.entity.PmspMaterialRequirement;

import java.math.BigDecimal;
import java.util.List;


public interface PmspMaterialRequirementRepository extends JpaRepository<PmspMaterialRequirement, Long> {

    List<PmspMaterialRequirement> findByShippingQtyGreaterThan(BigDecimal qty);

    @Modifying
    @Query(value = "TRUNCATE TABLE pmsp_material_requirement", nativeQuery = true)
    void truncateTable();
}
