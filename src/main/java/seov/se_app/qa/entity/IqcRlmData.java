package seov.se_app.qa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.se_app.andon.entity.andonGroup;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "iqc_rlm_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IqcRlmData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestNo;
    private String poNo;
    private String mainCableSn;
    private String subCableSn;
    private Integer subCableNo;
    private String lotNo;
    private int bs;
    private String resultNo;
    private String type;
    private String measureType;
    private String programType;
    private String userCode;
    private String status;
    private String flag;
    private LocalDateTime operationTime;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


}
