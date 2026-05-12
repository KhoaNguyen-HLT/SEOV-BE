package seov.se_app.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seov.auth.dto.respone.ApiResponse;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.dto.request.DeviceGetRequest;
import seov.se_app.device.dto.request.DeviceUpdateRequest;
import seov.se_app.device.entity.Device;
import seov.se_app.device.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;
    @PostMapping("/create")
    ResponseEntity<ApiResponse<Device>>  createUser(@RequestBody DeviceCreateRequest request) {
       Device device =  deviceService.createRequest(request);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", device)
        );
    }

    @PostMapping("/getDevices")
    ResponseEntity<ApiResponse<List<Device>>> getDevices(@RequestBody DeviceGetRequest request) {
        List<Device> listDevice = deviceService.getDevices(request);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "Update success", listDevice)
        );
    }

    @PutMapping ("/update")
    ResponseEntity<ApiResponse<Device>> updateDevice(@RequestBody DeviceUpdateRequest request) {
        Device device = deviceService.updateDevice(request);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", device)
        );
    }

    @DeleteMapping("delete/{id}")
    ResponseEntity<ApiResponse<Device>> deleteDevice(@PathVariable("id") Long id) {
        Device device = deviceService.deleteDevice(id);
        return ResponseEntity.ok(
                new ApiResponse<>(200, "success", device)
        );
    };


    @GetMapping("printData/{location}")
    public ResponseEntity<byte[]> printData(@PathVariable("location") String location) throws Exception {

        byte[] data = deviceService.printData(location);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=form.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}
