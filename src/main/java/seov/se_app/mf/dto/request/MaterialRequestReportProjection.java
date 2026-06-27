package seov.se_app.mf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
public interface MaterialRequestReportProjection {
    String getRequestNo();

    String getProductionNumber();

    String getDepartment();

    String getStatus();

    String getApprovedBy();

    String getIssuedBy();

    String getCreatedBy();

    String getUnit();

    String getLayout();

    LocalDateTime getUpdatedAt();

    String getMaterialType();

    String getMaterialCode();

    String getMaterialName();

    BigDecimal getQtyOrder();

    BigDecimal getIssuedQty();
}
