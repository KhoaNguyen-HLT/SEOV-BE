package seov.se_app.mf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialProductDetail {
    private String itemCode;
    private String itemName;
    private String customMode;
    private String unit;
    private BigDecimal qty;
}