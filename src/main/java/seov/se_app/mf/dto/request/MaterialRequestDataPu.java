package seov.se_app.mf.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequestDataPu {
    private String department;
    private LocalDate date;
    private String remark;
    @JsonProperty("zCode")
    private List<String> zCodes;
}