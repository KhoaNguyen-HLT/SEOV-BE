package seov.se_app.common.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class CommonQueryService {

    private final NamedParameterJdbcTemplate primaryDb;
    private final NamedParameterJdbcTemplate secondaryDb;
    private final NamedParameterJdbcTemplate thirdDb;
    public CommonQueryService(
            @Qualifier("primaryNamedJdbcTemplate")
            NamedParameterJdbcTemplate primaryDb,

            @Qualifier("secondaryNamedJdbcTemplate")
            NamedParameterJdbcTemplate secondaryDb,
            @Qualifier("thirdNamedJdbcTemplate")
            NamedParameterJdbcTemplate thirdDb) {

        this.primaryDb = primaryDb;
        this.secondaryDb = secondaryDb;
        this.thirdDb = thirdDb;
    }

    public List<Map<String, Object>> queryMainDb(String sql, Map<String, Object> params) {
        return primaryDb.queryForList(sql, params);
    }

    public List<Map<String, Object>> querySecondaryDb(
            String sql,
            Map<String, Object> params) {

        return secondaryDb.queryForList(sql, params);
    }

    public List<Map<String, Object>> queryThirdDb(
            String sql,
            Map<String, Object> params) {
        return thirdDb.queryForList(sql, params);
    }
}
