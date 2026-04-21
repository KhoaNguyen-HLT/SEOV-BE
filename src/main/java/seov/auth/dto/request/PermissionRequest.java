package seov.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PermissionRequest {
    private String name;
    private String code;
}