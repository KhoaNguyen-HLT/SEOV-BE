package seov.se_app.andon.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonDataRequest {
    private String siteCode;
    private String LineName;
    private String Description;
    private String ErrorStage;
    private String Team;
    private String userCode;
    private LocalDate datetime;
    private LocalDate created_at;
    private LocalDate updated_at;


}
