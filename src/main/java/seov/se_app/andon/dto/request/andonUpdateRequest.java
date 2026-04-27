package seov.se_app.andon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonUpdateRequest {
    private String status;
    private LocalDateTime processingAt;
    private LocalDateTime completedAt;
    private LocalDateTime updated_at;


}
