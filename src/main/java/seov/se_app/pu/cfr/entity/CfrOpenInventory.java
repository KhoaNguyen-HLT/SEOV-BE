package seov.se_app.pu.cfr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Data
@Table(
        name = "cfr_inventory"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CfrOpenInventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String period;
    @Column(nullable = false, length = 100)
    private String itemCode;
    @Column(precision = 18, scale = 3)
    private BigDecimal quantity;
    private String reportType;
    private String reportMonth;
    private String remark;

}
