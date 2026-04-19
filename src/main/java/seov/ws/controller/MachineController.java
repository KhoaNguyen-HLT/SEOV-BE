package seov.ws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import seov.ws.dto.request.MachineRequest;
import seov.ws.entity.Machines;
import seov.ws.service.MachineService;


@RestController
@RequestMapping("/machine")
public class MachineController {
    @Autowired
    private MachineService machineService;
    @PostMapping
    public Machines CreateMachine(@RequestBody MachineRequest request){

    return machineService.createMachine(request);
}

}
