package seov.se_app.pu.dpm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.pu.cfr.entity.CfrMaterial;
import seov.se_app.pu.dpm.entity.DpmGscmData;

import java.util.Optional;


public interface DpmGscmDataRepository extends JpaRepository<DpmGscmData, Long> {
    Optional<DpmGscmData> findByPo(String po);
//    @Query(value = """
//    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
//""", nativeQuery = true)
//    List<Map<String, Object>> getLotData();



}
