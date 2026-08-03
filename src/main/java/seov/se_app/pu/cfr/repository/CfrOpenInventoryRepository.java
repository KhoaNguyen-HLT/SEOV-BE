package seov.se_app.pu.cfr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seov.se_app.pu.cfr.entity.CfrOpenInventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface CfrOpenInventoryRepository extends JpaRepository<CfrOpenInventory, Long> {
    Optional<CfrOpenInventory> findByItemCode(String itemCode);
    @Query(value = """
    select A.*, B.item_code as item_code_B  from (select item_code,"month", document_type, cit.created_at,report_type, row_number() over(PARTITION by cit.item_code order by created_at  ) as RN
    from cfr_inventory_transaction cit 
    where report_type = '15' 
    --and status = 'ACTIVE' 
    and customs_type_code in ('E11', 'E15') ) A\s
    left join cfr_inventory B on A.item_code = B.item_code and A.report_type = B.report_type where B.item_code is null and rn = 1 and A.item_code != 'End';
""", nativeQuery = true)
    List<Map<String, Object>> updateOpenInventory15();


    @Query(value = """
    select A.*, B.item_code as item_code_B  from (select item_code,"month", document_type, a.created_at,report_type, row_number() over(PARTITION by a.item_code order by created_at  ) as RN
         from cfr_inventory_transaction a where report_type = '15a' 
        and (document_type in ('IVT_MF','FG_MF') or a.customs_type_code = 'G13') and a.quantity != 0) A
        left join (select * from  cfr_inventory where report_type = '15a') B on A.item_code = B.item_code where B.item_code is null and rn = 1 and A.item_code != 'End'
""", nativeQuery = true)
    List<Map<String, Object>> updateOpenInventory15a();





}
