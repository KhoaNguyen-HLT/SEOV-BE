package seov.se_app.qa.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.qa.dto.response.SubCableSnResponse;
import seov.se_app.qa.entity.IqcRlmData;

import java.util.List;
import java.util.Map;

public interface IqcRlmDataRepository extends JpaRepository<IqcRlmData, Long> {
    @Query(value = """
    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.program_type = :program and A."type" like '%Master%'
""", nativeQuery = true)
    List<Map<String, Object>> getLotData(String program);


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

    @Query(value = """
    SELECT DISTINCT ON (A.sub_cable_sn)
        A.lot_no AS lotNo,
        A.sub_cable_sn AS subCableSn
    FROM iqc_rlm_data A
    WHERE A.lot_no = :lot
      AND A.sub_cable_no = 1
    ORDER BY A.sub_cable_sn, A.id
    """, nativeQuery = true)
    List<SubCableSnResponse> getReportS(@Param("lot") String lot);

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
        order by a.subCableSn, a.subCableNo
""")
    List<String> getResultNo(String lot);


    @Query(value = """
    SELECT result_no
    FROM iqc_rlm_data ird
    WHERE bs = 1310
      AND ird.measure_type = 'IL'
      AND type LIKE '%Master%'
      AND ird.lot_no IN (:lotA, :lotB)

    UNION ALL

    SELECT result_no
    FROM iqc_rlm_data ird
    WHERE bs = 1310
      AND ird.measure_type = 'RL'
      AND type LIKE '%Master%'
      AND ird.lot_no IN (:lotA, :lotB)

    UNION ALL

    SELECT result_no
    FROM iqc_rlm_data ird
    WHERE bs = 1550
      AND ird.measure_type = 'IL'
      AND type LIKE '%Master%'
      AND ird.lot_no IN (:lotA, :lotB)

    UNION ALL

    SELECT result_no
    FROM iqc_rlm_data ird
    WHERE bs = 1550
      AND ird.measure_type = 'RL'
      AND type LIKE '%Master%'
      AND ird.lot_no IN (:lotA, :lotB)
    """, nativeQuery = true)
    List<String> getResultNoS(
            @Param("lotA") String lotA,
            @Param("lotB") String lotB
    );


    @Query(value = """
    select result_no from iqc_rlm_data where "type" like '%Random%' 
     and bs = :bs 
     and sub_cable_no = 1  
     and measure_type = :msType
     and program_type = 'S' order by lot_no , iqc_rlm_data.sub_cable_sn
    """, nativeQuery = true)
    List<String> getResultNoSRd(int bs,String msType
    );


    @Query("""
        select ird.resultNo
        from IqcRlmData ird
        where ird.type like '%Random%'
          and ird.lotNo like concat(:lotNo, '%')
        order by ird.id, ird.lotNo, ird.subCableSn, ird.subCableNo
        """)
    List<String> getResultNoMtRd(String lotNo);


    @Modifying
    @Transactional
    @Query("""
        delete from IqcRlmData a
        where a.programType = 'M'
    """)
    void deleteByProgramTypeM();

    @Modifying
    @Transactional
    @Query("""
        delete from IqcRlmData a
        where a.programType = 'S'
    """)
    void deleteByProgramTypeS();







}
