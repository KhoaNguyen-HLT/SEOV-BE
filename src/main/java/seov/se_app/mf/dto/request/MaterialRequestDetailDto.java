package seov.se_app.mf.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("id")
    private Long id;

    @JsonProperty("material_code")
    private String materialCode;

    @JsonProperty("eng_unit")
    private String unit;

    @JsonProperty("qtyOrder")
    private BigDecimal qtyOrder;

    @JsonProperty("issuedQty")
    private BigDecimal issuedQty;

    @JsonProperty("process")
    private String process;

    @JsonProperty("custom_mode")
    private String materialType;

    @JsonProperty("remark")
    private String remark;
}