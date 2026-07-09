package seov.se_app.mf.dto.request;


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
    String getRequestNeedDate();
    String getRequiredTime();
    String getProductName();
    String getRemark();

    String getCreateAt();
    LocalDateTime getUpdatedAt();

    String getMaterialType();

    String getMaterialCode();

    String getMaterialName();

    BigDecimal getQtyOrder();
    String getQtyRequest();

    BigDecimal getIssuedQty();
}
