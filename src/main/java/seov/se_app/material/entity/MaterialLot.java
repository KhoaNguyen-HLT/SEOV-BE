package seov.se_app.material.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "material_lots")
@AllArgsConstructor
@Builder

public class MaterialLot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Lot nội bộ trong nhà máy
     */
    @Column(name = "lot_no", nullable = false, unique = true, length = 100)
    private String lotNo;

    /**
     * FK -> materials.id
     */
    @Column(name = "material_id", nullable = false)
    private Long materialId;

    /**
     * Lot từ supplier
     */
    @Column(name = "vendor_lot_no", length = 100)
    private String vendorLotNo;

    /**
     * Số lượng nhập ban đầu
     */
    @Column(name = "quantity_original", nullable = false)
    private Integer quantityOriginal;

    /**
     * Số lượng còn lại
     */
    @Column(name = "quantity_remaining", nullable = false)
    private Integer quantityRemaining;

    /**
     * Ngày nhập kho
     */
    @Column(name = "received_date")
    private LocalDateTime receivedDate;

    /**
     * Hạn dùng nếu có
     */
    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    /**
     * Barcode / QR lot
     */
    @Column(name = "barcode", length = 255)
    private String barcode;

    /**
     * Ghi chú
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
