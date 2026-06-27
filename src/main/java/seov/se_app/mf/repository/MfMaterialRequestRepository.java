package seov.se_app.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seov.se_app.mf.entity.MfMaterialRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MfMaterialRequestRepository extends JpaRepository<MfMaterialRequest, Long> {
    boolean existsByRequestNo(String requestNo);
    Optional<MfMaterialRequest> findByRequestNo(String requestNo);

    @Query(value = """
       select A.* from mf_material_request A where A.department =:department
         AND A.request_date >= :fromDate AND A.request_date <= :toDate
    """, nativeQuery = true)
    List<Map<String, Object>> getMaterialRequestData(String department, LocalDate fromDate, LocalDate toDate);


    @Query(value = """
    select A.product_code , A.product_name , A.material_code , A.material_name, A.custom_mode , A.norm_sei , A.norm_seov , A.eng_unit  
    from bom_data a where a.product_code = :design_number order by a.custom_mode
""", nativeQuery = true)
    List<Map<String, Object>> prepareMaterialRequestData(String design_number);


    @Query(value = """
    select A.request_no,A.production_number,A.department,A.status,\s
        Concat(A.approved_by,'-',B.name) as approved_by,
         Concat(A.issued_by,'-',C.name) as issued_by,
         Concat(A.rejected_by,'-',D.name) as rejected_by
        ,A.reject_reason, A.updated_at \s
        from mf_material_request A
        left join users B on A.approved_by = B.username
        left join users C on A.issued_by = C.username
        left join users D on A.rejected_by = D.username
        where A.request_no = :requestNo
""", nativeQuery = true)
    List<Map<String, Object>> getHeaderMaterialRequest(String requestNo);
}
