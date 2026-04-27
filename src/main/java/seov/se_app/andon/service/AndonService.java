package seov.se_app.andon.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.andon.dto.request.andonDataRequest;
import seov.se_app.andon.dto.request.andonUpdateRequest;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.respon.getLinesRespone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import seov.se_app.andon.entity.andondata;
import seov.se_app.andon.repository.andonRepository;
import seov.se_app.common.ApiResponse;

@Service
public class AndonService {


    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;
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
        LocalDateTime now = LocalDateTime.now();
        andondata andondata = new andondata();
        andondata.setSiteCode(request.getSiteCode());
        andondata.setLineName(request.getLineName());
        andondata.setErrorStage(request.getErrorStage());
        andondata.setUserCode(request.getUserCode());
        andondata.setTeam(request.getTeam());
        andondata.setDescription(request.getDescription());
        andondata.setStatus(request.getStatus());
        andondata.setCreated_at(now);
        andondata.setUpdated_at(now);
        andondata andondata1 = andonRepository.save(andondata);
    return andondata1;
    }


    @Transactional
    public andondata updateProcessingStatus(Long id){

        andondata data = andonRepository.findById(id)
                .orElseThrow();

        data.setStatus("PROCESSING");
        data.setProcessingAt(LocalDateTime.now());

        return data; // JPA sẽ tự update vì dùng transection.
    }



}
