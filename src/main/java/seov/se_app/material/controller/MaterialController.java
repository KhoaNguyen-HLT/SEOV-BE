package seov.se_app.material.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seov.auth.dto.respone.ApiResponse;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.entity.Device;
import seov.se_app.device.service.DeviceService;

@RestController
@RequestMapping("/pm/material")
public class MaterialController {
    @Autowired
    private DeviceService deviceService;
    @PostMapping("/create")
    ResponseEntity<ApiResponse<Device>>  createUser(@RequestBody DeviceCreateRequest request) {
       Device device =  deviceService.createRequest(request);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", device)
        );
    }


}
