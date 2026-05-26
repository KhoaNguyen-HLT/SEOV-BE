package seov.se_app.material.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "material_requests")
@AllArgsConstructor
@Builder
public class MaterialRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "request_no", nullable = false, unique = true, length = 50)
    private String requestNo;

    @Column(name = "flow_code", length = 50)
    private String flowCode;

    @Column(name = "flow_name", length = 100)
    private String flowName;

    @Column(name = "transaction_type", length = 30)
    private String transactionType;

    @Column(name = "note", length = 255)
    private String note;

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
