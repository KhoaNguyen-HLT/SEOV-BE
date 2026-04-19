package seov.user.controller;

import seov.user.dto.request.UserCreationRequest;
import seov.user.entity.User;
import seov.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/create")
    User createUser(@RequestBody UserCreationRequest request) {
        return userService.createRequest(request);
    }

    @GetMapping("/getUsers")
    List<User> getUser() {
        return userService.getUsers();
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
