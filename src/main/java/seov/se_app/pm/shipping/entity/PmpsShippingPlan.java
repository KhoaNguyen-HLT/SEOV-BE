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
@Table(name = "pmsp_shipping_plan")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmpsShippingPlan extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // SP202607180001
    @Column(unique = true, nullable = false)
    private String spNo;

    private LocalDate planDate;




}