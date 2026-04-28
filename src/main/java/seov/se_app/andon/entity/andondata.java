package seov.se_app.andon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.auth.entity.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Table(name = "andondata")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andondata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String siteCode;
    private String lineName;
    private String description;
    private String errorStage;
    @Column(name = "team")
    private Integer team;
    private String userCode;
    private String status;
    private String flag;
    private LocalDateTime created_at;
    private LocalDateTime processingAt;
    private LocalDateTime completedAt;
    private LocalDateTime updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "team",
            referencedColumnName = "group_code",
            insertable = false,
            updatable = false
    )
    private andonGroup group;


}
