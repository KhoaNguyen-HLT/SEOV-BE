package seov.se_app.pe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pe.entity.BomData;

import java.util.Optional;


public interface BomDataRepository extends JpaRepository<BomData, Long> {
    Optional<BomData> findByItemCode(String itemCode);
//    @Query(value = """
//    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
//""", nativeQuery = true)
//    List<Map<String, Object>> getLotData();



}
