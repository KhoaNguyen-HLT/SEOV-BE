package seov.se_app.material.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "devices")
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Mã NVL nội bộ
     */
    @Column(name = "material_code", nullable = false, unique = true, length = 50)
    private String materialCode;
    /**
     * Tên NVL
     */
    @Column(name = "material_name", nullable = false, length = 255)
    private String materialName;

    /**
     * Quy cách
     */
    @Column(name = "specification", length = 255)
    private String specification;

    /**
     * Loại NVL:
     * CABLE / TERMINAL / TAPE / LABEL...
     */
    @Column(name = "material_type", length = 50)
    private String materialType;

    /**
     * pcs / kg / m
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * Nhà sản xuất
     */
    @Column(name = "maker", length = 100)
    private String maker;

    /**
     * Nhà cung cấp
     */
    @Column(name = "vendor", length = 100)
    private String vendor;

    /**
     * Mã khách hàng
     */
    @Column(name = "customer_part_no", length = 100)
    private String customerPartNo;

    /**
     * ACTIVE / INACTIVE
     */
    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = "ACTIVE";
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
