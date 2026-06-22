package seov.se_app.mf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.material.entity.MaterialRequest;
import seov.se_app.mf.entity.MfMaterialRequest;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequestResponse<T> {
    private int code;
    private String message;
    private String text;
    private MfMaterialRequest data;
}