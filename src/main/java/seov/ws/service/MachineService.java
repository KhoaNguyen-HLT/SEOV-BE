package seov.ws.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.ws.dto.request.MachineRequest;
import seov.ws.entity.Machines;
import seov.ws.entity.MachinesData;
import seov.ws.mapper.MachineMapper;
import seov.ws.repository.MachineDataRepository;
import seov.ws.repository.MachineRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class MachineService {
@Autowired
private MachineRepository machineRepository;
@Autowired
private MachineDataRepository machineDataRepository;
@Autowired
public MachineMapper machineMapper;
@Autowired
private SimpMessagingTemplate messagingTemplate;

    public Machines createMachine(MachineRequest request){
        Machines machine = machineMapper.toMachine(request);
        machine.setCreated_at(LocalDateTime.now());
        machine.setUpdated_at(LocalDateTime.now());
        return machineRepository.save(machine);
    }

    public void GetMachineData(){
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<Map<String, Object>> list =  machineDataRepository.findMachinesDataByCreatedAt(today);
        messagingTemplate.convertAndSend("/topic/users", list);
    }


    public void getDataTotal(){
        List<Object[]> list =  machineDataRepository.getDataTotal();
        messagingTemplate.convertAndSend("/topic/datatotal", list);
    }

    public MachinesData addMachineData(){
        MachinesData machinesData = new MachinesData();
        int random = new Random().nextInt(10) + 1;
        int randomQty = new Random().nextInt(100);
        int[] values = {8, 10, 12};
        int randomS = values[new Random().nextInt(values.length)];
        String machineCode = String.format("MC%02d", random);
        String machineShift = String.format("S%02d", randomS);
        machinesData.setMachineCode(machineCode);
        machinesData.setQty(randomQty);
        machinesData.setShift(machineShift);
        machinesData.setCreated_at(LocalDateTime.now());
        machinesData.setUpdated_at(LocalDateTime.now());
        return machineDataRepository.save(machinesData);
    }

}
