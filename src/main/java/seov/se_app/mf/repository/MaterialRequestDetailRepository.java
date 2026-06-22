package seov.se_app.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.mf.entity.MaterialRequestDetail;

import java.util.List;

public interface MaterialRequestDetailRepository extends JpaRepository<MaterialRequestDetail, Long> {
    List<MaterialRequestDetail> findByRequestNo(String requestNo);
}
