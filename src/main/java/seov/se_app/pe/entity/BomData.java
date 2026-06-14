package seov.se_app.pe.entity;

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
        name = "bom_data",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_bom_product_item",
                        columnNames = {"item_product", "item_code"}
                )
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_product", nullable = false, length = 100)
    private String itemProduct;
    @Column(name = "item_code", nullable = false, length = 100)
    private String itemCode;
    @Column(name = "quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;
}
