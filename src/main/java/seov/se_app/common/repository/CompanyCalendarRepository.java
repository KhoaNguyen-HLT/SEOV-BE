package seov.se_app.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.common.entity.CompanyCalendar;
import seov.se_app.pm.shipping.entity.PmpsIvtSnapshot;

import java.math.BigDecimal;
import java.time.LocalDate;


public interface CompanyCalendarRepository extends JpaRepository<CompanyCalendar, Long> {

    @Query(value = """
        SELECT B.work_date
        FROM supplier_delivery_calendar A
        JOIN (
            SELECT c.work_date,
                   EXTRACT(ISODOW FROM c.work_date) AS week_day
            FROM company_calendar c
            WHERE c.working_day = true
        ) B
        ON A.week_day = B.week_day
        WHERE A.supplier_code = :supplierCode
          AND B.work_date >= :demandDate
        ORDER BY B.work_date ASC
        LIMIT 1
        """,
            nativeQuery = true)
    LocalDate getDeliveryDate(
            @Param("demandDate") LocalDate demandDate,
            @Param("supplierCode") String supplierCode
    );
}
