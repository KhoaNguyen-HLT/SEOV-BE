package seov.se_app.common.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.andon.dto.request.*;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.respon.andonSenRequestRespone;
import seov.se_app.andon.dto.respon.getLinesRespone;
import seov.se_app.andon.entity.andonHandlingDetail;
import seov.se_app.andon.entity.andonProcessLog;
import seov.se_app.andon.entity.andondata;
import seov.se_app.andon.repository.andonHandlingDetailRepository;
import seov.se_app.andon.repository.andonProcessLogRepository;
import seov.se_app.andon.repository.andonRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.awt.SystemColor.text;

@Service
public class QrcodeService {

    public byte[] generateQrCode(String text, int width, int height) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        // bỏ margin mặc định của ZXing
        hints.put(EncodeHintType.MARGIN, 0);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(
                text,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
        );
        // crop + thêm viền trắng đẹp
        bitMatrix = deleteWhite(bitMatrix, 10);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(
                bitMatrix,
                "PNG",
                outputStream
        );
        return outputStream.toByteArray();
    }

    private BitMatrix deleteWhite(BitMatrix matrix, int padding) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2];
        int resHeight = rec[3];
        // tạo matrix mới có thêm padding
        BitMatrix resMatrix = new BitMatrix(
                resWidth + padding * 2,
                resHeight + padding * 2
        );
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {

                if (matrix.get(i + rec[0], j + rec[1])) {

                    // dịch vào giữa để tạo viền trắng
                    resMatrix.set(i + padding, j + padding);
                }
            }
        }
        return resMatrix;
    }


    public byte[] exportExcel() throws Exception {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("QR");

        // sample data
        List<String> orders = List.of(
                "AS170527",
                "AS170528",
                "AS170529"
        );

        // header
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Order");
        header.createCell(1).setCellValue("QR Code");

        CreationHelper helper = workbook.getCreationHelper();

        Drawing<?> drawing = sheet.createDrawingPatriarch();

        int rowNum = 1;

        for (String order : orders) {

            Row row = sheet.createRow(rowNum);

            row.setHeightInPoints(90);

            row.createCell(0).setCellValue(order);

            // generate qr
            byte[] qrBytes = generateQrCode(
                    order,
                    200,
                    200
            );

            // add picture
            int pictureIdx = workbook.addPicture(
                    qrBytes,
                    Workbook.PICTURE_TYPE_PNG
            );

            ClientAnchor anchor = helper.createClientAnchor();

            anchor.setCol1(1);
            anchor.setRow1(rowNum);

            Picture picture = drawing.createPicture(
                    anchor,
                    pictureIdx
            );

            // resize fit cell
            picture.resize(0.6);

            rowNum++;
        }

        // width
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        workbook.write(out);

        workbook.close();

        return out.toByteArray();
    }
}
