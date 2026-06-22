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
    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.program_name = :program and A."type" like '%Master%'
""", nativeQuery = true)
    List<Map<String, Object>> getLotData(String program);


    @Query(value = """
    SELECT
        lot_no AS lotNo,
        sub_cable_sn AS subCableSn
    FROM iqc_rlm_data
    WHERE lot_no = :lot
      AND sub_cable_no = 1
      AND program_name = :programName
    ORDER BY sub_cable_sn
""", nativeQuery = true)
    List<SubCableSnResponse> getReport(
            @Param("lot") String lot,
            @Param("programName") String programName
    );

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
              and b.subCableNo = 1 and b.programName = :programName
        )
          and a.lotNo = :lot
        order by a.subCableSn, a.subCableNo
""")
    List<String> getResultNo(String lot, String programName);


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
     and measure_type = :msType and program_name = :programName
     and program_type = 'S' order by lot_no , iqc_rlm_data.sub_cable_sn
    """, nativeQuery = true)
    List<String> getResultNoSRd(int bs,String msType, String programName
    );


    @Query(value = """
    select result_no from iqc_rlm_data where "type" like '%Random%' 
     and bs = :bs 
     and sub_cable_no = 1  
     and measure_type = :msType and program_name = :programName
     and program_type = 'S' order by
        cast(right(lot_no, 1) as integer),
        case
            when substring(lot_no from 'RD([0-9X])[0-9]') = 'X' then 10
            else cast(substring(lot_no from 'RD([0-9])[0-9]') as integer)
        end, sub_cable_sn
    """, nativeQuery = true)
    List<String> getResultNoSRdSc(int bs,String msType, String programName
    );


    @Query("""
        select ird.resultNo
        from IqcRlmData ird
        where ird.type like '%Random%' and ird.programName = :programName
          and ird.lotNo like concat(:lotNo, '%')
        order by ird.id, ird.lotNo, ird.subCableSn, ird.subCableNo
        """)
    List<String> getResultNoMtRd(String lotNo, String programName);


    @Modifying
    @Transactional
    @Query("""
        delete from IqcRlmData a
        where a.programType = 'M' and programName = :ProgramName
    """)
    void deleteByProgramTypeM(String ProgramName);

    @Modifying
    @Transactional
    @Query("""
        delete from IqcRlmData a
        where a.programType = 'S' and a.programName = :programName
    """)
    void deleteByProgramTypeS(String programName);







}
