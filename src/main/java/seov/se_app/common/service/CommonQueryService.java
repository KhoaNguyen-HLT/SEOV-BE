package seov.se_app.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CommonQueryService {

    private final NamedParameterJdbcTemplate primaryDb;
    private final NamedParameterJdbcTemplate secondaryDb;
    private final NamedParameterJdbcTemplate thirdDb;
    private final JdbcTemplate primaryJdbcTemplate;

    // Danh sách các keyword SQL nguy hiểm - không cho phép chạy trên DB ngoài
    private static final Set<String> BLOCKED_KEYWORDS = Set.of(
            "INSERT", "UPDATE", "DELETE", "DROP", "ALTER",
            "TRUNCATE", "CREATE", "REPLACE", "MERGE", "EXEC", "EXECUTE"
    );

    public CommonQueryService(
            @Qualifier("primaryNamedJdbcTemplate")
            NamedParameterJdbcTemplate primaryDb,
            @Qualifier("secondaryNamedJdbcTemplate")
            NamedParameterJdbcTemplate secondaryDb,
            @Qualifier("thirdNamedJdbcTemplate")
            NamedParameterJdbcTemplate thirdDb,
            @Qualifier("primaryJdbcTemplate")
            JdbcTemplate primaryJdbcTemplate

    ) {

        this.primaryDb = primaryDb;
        this.secondaryDb = secondaryDb;
        this.thirdDb = thirdDb;
        this.primaryJdbcTemplate = primaryJdbcTemplate;
    }

    /**
     * Query trên DB chính (primary) - không giới hạn loại SQL.
     */
    public List<Map<String, Object>> queryMainDb(String sql, Map<String, Object> params) {
        return primaryDb.queryForList(sql, params);
    }

    /**
     * Query trên DB phụ (secondary) - CHỈ cho phép SELECT.
     */
    public List<Map<String, Object>> querySecondaryDb(
            String sql,
            Map<String, Object> params) {

        validateReadOnly(sql, "secondaryDb");
        return secondaryDb.queryForList(sql, params);
    }

    /**
     * Query trên DB thứ ba (third) - CHỈ cho phép SELECT.
     */
    public List<Map<String, Object>> queryThirdDb(
            String sql,
            Map<String, Object> params) {

        validateReadOnly(sql, "thirdDb");
        return thirdDb.queryForList(sql, params);
    }




    // =========================
    // UPDATE thực hiện thao tác trên DB chính
    // =========================

    public int updateMainDb(
            String sql,
            Object... args) {

        return primaryJdbcTemplate.update(sql, args);
    }

    public int[] batchUpdateMainDb(
            String sql,
            List<Object[]> batchArgs) {

        return primaryJdbcTemplate.batchUpdate(sql, batchArgs);
    }

    public void executeMainDb(String sql) {
        primaryJdbcTemplate.execute(sql);
    }


    /**
     * Kiểm tra câu SQL chỉ được phép là SELECT.
     * Chặn các lệnh INSERT, UPDATE, DELETE, DROP, ALTER, TRUNCATE, ...
     *
     * @param sql      câu SQL cần kiểm tra
     * @param dbName   tên DB (dùng cho thông báo lỗi)
     */
    private void validateReadOnly(String sql, String dbName) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("SQL query không được để trống");
        }

        // Lấy keyword đầu tiên của câu SQL (bỏ qua khoảng trắng, xuống dòng)
        String firstKeyword = sql.trim().split("\\s+")[0].toUpperCase();

        if (BLOCKED_KEYWORDS.contains(firstKeyword)) {
            throw new SecurityException(
                    String.format("Không cho phép chạy lệnh [%s] trên [%s]. Chỉ được phép SELECT.",
                            firstKeyword, dbName)
            );
        }
    }
}
