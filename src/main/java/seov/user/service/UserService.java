package seov.user.service;

import lombok.RequiredArgsConstructor;
import seov.auth.entity.Role;
import seov.auth.repository.roleRepository;
import seov.user.dto.request.UserCreationRequest;
import seov.user.entity.Department;
import seov.user.entity.Position;
import seov.user.entity.User;
import seov.user.mapper.UserMapper;
import seov.user.repository.DepartmentRepository;
import seov.user.repository.PositionRepository;
import seov.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final roleRepository roleRepository;

    LocalDate now = LocalDate.now();
    public User createRequest(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = userMapper.toUser(request);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword(encoder.encode(request.getPassword()));

        user.setEmail(request.getEmail());
        user.setDepartment(request.getDepartment());
        user.setPosition(request.getPosition());
        user.setCreated_at(now);
        user.setUpdated_at(now);

        Set<Role> roles = new HashSet<>(
                roleRepository.findAllById(request.getRole())
        );

        user.setRoles(roles);

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

    public List<Department> getDepartment(){
        return departmentRepository.findAll();
    }

    public List<Position> getPosition(){
        return positionRepository.findAll();
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
