package seov.se_app.device.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceCreateRequest {
    private String location;
    private String gscmName;
    private String code;
    private String serialNumber;
    private String supplier;
    private String fa;
    private String faCode;
    private String poNumber;
    private String kianNo;
    private String status;
    private LocalDate dateTime;
}
