package seov.se_app.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.mf.entity.MfMaterialRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MfMaterialRequestRepository extends JpaRepository<MfMaterialRequest, Long> {
    boolean existsByRequestNo(String requestNo);
    Optional<MfMaterialRequest> findByRequestNo(String requestNo);

    @Query(value = """
       select A.* from mf_material_request A where A.department =:department AND status like Concat('%',:status,'%')
         AND A.created_at >= :fromDate AND A.created_at <= :toDate
    """, nativeQuery = true)
    List<Map<String, Object>> getMaterialRequestData(String department, LocalDateTime fromDate, LocalDateTime toDate, String status);


    @Query(value = """
    select A.product_code , A.product_name , A.material_code , A.material_name, A.custom_mode , A.norm_sei , A.norm_seov , A.eng_unit  
    from bom_data a where a.product_code = :design_number order by a.custom_mode
""", nativeQuery = true)
    List<Map<String, Object>> prepareMaterialRequestData(String design_number);

    @Query(value = """
          select A.item_code as material_code, A.item_namev as vietnamese_name, A.material_type as custom_mode, A.gscm_unit as gscm_eng  from material_data A where A."type" = 'NVL'
          and A.material_type not in ('MAIN','Main material','Packing material', 'Packing materials', 'Sub material' );
""", nativeQuery = true)
    List<Map<String, Object>> getConsumptionData();


    @Query(value = """
          select A.id,A.item_code, A.item_namee, A.gscm_unit  from material_data A where type = 'TP';
""", nativeQuery = true)
    List<Map<String, Object>> getProductData();




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


    @Query("""
    SELECT COUNT(r)
    FROM MfMaterialRequest r
    WHERE r.createdAt >= :start
      AND r.createdAt < :end
""")
    Long countRequestInDay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
