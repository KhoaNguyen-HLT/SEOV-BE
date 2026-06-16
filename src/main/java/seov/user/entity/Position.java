package seov.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import seov.se_app.common.entity.BaseEntity;

@Entity
@Table(name = "position")
@Getter
@Setter
public class Position extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "position_code", nullable = false, unique = true, length = 20)
    private String positionCode;

    @Column(name = "position_name", nullable = false, length = 100)
    private String positionName;

    @Column(name = "description")
    private String description;
}
