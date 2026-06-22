package seov.se_app.qa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "iqc_rlm_data_his")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IqcRlmDataHis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestNo;
    private String poNo;
    private String mainCableSn;
    private String subCableSn;
    private Integer subCableNo;
    private String lotNo;
    private Integer bs;
    private String resultNo;
    private String type;
    private String measureType;
    private String programName;
    private String programType;
    private String userCode;
    private String status;
    private String flag;
    private LocalDateTime operationTime;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;


    public IqcRlmDataHis(IqcRlmData data) {
        this.requestNo = data.getRequestNo();
        this.poNo = data.getPoNo();
        this.mainCableSn = data.getMainCableSn();
        this.subCableSn = data.getSubCableSn();
        this.subCableNo = data.getSubCableNo();
        this.lotNo = data.getLotNo();
        this.bs = data.getBs();
        this.resultNo = data.getResultNo();
        this.type = data.getType();
        this.measureType = data.getMeasureType();
        this.programType = data.getProgramType();
        this.userCode = data.getUserCode();
        this.status = data.getStatus();
        this.flag = data.getFlag();
        this.operationTime = data.getOperationTime();
        this.created_at = data.getCreated_at();
        this.updated_at = data.getUpdated_at();
    }


}
