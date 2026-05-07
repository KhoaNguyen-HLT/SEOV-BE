package seov.se_app.andon.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.K;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class andonChangeGroupResponse<T> {
    private int code;
    private String message;
    private T data;
    private List<Map<String, Object>> changeGroupData;
}
