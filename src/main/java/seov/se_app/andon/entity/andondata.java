package seov.se_app.andon.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import seov.auth.entity.Role;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andondata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
