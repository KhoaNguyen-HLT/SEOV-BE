package seov.se_app.andon.dto.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class andonSenRequestRespone {
    private int code;
    private String message;
    private Object data;
}
