package seov.se_app.mf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.mf.dto.request.MaterialProductDetail;
import seov.se_app.mf.entity.MfMaterialRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialProductRequestResponse<T> {
    private int code;
    private String message;
    private String text;
    private List<MaterialProductDetail> data;
}