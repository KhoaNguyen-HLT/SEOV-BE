package seov.se_app.device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.device.dto.request.DeviceCreateRequest;
import seov.se_app.device.dto.request.DeviceGetRequest;
import seov.se_app.device.dto.request.DeviceUpdateRequest;
import seov.se_app.device.entity.Device;
import seov.se_app.device.mapper.DeviceMapper;
import seov.se_app.device.repository.DeviceRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public Device createRequest(DeviceCreateRequest request){
        LocalDateTime now = LocalDateTime.now();
        Device device = deviceMapper.todevice(request);
        device.setCreatedAt(now);
        device.setUpdatedAt(now);
        return deviceRepository.save(device);
    }
    public Device updateDevice(DeviceUpdateRequest request){
        Device device = deviceRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Not found"));

        deviceMapper.updateDevice(request, device);

        return deviceRepository.save(device);
    }
//
//    public void getUser1() {
//        List<User> list = userRepository.findAll();
//        messagingTemplate.convertAndSend("/topic/users", list);
//    }
//
//
    public List<Device> getDevices(DeviceGetRequest request){
        return deviceRepository.getDevices(request.getLocation(), request.getFromDate(), request.getToDate());
    }
//    public User getUserid(Long username){
//        return userRepository.findAllById(username);
//    }
//
//    public List<User> getUserListCustom(String username){
//        return userRepository.getUserListCustom(username);
//    }
//
//
//
    public Device deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        deviceRepository.delete(device);
        return device;
    }



}
