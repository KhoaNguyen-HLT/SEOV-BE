package seov.se_app.device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.device.dto.request.DeviceCreateRequest;
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
//    public User updateUser(User request){
//        User userUpate = getUserid(request.getId());
//       userMapper.updateUser(userUpate, request);
////        userUpate.setPassword(request.getPassword());
//        return userRepository.save(userUpate);
//    }
//
//    public void getUser1() {
//        List<User> list = userRepository.findAll();
//        messagingTemplate.convertAndSend("/topic/users", list);
//    }
//
//
    public List<Device> getDevices(){
        return deviceRepository.findAll();
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
//    public User deleteUser(Long userId) {
//        User user = getUserid(userId);
//        userRepository.delete(user);
//        return user;
//    }



}
