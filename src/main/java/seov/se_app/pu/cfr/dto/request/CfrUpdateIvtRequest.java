package seov.se_app.pu.cfr.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CfrUpdateIvtRequest {
    private String reportName;
    private String month;
}
