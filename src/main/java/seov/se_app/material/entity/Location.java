package seov.se_app.material.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;


@Entity
@Data
@NoArgsConstructor
@Table(name = "locations")
@AllArgsConstructor
@Builder

public class Location extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Mã vị trí
     * VD:
     * WH-A01
     * CAB-02
     * LINE-03
     */
    @Column(name = "location_code",
            nullable = false,
            unique = true,
            length = 50)
    private String locationCode;

    /**
     * Tên hiển thị
     */
    @Column(name = "location_name",
            nullable = false,
            length = 100)
    private String locationName;

    /**
     * WH / CAB / LINE / NG / STORE
     */
    @Column(name = "location_type",
            nullable = false,
            length = 30)
    private String locationType;

    /**
     * Cấu trúc cha con nếu có
     * WH-A
     *   └─ WH-A01
     */
    @Column(name = "parent_location_id")
    private Long parentLocationId;

    /**
     * Ghi chú
     */
    @Column(name = "remark",
            length = 500)
    private String remark;

}
