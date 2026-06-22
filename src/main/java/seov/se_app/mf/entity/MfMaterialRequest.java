package seov.se_app.mf.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.time.LocalDate;

@Entity
@Table(name = "mf_material_request")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class MfMaterialRequest extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_no", nullable = false, unique = true, length = 50)
    private String requestNo;

    @Column(name = "department", length = 50)
    private String department;

    @Column(name = "production_number", length = 100)
    private String productionNumber;

    @Column(name = "request_date")
    private LocalDate requestDate;

    @Column(name = "status", length = 30)
    private String status; // DRAFT, SUBMITTED, APPROVED, DONE, CANCELLED

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "zCode", length = 200)
    private String zCode;

    @Column(name = "created_by", length = 50)
    private String createdBy;
}