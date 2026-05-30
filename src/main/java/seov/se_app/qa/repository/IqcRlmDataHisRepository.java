package seov.se_app.qa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.qa.entity.IqcRlmDataHis;


public interface IqcRlmDataHisRepository extends JpaRepository<IqcRlmDataHis, Long> {
//    @Query(value = """
//    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
//""", nativeQuery = true)
//    List<Map<String, Object>> getLotData();



}
