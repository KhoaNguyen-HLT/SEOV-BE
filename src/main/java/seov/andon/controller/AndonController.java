package seov.andon.controller;

import seov.andon.dto.respon.getLinesRespone;
import seov.andon.service.AndonService;
import seov.user.dto.request.UserCreationRequest;
import seov.user.entity.User;
import seov.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/andon")
public class AndonController {
    @Autowired
    private AndonService andonService;

    @GetMapping("/getLines")
    List<getLinesRespone> getLines() {
        return andonService.getLines();
    }
}
