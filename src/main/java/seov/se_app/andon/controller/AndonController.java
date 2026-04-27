package seov.se_app.andon.controller;

import org.springframework.http.ResponseEntity;
import seov.auth.dto.respone.RoleResponse;
import seov.se_app.andon.dto.request.andonUpdateRequest;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.request.andonDataRequest;
import seov.se_app.andon.dto.respon.getLinesRespone;
import seov.se_app.andon.entity.andondata;
import seov.se_app.andon.service.AndonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import seov.se_app.common.ApiResponse;

import java.util.List;
import java.util.Map;

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
    ResponseEntity<ApiResponse<andondata>> getLines(@RequestBody andonDataRequest request) {
        try {
            andondata data = andonService.callgroup(request);

            return ResponseEntity.ok(
                    ApiResponse.<andondata>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<andondata>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
        }

    @PutMapping("/updateProcessingStatus/{id}")
    ResponseEntity<ApiResponse<andondata>> updateProcessingStatus(@PathVariable Long id) {
        try {
            andondata data = andonService.updateProcessingStatus(id);

            return ResponseEntity.ok(
                    ApiResponse.<andondata>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<andondata>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }

}
