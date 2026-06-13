package seov.se_app.pu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pu.entity.CfrMaterial;

import java.util.Optional;


public interface CfrMaterialsRepository extends JpaRepository<CfrMaterial, Long> {
    Optional<CfrMaterial> findByItemCode(String itemCode);
//    @Query(value = """
//    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
//""", nativeQuery = true)
//    List<Map<String, Object>> getLotData();



}
