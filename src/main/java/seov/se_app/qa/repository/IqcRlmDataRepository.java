package seov.se_app.qa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seov.se_app.qa.dto.response.SubCableSnResponse;
import seov.se_app.qa.entity.IqcRlmData;

import java.util.List;
import java.util.Map;

public interface IqcRlmDataRepository extends JpaRepository<IqcRlmData, Long> {
    @Query(value = """
    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
""", nativeQuery = true)
    List<Map<String, Object>> getLotData();


    @Query("""
    select
        A.lotNo as lotNo,
        A.subCableSn as subCableSn
    from IqcRlmData A
    where A.lotNo = :lot
      and A.subCableNo = 1
    order by A.subCableSn
""")
    List<SubCableSnResponse> getReport(String lot);

    @Query("""
    select a.resultNo
        from IqcRlmData a
        where a.subCableSn in (
            select b.subCableSn
            from IqcRlmData b
            where b.lotNo = :lot
              and b.subCableNo = 1
        )
          and a.lotNo = :lot
        order by a.subCableSn
""")
    List<String> getResultNo(String lot);


    @Query("""
        select ird.resultNo
        from IqcRlmData ird
        where ird.type = 'IQC 24MT/24TMT Random'
          and ird.lotNo like concat(:lotNo, '%')
        order by ird.id, ird.lotNo, ird.subCableSn, ird.subCableNo
        """)
    List<String> getResultNoMtRd(String lotNo);







}
