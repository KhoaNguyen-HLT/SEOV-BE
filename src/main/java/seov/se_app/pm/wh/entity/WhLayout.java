package seov.se_app.pm.wh.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

@Entity
@Table(name = "pm_layout")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class WhLayout extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "material_code", length = 50)
    private String materialCode;
    @Column(name = "unit", length = 50)
    private String unit;
    @Column(name = "layout", length = 50)
    private String layout;
    @Column(name = "person", length = 50)
    private String person;

}