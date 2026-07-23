package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pm.shipping.entity.PmpsIvtSnapshot;
import seov.se_app.pm.shipping.entity.PmspMaterialStandard;

import java.math.BigDecimal;
import java.util.Optional;


public interface PmspMaterialStandardRepository extends JpaRepository<PmspMaterialStandard, Long> {

    Optional<PmspMaterialStandard> findByMaterialCode(String materialCode);

}
