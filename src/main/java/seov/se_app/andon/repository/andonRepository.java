package seov.se_app.andon.repository;

import org.antlr.v4.runtime.atn.SemanticContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.entity.andondata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface andonRepository extends JpaRepository<andondata, Long> {
    @Query("SELECT a FROM andondata a JOIN FETCH a.group WHERE a.id = :id")
    andondata findWithGroup(@Param("id") Long id);

    @Query(value = """
            SELECT
                a.id as id,
                a.site_code as siteCode,
                a.line_name as lineName,
                b.active as active,
                b.group_code as groupCode,
                b.group_name as groupName,
                a.description as description,
                a.error_stage as errorStage,
                a.team as team,
                a.flag as flag,
                a.status as status,
                a.processing_at as processingAt,
                a.completed_at as completedAt,
                a.created_at as createdAt,
                a.updated_at as updatedAt
            FROM andondata a
            LEFT JOIN andon_group b\s
                ON a.team = b.group_code
            WHERE a.status != 'OK'
              AND a.site_code =:siteCode
""", nativeQuery = true)
    List<Map<String, Object>> findDataPendingByLine(@Param("siteCode") String siteCode);


    @Query(value = """
                    select\s
            ROW_NUMBER() OVER (
                PARTITION BY A.request_id\s
                ORDER BY A.created_at
            ) AS RN,
            A.request_id as id,
            A.description,
            A.action_type,
            A.from_user,
            A.user_name,
            A.from_team,
            A.to_team,
            A.from_team_name,
            A.to_team_name,
            A.status,
            A.start_time,
            A.end_time,
            A.created_at,
            A.updated_at,
            A.is_active
        from (
            select\s
                A.*,
                B.name as user_name,
                C.group_name as from_team_name,
                D.group_name as to_team_name
            from andon_process_log A
            left join users B on A.from_user = B.username
            left join andon_group C on A.from_team = C.group_code
            left join andon_group D on A.to_team = D.group_code
            where A.request_id in (
                select a.id\s
                from andondata a\s
                where a.status != 'OK'\s
                  and a.site_code = :siteCode
            )
        ) A
""", nativeQuery = true)
    List<Map<String, Object>> getDataDataPendingLog(@Param("siteCode") String siteCode);



    @Query(value = " select A.*, B.method, B.old_device , B.new_device , B.old_status , B.replace_reason  from andondata A left join andon_handling_detail B  on \n" +
            "A.id = B.request_id where A.site_code LIKE '%' || :siteCode || '%'   and A.created_at between :fromDate  and :toDate " , nativeQuery = true)
    List<Map<String, Object>> andonGetData(String siteCode, LocalDateTime fromDate, LocalDateTime toDate);


    @Query(value = " select a.* from andondata a " , nativeQuery = true)
    List<Map<String, Object>> andonDashboardData();



}
