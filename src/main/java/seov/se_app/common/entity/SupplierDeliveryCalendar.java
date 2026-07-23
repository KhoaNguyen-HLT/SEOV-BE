package seov.se_app.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "supplier_delivery_calendar")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class SupplierDeliveryCalendar extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Nhà cung cấp
     */
    @Column(name = "supplier_code", nullable = false)
    private String supplierCode;

    /**
     * Thứ trong tuần
     * 1 = Monday
     * 2 = Tuesday
     * ...
     * 7 = Sunday
     */
    @Column(name = "week_day", nullable = false)
    private Integer weekDay;

    /**
     * Số lượng tối đa có thể giao trong một lần
     */
    @Column(name = "max_delivery_qty", precision = 18, scale = 2)
    private BigDecimal maxDeliveryQty;

    /**
     * Hiệu lực từ ngày
     */
    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    /**
     * Hiệu lực đến ngày
     */
    @Column(name = "effective_to")
    private LocalDate effectiveTo;

}