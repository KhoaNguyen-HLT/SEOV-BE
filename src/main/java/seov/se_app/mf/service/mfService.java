package seov.se_app.mf.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import seov.se_app.common.service.CommonQueryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
    public class mfService {

        private final CommonQueryService commonQueryService;

        public List<Map<String, Object>> getZCodeData(String zCode) {

            String sql = """
            SELECT item_code,
                   item_name
            FROM item_master
            WHERE item_code = :zCode
        """;

            Map<String, Object> params = new HashMap<>();
            params.put("zCode",zCode );

            return commonQueryService.queryThirdDb(sql, params);
        }
    }

