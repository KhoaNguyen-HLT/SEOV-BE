package seov.se_app.mf.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "mf_material_request_detail")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MaterialRequestDetail extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_no", nullable = false, length = 50)
    private String requestNo;

    @Column(name = "item_code", nullable = false, length = 100)
    private String itemCode;

    @Column(name = "unit", length = 30)
    private String unit;

    @Column(name = "request_qty", precision = 18, scale = 6)
    private BigDecimal requestQty;

    @Column(name = "issued_qty", precision = 18, scale = 6)
    private BigDecimal issuedQty;

    @Column(name = "process", length = 100)
    private String process;

    @Column(name = "material_type", length = 50)
    private String materialType;

    @Column(name = "remark", length = 500)
    private String remark;
}