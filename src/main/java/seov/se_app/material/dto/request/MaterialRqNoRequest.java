package seov.se_app.material.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRqNoRequest {
    private String requestNo;
    private String flowCode;
    private String flowName;
    private String transactionType;
}
