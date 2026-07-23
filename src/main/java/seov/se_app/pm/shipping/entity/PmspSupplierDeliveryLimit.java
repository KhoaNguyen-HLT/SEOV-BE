package seov.se_app.pm.shipping.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Table(name = "pmsp_supplier_delivery_limit")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PmspSupplierDeliveryLimit extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Mã NVL
    @Column(name = "material_code")
    private String materialCode;

    // NCC
    @Column(name = "supplier_code")
    private String supplierCode;

    // Số lượng tối đa NCC giao trong 1 lần
    @Column(name = "max_delivery_qty")
    private BigDecimal maxDeliveryQty;

    // Đơn vị
    @Column(name = "unit")
    private String unit;

    // trạng thái
    @Column(name = "active")
    private Boolean active;

}