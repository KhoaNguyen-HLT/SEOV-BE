package seov.se_app.andon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "andon_group")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "group_code",unique = true)
    private Integer  groupCode;
    @Column(name = "group_name")
    private String groupName;
    private String active;
    private String description;
}
