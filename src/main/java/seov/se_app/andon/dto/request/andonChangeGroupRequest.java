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
public class andonChangeGroupRequest {
    private Long id;
    private Integer from_team;
    private Integer to_team;
    private String reason;
    private String from_user;
    private LocalDateTime start_time;
    private LocalDateTime updated_at;

}
