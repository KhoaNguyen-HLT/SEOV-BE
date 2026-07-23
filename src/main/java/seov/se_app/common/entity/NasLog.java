package seov.se_app.common.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nas_log")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class NasLog extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Log */
    @Column(name = "log")
    private String  log;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "user_name")
    private String  userName;

    @Column(name = "ip")
    private String  ip;

    @Column(name = "event")
    private String  event;

    @Column(name = "file")
    private String  file;

    @Column(name = "file_size")
    private String  fileSize;

    @Column(name = "file_name")
    private String  fileName;

    @Column(name = "type")
    private String  type;

}