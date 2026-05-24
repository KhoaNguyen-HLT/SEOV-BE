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
@Table(
        name = "inventories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_inventory_lot_location",
                        columnNames = {"material_lot_id", "location_id"}
                )
        }
)
@AllArgsConstructor
@Builder

public class Inventory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * FK -> material_lots.id
     */
    @Column(name = "material_lot_id", nullable = false)
    private Long materialLotId;

    /**
     * FK -> locations.id
     */
    @Column(name = "location_id", nullable = false)
    private Long locationId;

    /**
     * Số lượng hiện tại của lot tại location này
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * AVAILABLE / HOLD / ALLOCATED / NG
     */
    @Column(name = "inventory_status", length = 30)
    private String inventoryStatus;

    /**
     * Ghi chú
     */
    @Column(name = "remark", length = 500)
    private String remark;

}
