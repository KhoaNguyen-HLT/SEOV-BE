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
            A.remark,
            CONCAT(A.approved_by, '-', B.name) AS approvedBy,
            CONCAT(A.issued_by, '-', C.name) AS issuedBy,
            CONCAT(A.created_by, '-', D.name) AS createdBy,
            A.created_at as createAt,
            A.updated_at AS updatedAt,
            A.request_need_date AS requestNeedDate,
            A.required_time as requiredTime,
            A.qty_request as qtyRequest,
            E.material_type AS materialType,
            E.unit AS unit,
            E.material_code AS materialCode,
            E.qty_order AS qtyOrder,
            E.issued_qty AS issuedQty,
            F.layout,
            G.vietnamese_name as materialName ,
            G.product_name as productName
        FROM mf_material_request A                     \s
        LEFT join users B  ON A.approved_by = B.username                     \s
        LEFT join users C  ON A.issued_by = C.username                     \s
        LEFT join users D  ON A.created_by = D.username                     \s
        LEFT join mf_material_request_detail E  ON A.request_no = E.request_no                     \s
        LEFT join pm_layout F ON E.material_code = F.material_code\s
        LEFT join bom_data G on A.production_number = G.product_code and E.material_code = G.material_code\s
        where A.request_no =  :requestNo       \s
        order by E.material_code ;
""", nativeQuery = true)
    List<MaterialRequestReportProjection> getReportMaterialRequest(
            @Param("requestNo") String requestNo
    );


}
