package seov.se_app.mf.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "mf_material_an_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MaterialAnRequest extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_no", nullable = false, length = 50)
    private String requestNo;

    @Column(name = "item_code", length = 50)
    private String itemCode;

    @Column(name = "unit", length = 30)
    private String unit;

    @Column(name = "qty", precision = 18, scale = 6)
    private BigDecimal qty;

    @Column(name = "process", length = 100)
    private String process;

    @Column(name = "remark", length = 500)
    private String remark;
}