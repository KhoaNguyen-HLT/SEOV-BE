package seov.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
     String username;
     String password;
     String name;
     String position;
     String section;
     String email;
     @JsonFormat(pattern = "yyyy-MM-dd")
     private LocalDate joinDate;


}
