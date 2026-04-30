package seov.se_app.andon.repository;

import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.andon.entity.andonHandlingDetail;

public interface andonHandlingDetailRepository extends JpaRepository<andonHandlingDetail, Long> {
    @Query(value = "SELECT EXISTS (SELECT 1 FROM andon_handling_detail WHERE request_id = :id)", nativeQuery = true)
    Integer existsByRequestId(@Param("id") Long id);

}
