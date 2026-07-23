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
@Table(name = "pmsp_po_location")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmpsPoLocation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String spNo;

    private String po;
    /**
     * Số lượng lấy từ PO này
     */
    @Column(precision = 18,scale = 2)
    private BigDecimal allocatedQty;


}