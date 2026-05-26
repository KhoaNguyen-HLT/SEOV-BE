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
@AllArgsConstructor
@Builder
@Table(name = "inventory_transaction_flows")
public class InventoryTransactionFlow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    mã đầu phieu request
    @Column(name = "mr_code", nullable = false, unique = true, length = 50)
    private String mrCode;

    @Column(name = "flow_code", nullable = false, unique = true, length = 50)
    private String flowCode;

    @Column(name = "flow_name", nullable = false, length = 100)
    private String flowName;

    /**
     * ID vị trí nguồn
     */
    @Column(name = "from_location_id")
    private Long fromLocationId;

    /**
     * ID vị trí đích
     */
    @Column(name = "to_location_id")
    private Long toLocationId;

    /**
     * RECEIVE / ISSUE / TRANSFER / RETURN / PRODUCE / SCRAP / ADJUST
     */
    @Column(name = "transaction_type", nullable = false, length = 30)
    private String transactionType;

    @Column(name = "require_lot")
    private Boolean requireLot = true;

    @Column(name = "require_quantity")
    private Boolean requireQuantity = true;

    @Column(name = "require_work_order")
    private Boolean requireWorkOrder = false;

    @Column(name = "require_reference")
    private Boolean requireReference = false;

    @Column(name = "require_approve")
    private Boolean requireApprove = false;

    @Column(name = "allow_negative_stock")
    private Boolean allowNegativeStock = false;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "note", length = 255)
    private String note;
}