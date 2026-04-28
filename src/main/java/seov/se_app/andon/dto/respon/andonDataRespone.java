package seov.se_app.andon.dto.respon;

import jakarta.persistence.*;
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
public class andonDataRespone {
    private Long id;
    private String siteCode;
    private String lineName;
    private String description;
    private String errorStage;
    private Integer team;
    private String groupName;
    private String status;
    private String flag;
    private LocalDateTime processingAt;
    private LocalDateTime completedAt;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
