package seov.se_app.andon.controller;

import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.request.andonDataRequest;
import seov.se_app.andon.dto.respon.getLinesRespone;
import seov.se_app.andon.entity.andondata;
import seov.se_app.andon.service.AndonService;
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

    @PostMapping("/callgroup")
    andondata getLines(@RequestBody andonDataRequest request) {

        return andonService.callgroup(request);
    }

}
