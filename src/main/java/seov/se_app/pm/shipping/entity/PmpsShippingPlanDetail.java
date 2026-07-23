package seov.se_app.pm.shipping.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pmsp_shipping_plan_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmpsShippingPlanDetail extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String spNo;
    @Column(name = "material_code")
    private String materialCode;
    private String supplierCode;
    /*** ngày NVL xuống MIN */
    private LocalDate needDate;
    /*** ngày NCC giao được */
    private LocalDate deliveryDate;
    /*** Số lươợng được giao */
    @Column(name = "delivery_qty", precision = 18, scale = 3)
    private BigDecimal deliveryQty;
    @Column(name = "moq_qty", precision = 18, scale = 3)
    private BigDecimal MoqQty;
    private String po;
    private String status;

}