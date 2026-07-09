package seov.se_app.pu.dpm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.hpsf.Decimal;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "dpm_shipping_plan_data"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DpmShippingPlanData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 20)
    private String po;
    @Column(length = 10)
    private String asNo;
    private LocalDate shipmentDate;
    private LocalDate eta;
    private BigDecimal shippingQty;
}
