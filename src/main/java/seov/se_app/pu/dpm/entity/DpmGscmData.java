package seov.se_app.pu.dpm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "dpm_gscm_data"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DpmGscmData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String po;
    @Column(length = 10)
    private String lineNo;
    private String requestNo;
    private LocalDateTime approvalDate;
    private String vendorC;
    private String paymentTerms;
    @Column(length = 10)
    private String tradeTerms;
    private String itemC;
    @Column(length = 200)
    private String itemName;
    private String orderQuantity;
    private String uom;
    private String unitPrice;
    private String expenseName;
    private LocalDate deliverySd;
}
