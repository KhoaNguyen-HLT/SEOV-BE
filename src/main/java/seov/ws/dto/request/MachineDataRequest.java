package seov.ws.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MachineDataRequest {
     String machineCode;
     String machineName;
     int qty;
     String shift;
}
