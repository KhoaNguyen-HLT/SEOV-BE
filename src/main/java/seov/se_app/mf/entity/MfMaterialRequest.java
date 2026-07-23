package seov.se_app.mf.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;
import seov.se_app.mf.enums.MaterialRequestStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    @Column(name = "request_need_date")
    private LocalDateTime requestNeedDate;

    @Column(name = "required_time")
    private LocalTime requiredTime;

    @Column(name = "qty_request", precision = 18, scale = 2)
    private BigDecimal qtyRequest;

    /**
     * SUBMITTED
     * APPROVED
     * REJECTED
     * COMPLETED
     * CANCELLED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private MaterialRequestStatus status;

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "z_code", length = 500)
    private String zCode;

    // Người tạo
    @Column(name = "created_by", length = 20)
    private String createdBy;

    // Người cập nhật cuối
    @Column(name = "updated_by", length = 20)
    private String updatedBy;

    // Người duyệt
    @Column(name = "approved_by", length = 20)
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    // Người xuất kho
    @Column(name = "issued_by", length = 20)
    private String issuedBy;

    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    // Người từ chối
    @Column(name = "rejected_by", length = 20)
    private String rejectedBy;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    @Column(name = "reject_reason", length = 500)
    private String rejectReason;
}