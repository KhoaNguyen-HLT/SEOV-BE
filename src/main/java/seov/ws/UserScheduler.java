package seov.ws;

import seov.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import seov.ws.service.MachineService;

@Component
@RequiredArgsConstructor
public class UserScheduler {
    private final UserService userService;
    private final MachineService machineService;

//    @Scheduled(fixedRate = 5000) // 👈 5 giây
//    public void run1() {
//        System.out.println("Push data fe mỗi 5s...");
//        machineService.GetMachineData();
//    }
//    @Scheduled(fixedRate = 5000) // 👈 5 giây
//    public void run2() {
//        System.out.println("Push data fe mỗi 5s...");
//        machineService.getDataTotal();
//    }
//
//
//        @Scheduled(fixedRate = 20000) // 👈 5 giây
//    public void run() {
//        System.out.println("Push dataMachine mỗi 5s...");
//            machineService.addMachineData();
//    }
}
