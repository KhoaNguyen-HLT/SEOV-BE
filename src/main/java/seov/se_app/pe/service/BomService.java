package seov.se_app.pe.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BomService {

    private static final int BATCH_SIZE = 3000;
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveBomData(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();


        System.out.println("File: " + file.getOriginalFilename());
        System.out.println("Size: " + file.getSize());
        System.out.println("ContentType: " + file.getContentType());

        try (InputStream check = file.getInputStream()) {
            byte[] header = check.readNBytes(4);
            System.out.println("Header = " + Arrays.toString(header));
        }


        try (InputStream is = file.getInputStream();
             ReadableWorkbook wb = new ReadableWorkbook(is)) {

            // clear tmp
            jdbcTemplate.execute("TRUNCATE TABLE bom_import_data RESTART IDENTITY");
            Sheet sheet = wb.getFirstSheet();

            List<Object[]> batchArgs = new ArrayList<>(BATCH_SIZE);

            try (Stream<Row> rows = sheet.openStream()) {
                Iterator<Row> iterator = rows.iterator();

                while (iterator.hasNext()) {

                    Row row = iterator.next();

                    // bỏ qua header
                    if (row.getRowNum() < 10) {
                        continue;
                    }
                    System.out.println(row.getRowNum());

                    if(row.getCellCount() <10) {
                        break;
                    }
                    String stt = getString(row.getCell(1));
                    String bom_link = getString(row.getCell(2));
                    String type = getString(row.getCell(3));
                    String prd_code = getString(row.getCell(4));
                    String product_Code = getString(row.getCell(5));
                    String product_Name = getString(row.getCell(6));
                    String material_Code = getString(row.getCell(7));
                    if (material_Code.isBlank()) {
                        material_Code = getString(row.getCell(8));
                    }
                    String custom_Mode = getString(row.getCell(9));
                    String material_Name = getString(row.getCell(10));
                    String vietnamese_Name = getString(row.getCell(11));

                    BigDecimal norm_Sei = getBigDecimal(row.getCell(12));
                    BigDecimal norm_Seov = getBigDecimal(row.getCell(13));

                    String gscm_eng = getString(row.getCell(14));
                    String gscm_vnese = getString(row.getCell(15));
                    String eng_unit = getString(row.getCell(16));
                    String vnese_unit = getString(row.getCell(17));
                    BigDecimal  for_pm = row.getCellCount() > 18 ? getBigDecimal(row.getCell(18)) : null;
                    String gscm_type = row.getCellCount() > 19 ? getString(row.getCell(19)) : "";



                    batchArgs.add(new Object[]{
                            stt,
                            bom_link,
                            type,
                            prd_code,
                            product_Code,
                            product_Name,
                            material_Code,
                            custom_Mode,
                            material_Name,
                            vietnamese_Name,
                            norm_Sei,norm_Seov,gscm_eng,gscm_vnese, eng_unit, vnese_unit,
                            for_pm, gscm_type,
                            now,
                            now
                    });

                    if (batchArgs.size() >= BATCH_SIZE) {
                        insertTmp(batchArgs);
                        batchArgs.clear();
                    }
                }

                if (!batchArgs.isEmpty()) {
                    insertTmp(batchArgs);
                }
            }

            // replace bom
            jdbcTemplate.execute("TRUNCATE TABLE bom_data RESTART IDENTITY");
            jdbcTemplate.execute("""
                    INSERT INTO bom_data
                    (
                        stt,
                    bom_link,
                    type,
                    prd_code,
                    product_code,
                    product_name,
                    material_code,
                    custom_mode,
                    material_name,
                    vietnamese_name,
                    norm_sei,norm_seov,gscm_eng,gscm_vnese, eng_unit, vnese_unit,
                    for_pm, gscm_type,
                        created_at,
                        updated_at
                    )
                    SELECT
                        stt,
                    bom_link,
                    type,
                    prd_code,
                    product_code,
                    product_name,
                    material_code,
                    custom_mode,
                    material_name,
                    vietnamese_name,
                    norm_sei,norm_seov,gscm_eng,gscm_vnese, eng_unit, vnese_unit,
                    for_pm, gscm_type,
                        created_at,
                        updated_at
                    FROM bom_import_data
                    """);
//            jdbcTemplate.execute("TRUNCATE TABLE bom_import_data");

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    private void insertTmp(List<Object[]> batchArgs) {

        jdbcTemplate.batchUpdate("""
                INSERT INTO bom_import_data
                (
                    stt,
                    bom_link,
                    type,
                    prd_code,
                    product_code,
                    product_name,
                    material_code,
                    custom_mode,
                    material_name,
                    vietnamese_name,
                    norm_sei,norm_seov,gscm_eng,gscm_vnese, eng_unit, vnese_unit,
                    for_pm, gscm_type,
                    created_at,
                    updated_at
                )
                VALUES
                (
                    ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?
                )
                """, batchArgs);

    }

    private String getString(Cell cell) {

        if (cell == null) {
            return "";
        }

        String value = cell.getText();
        return value == null ? "" : value.trim();
    }

    private BigDecimal getBigDecimal(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }

        String value = cell.getText();

        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        value = value.trim().replace(",", "").replace("-", "");;

        return new BigDecimal(value);
    }

}
