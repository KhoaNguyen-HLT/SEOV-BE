package seov.andon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.andon.dto.respon.getLinesRespone;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
@Service
public class AndonService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    LocalDate now = LocalDate.now();
    public List<getLinesRespone> getLines() {

            String sql = "SELECT top 10 SiteCode, LineName FROM mstSiteControllers";

            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                getLinesRespone dto = new getLinesRespone();
                dto.setSiteCode(rs.getString("SiteCode"));
                dto.setLineName(rs.getString("LineName"));
                return dto;
            });
//        getLinesRespone dto = new getLinesRespone();
//        dto.setSiteCode("SiteCode");
//        dto.setLineName("LineName");
//        return dto;
    }



}
