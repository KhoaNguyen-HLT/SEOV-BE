package seov.se_app.andon.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonDashboardDataRespone<T> {
    private int code;
    private String message;
    private List<Map<String, Object>> data;


}

