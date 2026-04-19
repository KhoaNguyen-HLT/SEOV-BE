package seov.ws.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "machines_data")
@Data

public class MachinesData {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String machineCode;
    private String machineName;
    private int qty;
    private String shift;
    private LocalDateTime created_at;
    private LocalDateTime  updated_at;

}
