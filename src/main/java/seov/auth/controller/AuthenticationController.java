package seov.auth.controller;

import org.springframework.web.bind.annotation.*;
import seov.auth.dto.request.*;
import seov.auth.dto.respone.ApiResponse;
import seov.auth.dto.respone.AuthenticationResponse;
import seov.auth.dto.respone.RoleResponse;
import seov.auth.entity.Role;
import seov.auth.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import seov.user.entity.Position;
import seov.user.entity.User;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestBody Map<String, Object> request) {
        try {
            String token = (String) request.get("token");
            boolean valid = authenticationService.isTokenValid(token);
            return ResponseEntity.ok(valid);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @GetMapping("/roles")
    public List<Object> getRole() {
        List<Object> result = authenticationService.getRole();
        return result;
    }

    @GetMapping("/getAllRole")
    public ResponseEntity<ApiResponse<List<Role>>>  getAllRole() {
        List<Role> result = authenticationService.getAllRole();
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<Role>>builder()
                            .code(200)
                            .message("success")
                            .data(result)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.<List<Role>>builder()
                            .code(200)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }


    @PutMapping("/roles/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(@PathVariable Long id, @RequestBody RoleUpdateRequest request) {
        RoleResponse roleResponse =  authenticationService.updateRole(id, request);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("success")
                .data(roleResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/roles")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole( @RequestBody RoleUpdateRequest request) {
        RoleResponse roleResponse =  authenticationService.CreateRole(request);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("success")
                .data(roleResponse)
                .build();
        return ResponseEntity.ok(response);
    }



    @GetMapping("/permissions")
    public List<Object> getPermission() {
        List<Object> result = authenticationService.getPermission();
        return result;

    }

    @PutMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<RoleResponse>> updatePermission(@PathVariable Long id, @RequestBody PermissionRequest request) {
        RoleResponse roleResponse =  authenticationService.updatePermission(id, request);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("success")
                .data(roleResponse)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<RoleResponse>> createPermission(@RequestBody PermissionRequest request) {
        RoleResponse roleResponse =  authenticationService.createPermission(request);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .code(200)
                .message("success")
                .data(roleResponse)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/UpdateRolePermission")
    public ResponseEntity<?> updateRolePermission(
            @RequestBody UpdateRolePermissionRequest request) {

        authenticationService.updateRolePermission(request);

        return ResponseEntity.ok(Map.of("message", "success"));
    }


    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse<User>> resetPassword(
            @RequestBody ResetPasswordRequest request) {
        User user = authenticationService.resetPassword(request);
        ApiResponse<User> response = ApiResponse.<User>builder()
                .code(200)
                .message("success")
                .data(user)
                .build();
        return ResponseEntity.ok(response);
    }


    @PostMapping("/changePassword")
    public ResponseEntity<ApiResponse<User>> changePassword(
            @RequestBody ChangePasswordRequest request) {

        try {
            User user = authenticationService.changePassword(request);

            return ResponseEntity.ok(
                    ApiResponse.<User>builder()
                            .code(200)
                            .message("success")
                            .data(user)
                            .build()
            );

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<User>builder()
                            .code(400)
                            .message(e.getMessage())
                            .data(null)
                            .build()
            );
        }
    }



}
