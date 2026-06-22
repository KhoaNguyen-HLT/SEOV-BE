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
    from (select * from cfr_inventory where report_type = '15')  A
    left join material_data B 
        on A.item_code = B.item_code
    left join (
        select item_code, sum(quantity) as qty_nhap
        from cfr_inventory_transaction
        where month <= :month
          and customs_type_code in ('E11', 'E15')
          and document_type = 'DATA_PU'
        group by item_code
    ) C on A.item_code = C.item_code
    left join (
        select item_code, sum(quantity) as qty_xuat_8
        from cfr_inventory_transaction
        where month <= :month
          and customs_type_code = 'B13'
          and document_type = 'DATA_PU'
        group by item_code
    ) D on A.item_code = D.item_code
    left join (
        select item_code, sum(quantity) as qty_xuat_10
        from cfr_inventory_transaction
        where month <= :month
          and document_type = 'IVT_MF'
        group by item_code
    ) E on A.item_code = E.item_code
    left join (
        select item_code, sum(quantity) as qty_xuat_11
        from cfr_inventory_transaction
        where month <= :month
          and customs_type_code = 'H21'
          and document_type = 'DATA_PU'
        group by item_code
    ) F on A.item_code = F.item_code) A
    """, nativeQuery = true)
    List<Map<String, Object>> getData(String month);



    @Query(value = """
    SELECT 
        x.item_code,
        x.item_namev,
        x.cfr_unit,
        x.tondau_5,
        x.qty_nhap_6,
        x.qty_nhap_7,
        x.qty_xuat_8,
        x.qty_xuat_9,
        x.qty_xuat_10,
        (
            x.tondau_5 
            + x.qty_nhap_6 
            + x.qty_nhap_7 
            - x.qty_xuat_8 
            - x.qty_xuat_9 
            - x.qty_xuat_10
        ) AS toncuoi
    FROM (
        SELECT 
            i.item_code,
            m.item_namev,
            m.cfr_unit,
            COALESCE(i.quantity, 0) AS tondau_5,

            COALESCE(SUM(
                CASE 
                    WHEN t.month <= :month
                     AND t.document_type = 'IVT_MF'
                    THEN t.quantity 
                    ELSE 0 
                END
            ), 0) AS qty_nhap_6,

            COALESCE(SUM(
                CASE 
                    WHEN t.month <= :month
                     AND t.document_type = 'DATA_PU'
                     AND t.customs_type_code = 'G13'
                    THEN t.quantity 
                    ELSE 0 
                END
            ), 0) AS qty_nhap_7,

            0 AS qty_xuat_8,

            COALESCE(SUM(
                CASE 
                    WHEN t.month <= :month
                     AND t.document_type = 'DATA_PU'
                     AND t.customs_type_code IN ('E42', 'G23')
                    THEN t.quantity 
                    ELSE 0 
                END
            ), 0) AS qty_xuat_9,

            COALESCE(SUM(
                CASE 
                    WHEN t.month <= :month
                     AND t.document_type IN ('OTHER_MF', 'DATA_PU')
                     AND t.customs_type_code IN ('OTHER', 'H21')
                    THEN t.quantity 
                    ELSE 0 
                END
            ), 0) AS qty_xuat_10

        FROM cfr_inventory i
        LEFT JOIN material_data m 
            ON i.item_code = m.item_code
        LEFT JOIN cfr_inventory_transaction t 
            ON i.item_code = t.item_code
           AND t.report_type = '15a'

        WHERE i.report_type = '15a'

        GROUP BY 
            i.item_code,
            m.item_namev,
            m.cfr_unit,
            i.quantity
    ) x
    ORDER BY x.item_code
    """, nativeQuery = true)
    List<Map<String, Object>> getData15a(@Param("month") String month);

}
