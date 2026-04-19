package seov.auth.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class AuthResponse {
    private int username;
    private List<String> roles;
    private Set<String> permissions;
}