package seov.se_app.common.qrcode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seov.se_app.andon.dto.request.*;
import seov.se_app.andon.dto.respon.*;
import seov.se_app.common.qrcode.service.QrcodeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/qrcode")
public class QrcodeController {
    @Autowired
    private QrcodeService QrcodeService;

    @GetMapping(value = "/genQrcode/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> genQrcode(
            @PathVariable String id
    ) throws Exception {

        byte[] qrCode = QrcodeService.generateQrCode(id, 300, 300);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }

//function xuất excel theo mã được truyền vào
    @GetMapping("/export")
    public ResponseEntity<byte[]> export() throws Exception {

        byte[] excelBytes = QrcodeService.exportExcel();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=qrcode.xlsx"
                )
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM
                )
                .body(excelBytes);
    }

}
