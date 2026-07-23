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
@Table(name = "pmsp_inventory_snapshot")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmpsIvtSnapshot extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*** Ngày snapshot tồn kho */
    @Column(name = "snapshot_date")
    private LocalDate snapshotDate;

    /*** Mã nguyên vật liệu */
    @Column(name = "material_code", length = 50)
    private String materialCode;

    /*** Số lượng tồn*/
    @Column(name = "quantity", precision = 18, scale = 3)
    private BigDecimal quantity;

}