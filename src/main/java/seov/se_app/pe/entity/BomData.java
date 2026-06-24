package seov.se_app.pe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.common.entity.BaseEntity;

import java.math.BigDecimal;

@Entity
@Data
@Table(
        name = "bom_data"
//        uniqueConstraints = {
//                @UniqueConstraint(
//                        name = "uk_bom_prd_material",
//                        columnNames = {"product_code", "material_code"}
//                )
//        }
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BomData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stt;

    @Column(name = "bom_link", length = 200)
    private String bomLink;

    private String type;

    @Column(name = "prd_code", length = 50)
    private String prdCode;

    @Column(name = "product_code", length = 50)
    private String productCode;

    @Column(name = "product_name", length = 200)
    private String productName;

    @Column(name = "material_code", length = 50)
    private String materialCode;

    @Column(name = "custom_mode", length = 50)
    private String customMode;

    @Column(name = "material_name", length = 200)
    private String materialName;

    @Column(name = "vietnamese_name", length = 500)
    private String vietnameseName;

    @Column(name = "norm_sei", precision = 18, scale = 5)
    private BigDecimal normSei;

    @Column(name = "norm_seov", precision = 18, scale = 5)
    private BigDecimal normSeov;

    @Column(name = "gscm_eng", length = 50)
    private String gscmEng;

    @Column(name = "gscm_vnese", length = 50)
    private String gscmvnese;

    @Column(name = "eng_unit", length = 50)
    private String engUnit;

    @Column(name = "vnese_unit", length = 50)
    private String vneseUnit;

    private String note;

    @Column(name = "for_pm", precision = 18, scale = 5)
    private BigDecimal forPm;

    @Column(name = "gscm_type")
    private String gscmType;
}
