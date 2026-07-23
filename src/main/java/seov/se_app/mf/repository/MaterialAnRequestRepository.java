package seov.se_app.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.mf.dto.request.MaterialRequestReportProjection;
import seov.se_app.mf.entity.MaterialAnRequest;
import seov.se_app.mf.entity.MaterialRequestDetail;

import java.util.List;
import java.util.Map;

public interface MaterialAnRequestRepository extends JpaRepository<MaterialAnRequest, Long> {
    List<MaterialAnRequest> findByRequestNo(String requestNo);
    @Query(value = """
        select A.id, A.request_no, A.item_code, B.item_namee, A.unit, A.qty, A.created_at from mf_material_an_request A
        left join material_data B
        on A.item_code = B.item_code where A.request_no = :requestNo order by id
""", nativeQuery = true)
    List<Map<String, Object>> getMaterialAnRequest(String requestNo);

}
