package seov.se_app.pu.cfr.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.pu.cfr.entity.CfrTransInventory;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface CfrTransInventoryRepository extends JpaRepository<CfrTransInventory, Long> {
    Optional<CfrTransInventory> findByItemCode(String itemCode);
//    @Query(value = """
//    select DISTINCT (A.lot_no) from iqc_rlm_data A where A.type = 'IQC 24MT/24TMT Master'
//""", nativeQuery = true)
//    List<Map<String, Object>> getLotData();
@Modifying
@Transactional
@Query("""
        delete from  CfrTransInventory a
        where a.month = :month and a.documentType = :documentType and a.reportType = :reportName
    """)
void deleteTransInventoryData( String month, String documentType, String reportName);


    @Query(value = """
    select A.*, (A.tondau_5 + A.qty_nhap_6 + A.qty_nhap_7 -A.qty_xuat_8 -A.qty_xuat_9 -A.qty_xuat_10 -A.qty_xuat_11 ) as toncuoi from (select A.item_code,
           B.item_namev,
           B.cfr_unit,
           A.quantity as tondau_5,
           coalesce(C.qty_nhap, 0) as qty_nhap_6,
           0 as qty_nhap_7,
           coalesce(D.qty_xuat_8, 0) as qty_xuat_8,
           0 as qty_xuat_9,
           coalesce(E.qty_xuat_10 , 0) as qty_xuat_10,
           coalesce(F.qty_xuat_11, 0) as qty_xuat_11
    from (select * from cfr_inventory where report_type = '15' and  report_month <= :month )  A
    left join material_data B 
        on A.item_code = B.item_code
    left join (
        select item_code, sum(quantity) as qty_nhap
        from cfr_inventory_transaction
        where month <= :month
          and customs_type_code in ('E11', 'E15')
          and document_type = 'DATA_PU' and report_type = '15'
        group by item_code
    ) C on A.item_code = C.item_code
    left join (
        select item_code, sum(quantity) as qty_xuat_8
        from cfr_inventory_transaction
        where month <= :month
          and customs_type_code = 'B13'
          and document_type = 'DATA_PU' and report_type = '15'
        group by item_code
    ) D on A.item_code = D.item_code
    left join (
        select item_code, sum(quantity) as qty_xuat_10
        from cfr_inventory_transaction
        where month <= :month
          and document_type = 'IVT_MF' and report_type = '15'
        group by item_code
    ) E on A.item_code = E.item_code
    left join (
        select item_code, sum(quantity) as qty_xuat_11
        from cfr_inventory_transaction
        where month <= :month
          and customs_type_code = 'H21'
          and document_type = 'DATA_PU' and report_type = '15'
        group by item_code
    ) F on A.item_code = F.item_code) A
    """, nativeQuery = true)
    List<Map<String, Object>> getData(String month);



    @Query(value = """

    select A.*, (A.tondau_5 + A.qty_nhap_6 +A.qty_nhap_7 - A.qty_xuat_8 - A.qty_xuat_9 - A.qty_xuat_10 ) as toncuoi\s
    from (select A.item_code , B.item_namev , B.cfr_unit , A.quantity as tondau_5, coalesce(C.qty_nhap_6, 0) as qty_nhap_6  , coalesce(D.qty_nhap_7, 0) as qty_nhap_7,
    0 as qty_xuat_8, coalesce(E.qty_xuat_9, 0) as qty_xuat_9, coalesce(F.qty_xuat_10, 0) as qty_xuat_10
    from (select * from cfr_inventory where report_type = '15a' and report_month <= :month ) A\s
    left join\s
    material_data  B on A.item_code = B.item_code\s
    left join
    (select A.item_code,sum(A.quantity)  as qty_nhap_6  from cfr_inventory_transaction A where month <= :month and  A.document_type = 'IVT_MF' and A.report_type = '15a'  group by A.item_code ) C
    on A.item_code = C.item_code
    left join
    (select A.item_code,sum(A.quantity)  as qty_nhap_7  from cfr_inventory_transaction A where month <= :month and A.customs_type_code = 'G13' and A.document_type = 'DATA_PU' and A.report_type = '15a'  group by A.item_code ) D
    on A.item_code = D.item_code
    left join
    (select A.item_code,sum(A.quantity)  as qty_xuat_9  from cfr_inventory_transaction A where month <= :month  and A.customs_type_code in('E42', 'G23')  and A.document_type = 'DATA_PU' and A.report_type = '15a'  group by A.item_code ) E
    on A.item_code = E.item_code
    left join
    (select A.item_code,sum(A.quantity)  as qty_xuat_10  from cfr_inventory_transaction A where month <= :month and A.document_type in ('OTHER_MF', 'DATA_PU') and  customs_type_code in ('OTHER','H21')  and A.report_type = '15a' group by A.item_code ) F
    on A.item_code = F.item_code) A
    """, nativeQuery = true)
    List<Map<String, Object>> getData15a(@Param("month") String month);


    @Query(value = """

            with tb1 as (
            select A.item_code, SUM(A.quantity) as pr_qty from cfr_inventory_transaction a where report_type = '15a' and document_type = 'DATA_PU' and a."month" <= :month and a.month >= '2026-04' group by item_code order by a.item_code
            ), tb2 as (select item_code, sum(quantity) as tong_nvl_xuat_trong_ky
            from cfr_inventory_transaction a
            where a."month" <= :month and a.month >= '2026-04'
              and document_type = 'IVT_MF' and report_type = '15'
            group by item_code),
            tb3 as (select  b.material_code , sum(b.norm_seov) as sum_nvl_dm from tb1 left join bom_data b  on tb1.item_code = b.product_code group by material_code order by b.material_code )
            --        select * from tb3
           select A.*, (A.tong_nvl_xuat_trong_ky * A.ty_le_nvl_bom) as nvl_thucte_sd,  (A.tong_nvl_xuat_trong_ky * A.ty_le_nvl_bom)/A.tp_nhap_trong_ky as fn from\s
            (select tb1.item_code, B.item_namee, B.cfr_unit,C.material_code ,D.item_namee as material_name  , D.cfr_unit as m_unit, C.prd_code , C.norm_seov, tb1.pr_qty as tp_nhap_trong_ky,
            tb2.tong_nvl_xuat_trong_ky , (C.norm_seov * tb1.pr_qty) as nvl_sudung_dm,
            case
            when tb3.sum_nvl_dm > 0 then (C.norm_seov * tb1.pr_qty)/tb3.sum_nvl_dm\s
    	else null
    end
            as ty_le_nvl_bom
            from tb1
            left join material_data B on tb1.item_code = b.item_code
            left join bom_data C on tb1.item_code = c.product_code
            left join material_data D on C.material_code = D.item_code
            left join tb2 on C.material_code  = tb2.item_code
            left join tb3 on c.material_code = tb3.material_code  ) A
    """, nativeQuery = true)
    List<Map<String, Object>> getData16(@Param("month") String month);



    @Query(value = """
    select item_code from cfr_inventory_transaction where month = :month and report_type = :reportName limit 1
    """, nativeQuery = true)
    List<Map<String, Object>> checkExistedData(@Param("month") String month, @Param("reportName") String reportName);


}
