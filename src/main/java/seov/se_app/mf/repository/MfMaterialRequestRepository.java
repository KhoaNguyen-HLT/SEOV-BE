package seov.se_app.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seov.se_app.mf.entity.MfMaterialRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MfMaterialRequestRepository extends JpaRepository<MfMaterialRequest, Long> {
    boolean existsByRequestNo(String requestNo);


    @Query(value = """
       select A.* from mf_material_request A where A.department =:department
         AND A.request_date >= :fromDate AND A.request_date <= :toDate
    """, nativeQuery = true)
    List<Map<String, Object>> getMaterialRequestData(String department, LocalDate fromDate, LocalDate toDate);
}
