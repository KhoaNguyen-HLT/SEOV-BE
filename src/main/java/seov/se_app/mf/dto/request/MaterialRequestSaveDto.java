package seov.se_app.mf.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequestSaveDto {
    private String department;
    private String productionNumber;
    private String createdBy;
    private LocalDate requestDate;
    private String remark;
    @JsonProperty("zCodes")
    private List<String> zCode;
    private List<MaterialRequestDetailDto> details;
}