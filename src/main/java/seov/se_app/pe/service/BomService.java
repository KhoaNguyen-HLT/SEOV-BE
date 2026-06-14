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
    LocalDateTime now =  LocalDateTime.now();

    @Transactional
    public void saveBomData(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            return;
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
                    if (row.getRowNum() < 3) {
                        continue;
                    }
                    String itemProduct = getString(row.getCell(1));
                    String itemCode = getString(row.getCell(2));
                    if (itemCode.isBlank()) {
                        continue;
                    }

                    BigDecimal quantity = getBigDecimal(row.getCell(3));

                    batchArgs.add(new Object[]{
                            itemProduct,
                            itemCode,
                            quantity,
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
                        item_product,
                        item_code,
                        quantity,
                        created_at,
                        updated_at
                    )
                    SELECT
                        item_product,
                        item_code,
                        quantity,
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
                    item_product,
                    item_code,
                    quantity,
                    created_at,
                    updated_at
                )
                VALUES
                (
                    ?, ?, ?, ?, ?
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

        return new BigDecimal(value.trim());
    }

}
