package seov.se_app.andon.controller;

import org.springframework.http.ResponseEntity;
import seov.auth.dto.respone.RoleResponse;
import seov.se_app.andon.dto.request.andonChangeGroupRequest;
import seov.se_app.andon.dto.request.andonGetDataRequest;
import seov.se_app.andon.dto.request.andonHandlingDetailRequest;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.request.andonDataRequest;
import seov.se_app.andon.dto.respon.andonSenRequestRespone;
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


    @PostMapping("/andonGetData")
    List<Map<String, Object>> andonGetData(@RequestBody andonGetDataRequest request) {

        return andonService.andonGetData(request);
    }


    @PostMapping("/sendRequest/{id}")
    ResponseEntity<andonSenRequestRespone> sendRequest(@PathVariable String id) {
       return andonService.sendRequest(id);
    }



    @GetMapping("/getDataPending/{siteCode}")
    ResponseEntity<ApiResponse<List<andonDataRespone>>> getDataPending(@PathVariable String siteCode) {
        try {
            List<andonDataRespone> data = andonService.getDataPending(siteCode);

            return ResponseEntity.ok(
                    ApiResponse.<List<andonDataRespone>>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<List<andonDataRespone>>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }



    @PostMapping("/callgroup")
    ResponseEntity<ApiResponse<andonDataRespone>> callgroup(@RequestBody andonDataRequest request) {
        try {
            andonDataRespone data = andonService.callgroup(request);

            return ResponseEntity.ok(
                    ApiResponse.<andonDataRespone>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<andonDataRespone>builder()
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

    @PutMapping("/updateDoneStatus/{id}")
    ResponseEntity<ApiResponse<andondata>> updateDoneStatus(@PathVariable Long id, @RequestBody andonHandlingDetailRequest request) {
        try {
            andondata data = andonService.updateDoneStatus(id, request);

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

    @PutMapping("/changeGroup")
    ResponseEntity<ApiResponse<andonDataRespone>> changeGroup(@RequestBody andonChangeGroupRequest request) {
        try {
            andonDataRespone data = andonService.changeGroup(request);

            return ResponseEntity.ok(
                    ApiResponse.<andonDataRespone>builder()
                            .code(200)
                            .message("success")
                            .data(data)
                            .build()
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    ApiResponse.<andonDataRespone>builder()
                            .code(500)
                            .message("error")
                            .data(null)
                            .build()
            );
        }
    }






}
