package seov.se_app.material.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

@Entity
@Data
@NoArgsConstructor
@Table(name = "inventory_transactions")
@AllArgsConstructor
@Builder

public class InventoryTransaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mã giao dịch
     * VD: TXN202605240001
     */
    @Column(name = "transaction_no", nullable = false, unique = true, length = 50)
    private String transactionNo;

    /**
     * RECEIVE / MOVE / ISSUE_TO_LINE / RETURN_TO_WH / CONSUME / ADJUST / STOCKTAKE / SCRAP
     */
    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

// thông tin loại giao dịch được chuyển từ đâu đến đâu theo tên cửa sổ.
    @Column(name = "transaction_code", length = 50)
    private String transactionCode;

    /**
     * FK -> material_lots.id
     */
    @Column(name = "material_lot_id", nullable = false)
    private Long materialLotId;

    /**
     * Vị trí nguồn
     * Có thể null khi RECEIVE
     */
    @Column(name = "from_location_id")
    private Long fromLocationId;

    /**
     * Vị trí đích
     * Có thể null khi CONSUME/SCRAP
     */
    @Column(name = "to_location_id")
    private Long toLocationId;

    /**
     * Số lượng giao dịch
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;


    /**
     * Người thao tác / người scan
     */
    @Column(name = "created_by", length = 100)
    private String createdBy;

    /**
     * Ghi chú
     */
    @Column(name = "remark", length = 500)
    private String remark;
}
