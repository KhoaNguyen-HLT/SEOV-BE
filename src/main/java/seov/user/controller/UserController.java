package seov.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import seov.se_app.qa.dto.response.qaResponse;
import seov.user.dto.request.UserCreationRequest;
import seov.user.dto.respon.ApiResponse;
import seov.user.entity.Department;
import seov.user.entity.Position;
import seov.user.entity.User;
import seov.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> createUser(
            @RequestBody UserCreationRequest request) {

        User user = userService.createRequest(request);

        if (user != null) {
            return ResponseEntity.ok(
                    ApiResponse.<User>builder()
                            .code(200)
                            .message("success")
                            .data(user)
                            .build()
            );
        }

        return ResponseEntity.ok(
                ApiResponse.<User>builder()
                        .code(400)
                        .message("Username already exists")
                        .data(null)
                        .build()
        );
    }

    @GetMapping("/getUsers")
    List<User> getUser() {
        return userService.getUsers();
    }


    @GetMapping("/getDepartment")
    ResponseEntity<ApiResponse<List<Department>>> getDepartment() {
        List<Department> departments = userService.getDepartment();
        if (departments != null && !departments.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<Department>>builder()
                            .code(200)
                            .message("success")
                            .data(departments)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.<List<Department>>builder()
                            .code(400)
                            .message("error")
                            .data(null)
                            .build()
            );
        }

    }

    @GetMapping("/getPosition")
    ResponseEntity<ApiResponse<List<Position>>> getPosition() {
        List<Position> positions = userService.getPosition();
        if (positions != null && !positions.isEmpty()) {
            return ResponseEntity.ok(
                    ApiResponse.<List<Position>>builder()
                            .code(200)
                            .message("success")
                            .data(positions)
                            .build()
            );
        } else {
            return ResponseEntity.ok(
                    ApiResponse.<List<Position>>builder()
                            .code(200)
                            .message("error")
                            .data(null)
                            .build()
            );
        }

    }

    @GetMapping("/{username}")
    User getpidvn_user(@PathVariable Long username  ){
        return userService.getUserid(username);
    }

    @GetMapping("/getUserCustom")
    List<User> GetUserCustom(@RequestParam String username  ){
        return userService.getUserListCustom(username);
    }

    @PutMapping ("/updateUser")
    User updateUser(@RequestBody User request) {
        return userService.updateUser(request);
    }
    @DeleteMapping("/{userId}")
    User deleteUser(@PathVariable("userId") Long userId) {
        return userService.deleteUser(userId);
    };


}
