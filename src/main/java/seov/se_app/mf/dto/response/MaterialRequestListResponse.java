package seov.se_app.mf.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MaterialRequestListResponse<T> {
    private int code;
    private String message;
    private String text;
    private List<Map<String, Object>> data;
    private List<Map<String, Object>> hdData;
    private List<Map<String, Object>> prData;
}