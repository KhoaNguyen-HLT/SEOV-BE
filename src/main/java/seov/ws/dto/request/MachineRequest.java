package seov.ws.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MachineRequest {
     String machineName;
     String machineCode;
     String model;
     String status;
}
