package seov.se_app.andon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonGetDataRequest {
    String fromDate;
    String toDate;
    String line;
    String status;

}
