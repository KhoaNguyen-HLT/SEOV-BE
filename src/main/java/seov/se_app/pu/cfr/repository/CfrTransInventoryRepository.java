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
@Modifying
@Transactional
@Query("""
        delete from  CfrTransInventory a
        where a.month = :month and a.documentType = :documentType and a.reportType = :reportName
    """)
void deleteTransInventoryData( String month, String documentType, String reportName);


    @Query(value = """
    select A.*, (A.tondau_5 + A.qty_nhap_6 + A.qty_nhap_7 -A.qty_xuat_8 -A.qty_xuat_9 -A.qty_xuat_10 -A.qty_xuat_11 ) as toncuoi,
        coalesce(K.scrossqty_nhap, 0) as scrossqty_nhap, coalesce(K.cross_nhap_gscm, 0) as cross_nhap_gscm, coalesce(H.cross_toncuoi, 0) as cross_toncuoi 
        from 
    (select A.item_code,
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
    left join 
        (select A.*, B.sum as cross_nhap_gscm
        from (select A.item_code , sum(A.quantity) as scrossqty_nhap  from cfr_cross_inout A where
        customs_type_code in ('E11', 'E15') and A.report_type = '15' and A.transaction_type = 'IN' and  A."period" = '2026-04' and A."month" <= :month
        group by A.item_code) A
        left join
        (select A.item_code , sum(A.quantity)  from cfr_cross_inout A where
        customs_type_code = 'GSCM' and A.report_type = '15' and A.transaction_type  = 'IN' and A."period" = '2026-04' and A."month" <= :month group by A.item_code) B
        on A.item_code = B.item_code) K
        on A.item_code = K.item_code
            left join
                (select
                    A.item_code , sum(A.quantity) as cross_toncuoi
                from
                    cfr_cross_inventory A
                where
                    A.document_type in ('ENDING_PM', 'IVT_MF')
                    and A.report_type ='15'
                    and A."period" = '2026-04'
                    and A.report_month = :month
                group by
                    item_code) H    
                    on A.item_code = H.item_code
    """, nativeQuery = true)
    List<Map<String, Object>> getData(String month);



    @Query(value = """

    select A.*, (A.tondau_5 + A.qty_nhap_6 +A.qty_nhap_7 - A.qty_xuat_8 - A.qty_xuat_9 - A.qty_xuat_10 ) as toncuoi,
    coalesce(K.crossqty_xuat, 0) as crossqty_xuat, coalesce(K.crossqty_xuat_fgpm, 0) as crossqty_xuat_fgpm , coalesce(K.cross_toncuoi, 0) as cross_toncuoi
    from (select A.item_code , B.item_namev , B.cfr_unit , A.quantity as tondau_5, coalesce(C.qty_nhap_6, 0) as qty_nhap_6  , coalesce(D.qty_nhap_7, 0) as qty_nhap_7,
    0 as qty_xuat_8, coalesce(E.qty_xuat_9, 0) as qty_xuat_9, coalesce(F.qty_xuat_10, 0) as qty_xuat_10
    from (select * from cfr_inventory where report_type = '15a' and report_month <= :month ) A\s
    left join\s
    material_data  B on A.item_code = B.item_code\s
    left join
    (select A.item_code,sum(A.quantity)  as qty_nhap_6  from cfr_inventory_transaction A where month <= :month and  A.document_type = 'FG_MF' and A.report_type = '15a'  group by A.item_code ) C
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
    left join
        (
        select C.item_code,
        case
            when A.crossqty_xuat  is null then 0
            else A.crossqty_xuat
        end as crossqty_xuat,
        case
            when B.crossqty_xuat_fgpm is null then 0
            else B.crossqty_xuat_fgpm
        end as crossqty_xuat_fgpm,
        C.sum  cross_toncuoi from 
        (select A.item_code , sum(A.quantity)  from cfr_cross_inventory A where
        A.document_type in ('FG_PM', 'FG_MF') and A.report_type ='15a' and A."period" = '2026-04' and A.report_month = '2026-06' group by item_code) C
        left join
        (select A.item_code , sum(A.quantity) as crossqty_xuat  from cfr_cross_inout A where
        customs_type_code = 'E42' and A.report_type = '15a' and A.transaction_type = 'OUT' and  A."period" = '2026-04' and A."month" <= '2026-06'
        group by A.item_code) A on C.item_code = A.item_code 
        left join
        (select A.item_code , sum(A.quantity) as crossqty_xuat_fgpm  from cfr_cross_inout A where
        customs_type_code = 'FG_PM' and A.report_type = '15a' and A.transaction_type  = 'OUT' and A."period" = '2026-04' and A."month" <= '2026-06' group by A.item_code) B
        on C.item_code = B.item_code) K
        on A.item_code = K.item_code
    """, nativeQuery = true)
    List<Map<String, Object>> getData15a(@Param("month") String month);


    @Query(value = """

           WITH tb1 AS (
               SELECT
                   a.item_code,
                   SUM(a.quantity) AS pr_qty
               FROM cfr_inventory_transaction a
               WHERE a.report_type = '15a'
                 AND a.document_type = 'IVT_MF'
                 AND a."month" >= '2026-04'
                 AND a."month" <= :month
                 and a.quantity != 0
               GROUP BY a.item_code
           ),
           tb2 AS (
               SELECT
                   a.item_code,
                   SUM(a.quantity) AS tong_nvl_xuat_trong_ky
               FROM cfr_inventory_transaction a
               WHERE a.report_type = '15'
                 AND a.document_type = 'IVT_MF'
                 AND a."month" >= '2026-04'
                 AND a."month" <= :month
               GROUP BY a.item_code
           ),
           tb3 AS (select material_code  , sum(nvl_sudung_dm) as tong_nvl_sd_dm from
                (SELECT
                       tb1.item_code,
                       c.material_code,
                       c.prd_code,
                       (c.norm_seov * tb1.pr_qty) AS nvl_sudung_dm
                   FROM tb1
                   LEFT JOIN bom_data c
                       ON tb1.item_code = c.product_code) where nvl_sudung_dm != 0 group by material_code\s
           )
           SELECT
               A.*,
               (A.tong_nvl_xuat_trong_ky * A.ty_le_nvl_bom) AS nvl_thucte_sd,
               (A.tong_nvl_xuat_trong_ky * A.ty_le_nvl_bom) / A.tp_nhap_trong_ky AS fn
           FROM (
               SELECT
                   tb1.item_code,
                   b.item_namee,
                   b.cfr_unit,
                   c.material_code,
                   d.item_namee AS material_name,
                   d.cfr_unit AS m_unit,
                   concat(c.product_code,c.material_code) as prd_code,
                   c.norm_seov,
                   tb1.pr_qty AS tp_nhap_trong_ky,
                   tb2.tong_nvl_xuat_trong_ky,
                   (c.norm_seov * tb1.pr_qty) AS nvl_sudung_dm,
                   tb3.tong_nvl_sd_dm,
                   (c.norm_seov * tb1.pr_qty) / tb3.tong_nvl_sd_dm AS ty_le_nvl_bom
               FROM tb1
               LEFT JOIN material_data b
                   ON tb1.item_code = b.item_code
               LEFT JOIN (select * from bom_data where custom_mode != 'E13') c
                   ON tb1.item_code = c.product_code
               LEFT JOIN material_data d
                   ON c.material_code = d.item_code
               LEFT JOIN tb2
                   ON c.material_code = tb2.item_code
               LEFT JOIN tb3
                   ON c.material_code = tb3.material_code
           ) A
    """, nativeQuery = true)
    List<Map<String, Object>> getData16(@Param("month") String month);



    @Query(value = """
    select item_code from cfr_inventory_transaction where month = :month and report_type = :reportName limit 1
    """, nativeQuery = true)
    List<Map<String, Object>> checkExistedData(@Param("month") String month, @Param("reportName") String reportName);

    @Query(value = """
     select A.* from (select A.document_type, A."month" , A.report_type , A.created_by, B.name, to_char(A.created_at , 'DD/MM/YYYY HH24:MI') as datetime  from cfr_inventory_transaction A 
    left join users B on A.created_by = B.username
    ) A where A.report_type = :reportName group by A.document_type, A."month" , A.report_type , A.created_by, A.datetime, A.name
    order by A.report_type,A.document_type, A."month"
    """, nativeQuery = true)
    List<Map<String, Object>> getHisData(@Param("reportName") String reportName);


}
