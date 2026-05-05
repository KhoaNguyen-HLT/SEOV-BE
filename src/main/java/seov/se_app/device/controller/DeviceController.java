package seov.se_app.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.entity.Device;
import seov.se_app.device.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @PostMapping("/create")
    Device createUser(@RequestBody DeviceCreateRequest request) {
        return deviceService.createRequest(request);
    }

    @GetMapping("/getDevices")
    List<Device> getDevices() {
        return deviceService.getDevices();
    }
//
//    @GetMapping("/{username}")
//    User getpidvn_user(@PathVariable Long username  ){
//        return userService.getUserid(username);
//    }
//
//    @GetMapping("/getUserCustom")
//    List<User> GetUserCustom(@RequestParam String username  ){
//        return userService.getUserListCustom(username);
//    }
//
//    @PutMapping ("/updateUser")
//    User updateUser(@RequestBody User request) {
//        return userService.updateUser(request);
//    }
//    @DeleteMapping("/{userId}")
//    User deleteUser(@PathVariable("userId") Long userId) {
//        return userService.deleteUser(userId);
//    };
}
