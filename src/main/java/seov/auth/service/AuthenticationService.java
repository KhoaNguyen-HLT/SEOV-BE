package seov.auth.service;

import org.springframework.web.bind.annotation.RequestBody;
import seov.auth.dto.request.AuthenticationRequest;
import seov.auth.dto.request.PermissionRequest;
import seov.auth.dto.request.RoleUpdateRequest;
import seov.auth.dto.respone.RoleResponse;
import seov.auth.repository.roleRepository;
import seov.user.entity.User;
import seov.user.repository.UserRepository;
import seov.auth.dto.respone.AuthenticationResponse;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import seov.auth.entity.Role;
import seov.auth.entity.Permissions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    roleRepository roleRepository;
    @Autowired
    PermissionRepository permissionRepository;
    @NonFinal
    static final String SIGNER_KEY  = "63af975db954bc2318eff8b6bc65aa33f7cb6315ea02c6f5924d38bd0a17f0f8fd79ecfafbec11b9279b8e0f737ebc4d623a34489612afbb357f90592f73a959";

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        var user = userRepository.findByUsername(request.getUsername());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        return passwordEncoder.matches(request.getPassword(), user.get().getPassword());
        boolean authenticated = passwordEncoder.matches(
                request.getPassword(),
                user.get().getPassword()
        );

        if(authenticated) {
            User user1 = userRepository.findFullUserByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // roles
            List<String> roles = user1.getRoles()
                    .stream()
                    .map(Role::getName)
                    .toList();

            // permissions
            Set<String> permissions = user1.getRoles()
                    .stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(Permissions::getName)
                    .collect(Collectors.toSet());
            var token = generateToken(request.getUsername(),roles, permissions );

            return AuthenticationResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .roles(roles)
                    .permissions(permissions)
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .token("null")
                    .authenticated(false)
                    .build();
        }

    }

    public Map<String, Object> getData(String request){
        Map<String, Object> result = new HashMap<>();

        result.put("role", "admin");
        result.put("permission", "delelte");
        return result;
    }

    private String generateToken(String username, List<String> roles,Set<String> permissions ) {
        JWSHeader header= new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("khoa.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("role", roles)
                .claim("permissions", permissions)
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header,payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isTokenValid(String token) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);

            // verify chữ ký
            JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

            boolean isVerified = jwsObject.verify(verifier);
            if (!isVerified) {
                return false;
            }

            // lấy payload
            JWTClaimsSet claims = JWTClaimsSet.parse(jwsObject.getPayload().toJSONObject());

            // check expiration
            Date expiration = claims.getExpirationTime();

            return expiration.after(new Date());

        } catch (Exception e) {
            return false;
        }
    }


    public List<Object> getRole() {
        List<Object> listRole = Collections.singletonList(roleRepository.findAll());
        return listRole;
    }


    public RoleResponse updateRole(Long id, RoleUpdateRequest request){
        Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        roleRepository.save(role);

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .build();
    }


    public RoleResponse CreateRole(RoleUpdateRequest request){
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        // 1. Save
        Role savedRole = roleRepository.save(role);

        // 2. Convert sang response
        return RoleResponse.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .description(savedRole.getDescription())
                .build();

    }

    public List<Object> getPermission() {
        List<Object> listRole = Collections.singletonList(roleRepository.findAll());
        return listRole;
    }


    public RoleResponse updatePermission(Long id, PermissionRequest request){
        Permissions permission = .findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        permission.setName(request.getName());
        permission.setCode(request.getCode());

        roleRepository.save(permission);

        return RoleResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getCode())
                .build();
    }


    public RoleResponse createPermission(RoleUpdateRequest request){
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        // 1. Save
        Role savedRole = roleRepository.save(role);

        // 2. Convert sang response
        return RoleResponse.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .description(savedRole.getDescription())
                .build();

    }




}
