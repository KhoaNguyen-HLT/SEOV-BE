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
@Table(name = "pmsp_material_demand")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmspMaterialDemand extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** * Ngày cần sử dụng NVL cho sản xuất */
    @Column(name = "demand_date")
    private LocalDate demandDate;

    /** * Mã nguyên vật liệu */
    @Column(name = "material_code", length = 50)
    private String materialCode;

    /** * Số lượng NVL cần dùng */
    @Column(name = "demand_qty", precision = 18, scale = 3)
    private BigDecimal demandQty;

    /** * PIC */
    @Column(name = "pic", length = 50)
    private String pic;

}