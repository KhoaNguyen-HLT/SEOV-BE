package seov.se_app.andon.dto.respon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonDataRespone {
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
