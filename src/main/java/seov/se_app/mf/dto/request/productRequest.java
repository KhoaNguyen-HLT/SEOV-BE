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
public class productRequest {

    @JsonProperty("item_code")
    private String itemCode;
    private String unit;
    private String remark;
    private BigDecimal qty;
}