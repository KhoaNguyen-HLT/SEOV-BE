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
@Table(name = "pmsp_material_standard")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmspMaterialStandard extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Mã nguyên vật liệu
     */
    @Column(name = "material_code", nullable = false, unique = true, length = 50)
    private String materialCode;

    /**
     * Tồn kho tối thiểu
     */
    @Column(name = "min_qty", nullable = false, precision = 18, scale = 3)
    private BigDecimal minQty;

    /**
     * Tồn kho tối đa
     */
    @Column(name = "max_qty", nullable = false, precision = 18, scale = 3)
    private BigDecimal maxQty;

    /**
     * số ngày giao hàng
     */
    @Column(name = "lead_time", nullable = false)
    private Integer leadTime = 0;

    @Column(name = "created_by", length = 50)
    private String createdBy;

}