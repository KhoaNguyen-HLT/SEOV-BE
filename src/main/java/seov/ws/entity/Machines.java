package seov.ws.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Machines {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String machineName;
    private String machineCode;
    private String model;
    private String status;
    private String remark;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
