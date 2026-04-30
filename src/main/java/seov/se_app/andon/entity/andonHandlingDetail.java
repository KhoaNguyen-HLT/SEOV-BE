package seov.se_app.andon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "andon_handling_detail")
public class andonHandlingDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "request_id")
    private Long requestId;
    private String method;
    private String repairNotes;
    private String oldDevice;
    private String newDevice;
    private String oldStatus;
    private String replaceReason;
    private LocalDateTime createdAt;
}