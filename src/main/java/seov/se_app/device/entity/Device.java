package seov.se_app.device.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "devices")
@AllArgsConstructor
@Builder
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private LocalDateTime dateTimeUsed;
    private LocalDate dateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
