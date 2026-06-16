package seov.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import seov.se_app.common.entity.BaseEntity;

@Entity
@Table(name = "department")
@Getter
@Setter
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_code", nullable = false, unique = true, length = 20)
    private String departmentCode;

    @Column(name = "department_name", nullable = false, length = 100)
    private String departmentName;

    @Column(name = "description")
    private String description;
}
