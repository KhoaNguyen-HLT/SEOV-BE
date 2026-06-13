package seov.se_app.pu.entity;

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
        name = "cfr_inventory_transaction"
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CfrTransInventory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Kỳ báo cáo, ví dụ 2026-04
    private String month;
    // Mã hàng
    @Column(nullable = false, length = 100)
    private String itemCode;
    // Loại giao dịch: IN / OUT / TRANSFER / ADJUST
    private String transactionType;
    // Số lượng giao dịch
    @Column(precision = 18, scale = 3)
    private BigDecimal quantity;
    private String documentType;
    private String documentNo;
    private String reportType;
    private String customsTypeCode;
    private String remark;
}
