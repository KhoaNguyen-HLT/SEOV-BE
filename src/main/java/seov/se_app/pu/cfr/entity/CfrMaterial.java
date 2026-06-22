package seov.se_app.pu.cfr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "material_data",
        indexes = {
                @Index(name = "idx_material_code", columnList = "itemCode")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CfrMaterial extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String itemCode;

    @Column(length = 500)
    private String itemNameE;

    @Column(length = 500)
    private String itemNameV;

    private String hqUnit;
    private String cfrUnit;
    private String gscmUnit;

    private String materialType;
    private String gscmType;
    private String type;
    private String customMode;
    private String hsCode;
    private String supplier;
}
