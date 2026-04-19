package seov.ws.controller;

import seov.user.entity.User;
import seov.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private UserService userService;
//    @MessageMapping("/hello")
    @SendTo("/topic/users")
    public List<User> getUser() {
        return userService.getUsers();
    }
}