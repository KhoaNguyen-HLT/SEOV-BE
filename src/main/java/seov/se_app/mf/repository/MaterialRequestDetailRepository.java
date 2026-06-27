package seov.se_app.mf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.mf.dto.request.MaterialRequestReportProjection;
import seov.se_app.mf.entity.MaterialRequestDetail;

import java.util.List;
import java.util.Map;

public interface MaterialRequestDetailRepository extends JpaRepository<MaterialRequestDetail, Long> {
    List<MaterialRequestDetail> findByRequestNo(String requestNo);

    @Query(value = """
        select A.id ,A.request_no , A.unit , A.material_code as "materialCode" , B.item_namee as "itemName" , A.material_type as "materialType" , A.qty_order as "qtyOrder" ,\s
        case\s
        	when C.status != 'COMPLETED' then A.qty_order
        	else A.issued_qty
        end as "issuedQty",\s
        A.remark , C.status \s
        from mf_material_request_detail A
        left join material_data B on A.material_code = B.item_code\s
        left join mf_material_request C on A.request_no = C.request_no\s
    where A.request_no = :requestNo
""", nativeQuery = true)
    List<Map<String, Object>> getDetailMaterialRequest(String requestNo);


    @Query(value = """
    SELECT
            A.request_no AS requestNo,
            A.production_number AS productionNumber,
            A.department AS department,
            A.status AS status,
            CONCAT(A.approved_by, '-', B.name) AS approvedBy,
            CONCAT(A.issued_by, '-', C.name) AS issuedBy,
            CONCAT(A.created_by, '-', D.name) AS createdBy,
            A.updated_at AS updatedAt,
            E.material_type AS materialType,
            E.unit AS unit,
            E.material_code AS materialCode,
            E.qty_order AS qtyOrder,
            E.issued_qty AS issuedQty,
            F.layout,
            G.item_namee as materialName \s
        FROM mf_material_request A
        LEFT JOIN users B ON A.approved_by = B.username
        LEFT JOIN users C ON A.issued_by = C.username
        LEFT JOIN users D ON A.created_by = D.username
        LEFT JOIN mf_material_request_detail E ON A.request_no = E.request_no
        LEFT JOIN pm_layout F ON E.material_code = F.material_code
        LEFT JOIN material_data  G ON E.material_code = G.item_code
    WHERE A.request_no = :requestNo order by E.material_code
""", nativeQuery = true)
    List<MaterialRequestReportProjection> getReportMaterialRequest(
            @Param("requestNo") String requestNo
    );


}
