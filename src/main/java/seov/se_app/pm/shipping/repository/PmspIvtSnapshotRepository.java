package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pm.shipping.entity.PmpsIvtSnapshot;

import java.math.BigDecimal;


public interface PmspIvtSnapshotRepository extends JpaRepository<PmpsIvtSnapshot, Long> {

    /**
     * Lấy tồn đầu ngày
     */
    @Query("""
            SELECT i.quantity
            FROM PmpsIvtSnapshot i
            WHERE i.materialCode = :materialCode
           """)
    BigDecimal findQuantityByMaterialCodeAndSnapshotDate(
            @Param("materialCode") String materialCode
    );


}
