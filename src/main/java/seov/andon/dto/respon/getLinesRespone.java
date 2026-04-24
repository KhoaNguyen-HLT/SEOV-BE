package seov.andon.dto.respon;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class getLinesRespone {
     String siteCode;
     String lineName;
}
