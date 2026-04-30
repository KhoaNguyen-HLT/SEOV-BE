package seov.se_app.andon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "andon_process_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonProcessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 🔗 Liên kết ticket (andondata)
    @Column(name = "request_id")
    private Long request_id;
    @Column(name = "action_type")
    private String actionType;
    @Column(name = "status")
    private String status;
    @Column(name = "from_team")
    private Integer fromTeam;
    @Column(name = "to_team")
    private Integer toTeam;
    @Column(name = "from_user")
    private String fromUser;
    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column(name = "duration")
    private Long duration;
    private Boolean isActive;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "created_by")
    private String createdBy;


}
