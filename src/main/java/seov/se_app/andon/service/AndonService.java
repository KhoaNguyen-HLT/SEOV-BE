package seov.se_app.andon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.andon.dto.request.andonDataRequest;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.respon.getLinesRespone;

import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import seov.se_app.andon.entity.andondata;
import seov.se_app.andon.repository.andonRepository;

@Service
public class AndonService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    LocalDate now = LocalDate.now();
    @Autowired
    private andonRepository andonRepository;

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

    public andondata callgroup(andonDataRequest request){
        andondata andondata = new andondata();
        andondata.setSiteCode(request.getSiteCode());
        andondata.setLineName(request.getLineName());
        andondata.setErrorStage(request.getErrorStage());
        andondata.setUserCode(request.getUserCode());
        andondata.setTeam(request.getTeam());
        andondata.setDescription(request.getDescription());
        andondata.setCreated_at(now);
        andondata.setUpdated_at(now);
        andondata andondata1 = andonRepository.save(andondata);

    return andondata1;
    }



}
