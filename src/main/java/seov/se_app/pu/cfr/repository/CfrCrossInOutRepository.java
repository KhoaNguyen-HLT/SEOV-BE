package seov.se_app.pu.cfr.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pu.cfr.entity.CfrCrossInOut;
import seov.se_app.pu.cfr.entity.CfrMaterial;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface CfrCrossInOutRepository extends JpaRepository<CfrCrossInOut, Long> {
    Optional<CfrCrossInOut> findByItemCode(String itemCode);
//    @Query(value = """
//    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
//""", nativeQuery = true)
//    List<Map<String, Object>> getLotData();
@Modifying
@Transactional
@Query("""
        delete from  CfrCrossInOut a
        where a.month = :month and a.documentType = :documentType and a.reportType = :reportName and period = :period
    """)
void deleteCrossInOutData( String month, String documentType, String reportName, String period);

    @Query(value = """
    select item_code from cfr_cross_inout where month = :month and report_type = :reportName and period = :period and document_type = :documentType limit 1
    """, nativeQuery = true)
    List<Map<String, Object>> checkExistedData(@Param("month") String month, @Param("reportName") String reportName,
                                               @Param("documentType") String documentType, @Param("period") String period);


}
