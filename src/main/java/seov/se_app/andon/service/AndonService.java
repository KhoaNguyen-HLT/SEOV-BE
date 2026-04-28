package seov.se_app.andon.service;

import jakarta.persistence.EntityManager;
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
    private EntityManager entityManager;

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



    public List<andonDataRespone> getDataPending(String siteCode) {

        try {
            List<Map<String, Object>> list = andonRepository.findDataPendingByLine(siteCode);

            return list.stream().map(data ->
                    andonDataRespone.builder()
                            .id(data.get("id") != null ? ((Number) data.get("id")).longValue() : null)
                            .siteCode((String) data.get("siteCode"))
                            .lineName((String) data.get("lineName"))
                            .description((String) data.get("description"))
                            .errorStage((String) data.get("errorStage"))
                            .team(data.get("team") != null ? ((Number) data.get("team")).intValue() : null)
                            .groupName((String) data.get("groupName"))
                            .status((String) data.get("status"))
                            .flag((String) data.get("flag"))
                            .processingAt(data.get("processingAt") != null
                                    ? ((java.sql.Timestamp) data.get("processingAt")).toLocalDateTime()
                                    : null)
                            .completedAt(data.get("completedAt") != null
                                    ? ((java.sql.Timestamp) data.get("completedAt")).toLocalDateTime()
                                    : null)
                            .created_at(data.get("createdAt") != null
                                    ? ((java.sql.Timestamp) data.get("createdAt")).toLocalDateTime()
                                    : null)
                            .updated_at(data.get("updatedAt") != null
                                    ? ((java.sql.Timestamp) data.get("updatedAt")).toLocalDateTime()
                                    : null)
                            .build()
            ).toList();

        } catch (Exception e) {
            throw new RuntimeException("Error get data pending", e);
        }
    }

    @Transactional
    public andonDataRespone  callgroup(andonDataRequest request){
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
        andondata.setFlag("true");
//        Lưu database
        andondata saved = andonRepository.save(andondata);
        entityManager.flush();
        entityManager.clear();
        andondata data = andonRepository.findWithGroup(saved.getId());
        // 👇 query lại để lấy group
        // 👉 map DTO
        return andonDataRespone.builder()
                .id(data.getId())
                .siteCode(data.getSiteCode())
                .lineName(data.getLineName())
                .description(data.getDescription())
                .errorStage(data.getErrorStage())
                .team(data.getTeam())
                .status(data.getStatus())
                .groupName(
                        data.getGroup() != null ? data.getGroup().getGroupName() : null
                )
                .flag(data.getFlag())
                .processingAt(data.getProcessingAt())
                .completedAt(data.getCompletedAt())
                .created_at(data.getCreated_at())
                .updated_at(data.getUpdated_at())
                .build();
    }


    @Transactional
    public andondata updateProcessingStatus(Long id){

        andondata data = andonRepository.findById(id)
                .orElseThrow();

        data.setStatus("PROCESSING");
        data.setProcessingAt(LocalDateTime.now());
        return data; // JPA sẽ tự update vì dùng transection.
    }

    @Transactional
    public andondata updateDoneStatus(Long id){

        andondata data = andonRepository.findById(id)
                .orElseThrow();

        data.setStatus("OK");
        data.setCompletedAt(LocalDateTime.now());
        return data; // JPA sẽ tự update vì dùng transection.
    }



}
