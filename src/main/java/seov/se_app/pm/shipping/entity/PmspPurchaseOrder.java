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
@Table(name = "pmsp_purchase_order")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmspPurchaseOrder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Số PO
    @Column(nullable = false, unique = true)
    private String poNo;

    // Nhà cung cấp
    private String supplierCode;

    // Mã NVL
    @Column(name = "material_code")
    private String materialCode;

    // Số lượng đặt
    @Column(precision = 18, scale = 2)
    private BigDecimal orderQty;

    // Đã phân bổ vào Shipping Plan
    @Column(precision = 18, scale = 2)
    private BigDecimal allocatedQty = BigDecimal.ZERO;

    // Còn lại chưa phân bổ
    @Column(precision = 18, scale = 2)
    private BigDecimal remainQty;

    // Ngày tạo PO
    private LocalDate poDate;

}