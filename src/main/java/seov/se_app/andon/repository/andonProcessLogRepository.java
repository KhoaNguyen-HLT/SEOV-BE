package seov.se_app.andon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.andon.entity.andonProcessLog;
import seov.user.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface andonProcessLogRepository extends JpaRepository<andonProcessLog, Long> {
    @Query(value = """
    SELECT 
        ROW_NUMBER() OVER (
            PARTITION BY A.request_id
            ORDER BY A.created_at
        ) AS rn,
        A.id,
        A.request_id,
        A.created_at,
        B.name AS user_name,
        C.group_name AS from_team_name,
        D.group_name AS to_team_name
    FROM andon_process_log A
    LEFT JOIN user B 
        ON A.from_user = B.username
    LEFT JOIN andon_group C 
        ON A.from_team = C.id
    LEFT JOIN andon_group D 
        ON A.to_team = D.id
    WHERE A.request_id = :id
""", nativeQuery = true)
    List<Map<String, Object>> getChangeGroupData(@Param("id") Long id);
}
