package seov.se_app.material.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequest {
    private String location;
    private String gscmName;
    private String code;
    private String serialNumber;
}
