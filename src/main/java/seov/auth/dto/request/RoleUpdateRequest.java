package seov.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleUpdateRequest {
    private String name;
    private String description;
}