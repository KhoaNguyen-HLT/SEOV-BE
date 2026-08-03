package seov.se_app.pm.shipping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pm.shipping.entity.PmpsShippingPlan;

import java.util.List;
import java.util.Map;


public interface PmspShippingPlanRepository extends JpaRepository<PmpsShippingPlan, Long> {

    @Query(value = """
    		select A.*,
            case
            	when B.dsi = 0 then null
            	else round(A.delivery_qty/B.dsi, 3)
            end as qty_dsi,
            C.quantity tondau,
            B.min_qty ,
            case
                when B.dsi = 0 then null
                else round(C.quantity /B.dsi, 3) 
            end as songay 
            from pmsp_shipping_plan_detail A
            left join pmsp_material_standard B
            on A.material_code = B.material_code
            left join pmsp_inventory_snapshot C
            on A.material_code = C.material_code
            where A.created_at >= CAST(:date AS timestamp)
            AND A.created_at <= CAST(CONCAT(:date, ' 23:59:59') AS timestamp)    
    """, nativeQuery = true)
    List<Map<String, Object>> getShippingPlanData(@Param("date") String date);
}
