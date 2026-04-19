package seov.user.service;

import seov.user.dto.request.UserCreationRequest;
import seov.user.entity.User;
import seov.user.mapper.UserMapper;
import seov.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    LocalDate now = LocalDate.now();
    public User createRequest(UserCreationRequest request){
    User user = userMapper.toUser(request);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String password = encoder.encode(request.getPassword());
        user.setPassword(password);
        user.setCreated_at(now);
        user.setUpdated_at(now);
        return userRepository.save(user);
    }
    public User updateUser(User request){
        User userUpate = getUserid(request.getId());
       userMapper.updateUser(userUpate, request);
//        userUpate.setPassword(request.getPassword());
        return userRepository.save(userUpate);
    }

    public void getUser1() {
        List<User> list = userRepository.findAll();
        messagingTemplate.convertAndSend("/topic/users", list);
    }


    public List<User> getUsers(){
        return userRepository.findAll();
    }
    public User getUserid(Long username){
        return userRepository.findAllById(username);
    }

    public List<User> getUserListCustom(String username){
        return userRepository.getUserListCustom(username);
    }



    public User deleteUser(Long userId) {
        User user = getUserid(userId);
        userRepository.delete(user);
        return user;
    }



}
