package seov.se_app.qa.dto.response;

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
public class qaResponse<T> {
    private int code;
    private String message;
    private List<Map<String, Object>> data;
}
