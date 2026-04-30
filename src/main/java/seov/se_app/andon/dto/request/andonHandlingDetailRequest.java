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
public class andonHandlingDetailRequest {
    private String method;
    private String repairNotes;
    private String oldDevice;
    private String newDevice;
    private String oldStatus;
    private String replaceReason;
    private LocalDateTime created_at;

}
