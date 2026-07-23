package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pm.shipping.entity.PmpsIvtSnapshot;
import seov.se_app.pm.shipping.entity.PmspMaterialDemand;

import java.time.LocalDate;
import java.util.List;


public interface PmspMaterialDemandRepository extends JpaRepository<PmspMaterialDemand, Long> {


    /**
     * Lấy danh sách NVL có nhu cầu trong khoảng thời gian
     */
    @Query("""
            SELECT DISTINCT d.materialCode
            FROM PmspMaterialDemand d
            WHERE d.demandDate BETWEEN :fromDate AND :toDate
           """)
    List<String> findMaterialCodes(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );


    /**
     * Lấy nhu cầu của 1 NVL theo khoảng ngày
     */
    List<PmspMaterialDemand> findByMaterialCodeAndDemandDateBetween(
            String materialCode,
            LocalDate fromDate,
            LocalDate toDate
    );

}
