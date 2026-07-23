package seov.se_app.pm.shipping.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pmsp_material_requirement")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmspMaterialRequirement extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Ngày chạy tính toán
     */
    @Column(name = "plan_date")
    private LocalDate planDate;

    /**
     * Ngày phát sinh nhu cầu
     */
    @Column(name = "demand_date")
    private LocalDate demandDate;

    @Column(name = "material_code")
    private String materialCode;

    /**
     * Tồn đầu ngày
     */
    @Column(name = "opening_qty", precision = 18, scale = 3)
    private BigDecimal openingQty;

    /**
     * Nhu cầu sử dụng trong ngày
     */
    @Column(name = "demand_qty", precision = 18, scale = 3)
    private BigDecimal demandQty;

    /**
     * Tồn cuối ngày dự kiến
     */
    @Column(name = "closing_qty", precision = 18, scale = 3)
    private BigDecimal closingQty;

    /**
     * Số lượng thiếu
     */
    @Column(name = "shipping_qty", precision = 18, scale = 3)
    private BigDecimal shippingQty;


}