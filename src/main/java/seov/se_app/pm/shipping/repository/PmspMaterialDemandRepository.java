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
    @Query(value = """
    select A.* from (SELECT DISTINCT d.material_code 
      FROM pmsp_material_demand d
      WHERE d.demand_date  BETWEEN :fromDate AND :toDate) A
    inner join pmsp_material_standard B on A.material_code = B.material_code 
    """, nativeQuery = true)
    List<String> findMaterialCodes(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );


    /**
     * Lấy nhu cầu của 1 NVL theo khoảng ngày
     */
    List<PmspMaterialDemand> findByMaterialCodeAndDemandDateBetween(
            String materialCode,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

}
