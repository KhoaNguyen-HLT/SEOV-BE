package seov.se_app.mf.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequestDetailDto {
    private String itemCode;
    private String unit;
    private BigDecimal requestQty;
    private String process;
    private String materialType;
    private String remark;
}