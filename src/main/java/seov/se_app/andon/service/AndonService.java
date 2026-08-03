package seov.se_app.andon.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import seov.se_app.andon.dto.request.*;
import seov.se_app.andon.dto.respon.andonDataRespone;
import seov.se_app.andon.dto.respon.andonSenRequestRespone;
import seov.se_app.andon.dto.respon.getLinesRespone;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import seov.se_app.andon.entity.andonHandlingDetail;
import seov.se_app.andon.entity.andonProcessLog;
import seov.se_app.andon.entity.andondata;
import seov.se_app.andon.repository.andonHandlingDetailRepository;
import seov.se_app.andon.repository.andonProcessLogRepository;
import seov.se_app.andon.repository.andonRepository;
import seov.se_app.common.service.CommonQueryService;
import seov.se_app.mf.dto.request.MaterialRequestDataPu;

@Service
@RequiredArgsConstructor
public class AndonService {
    private final CommonQueryService commonQueryService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private EntityManager entityManager;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
    private final JdbcTemplate jdbcTemplatesc;
    @Autowired
    private andonRepository andonRepository;
    @Autowired
    private andonProcessLogRepository andonProcessLogRepository;
    @Autowired
    private andonHandlingDetailRepository andonHandlingDetailRepository;



//    public AndonService(
//            @Qualifier("secondaryJdbcTemplate") JdbcTemplate jdbcTemplatesc
//    ) {
//        this.jdbcTemplatesc = jdbcTemplatesc;
//    }



    public List<getLinesRespone> getLines() {

        String sql = "SELECT top 10 SiteCode, LineName FROM mstSiteControllers";

        Map<String, Object> params = new HashMap<>();
        List<Map<String, Object>> data = commonQueryService.queryThirdDb(sql, params);

        List<getLinesRespone> result = data.stream().map(row -> new getLinesRespone(
                (String) row.get("SiteCode"),
                (String) row.get("LineName")
        )).toList();

        return result;
    }

//    public List<getLinesRespone> getLines() {
//
//            String sql = "SELECT top 10 SiteCode, LineName FROM mstSiteControllers";
//
//            return jdbcTemplatesc.query(sql, (rs, rowNum) -> {
//                getLinesRespone dto = new getLinesRespone();
//                dto.setSiteCode(rs.getString("SiteCode"));
//                dto.setLineName(rs.getString("LineName"));
//                return dto;
//            });
//
//    }


    public List<Map<String, Object>> andonGetData(andonGetDataRequest request) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime fromDate =
                LocalDateTime.parse(request.getFromDate(), formatter);

        LocalDateTime toDate =
                LocalDateTime.parse(request.getToDate(), formatter);

        return andonRepository.andonGetData(
                request.getLine(),
                fromDate,
                toDate
        );
    }

    public ResponseEntity<andonSenRequestRespone> sendRequest(String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("khoa-nv@gr.sei.co.jp");
            message.setSubject("khoatest");
            message.setText("khoatest");

            mailSender.send(message);

            return ResponseEntity.ok(new andonSenRequestRespone(200, "Send email success", null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body( new andonSenRequestRespone(500, "Send email failed", e.getMessage()));
        }
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
    public andonDataRespone changeGroup(andonChangeGroupRequest request){

        LocalDateTime now = LocalDateTime.now();

        // 1. đóng log cũ
//        andonProcessLogRepository.closeActiveLog(request.getId(), now);

        // 2. insert log mới
        andonProcessLog log = andonProcessLog.builder()
                .request_id(request.getId())
                .fromTeam(request.getFrom_team())
                .toTeam(request.getTo_team())
                .description(request.getReason())
                .fromUser(request.getFrom_user())
                .status("PROCESSING")
                .actionType("TRANSFER")
                .startTime(request.getStart_time())
                .endTime(now)
                .createdAt(now)
                .updatedAt(now)
                .isActive(true)
                .build();

        andonProcessLogRepository.save(log);

        // 3. update ticket (đúng cách)
        andondata andondata = andonRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Not found"));

        andondata.setTeam(request.getTo_team());
        andondata.setUpdated_at(now);

//        andonRepository.save(andondata);
//        Flush đảm bảo việc save thành công rồi mới gọi lại data.
        andonRepository.saveAndFlush(andondata);
        entityManager.refresh(andondata);
        // 4. query lại đúng ID
        andondata data = andonRepository.findWithGroup(request.getId());

        return andonDataRespone.builder()
                .id(data.getId())
                .siteCode(data.getSiteCode())
                .lineName(data.getLineName())
                .description(data.getDescription())
                .errorStage(data.getErrorStage())
                .team(data.getTeam())
                .status(data.getStatus())
                .groupName(data.getGroup() != null ? data.getGroup().getGroupName() : null)
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
        data.setUpdated_at(LocalDateTime.now());
        return data; // JPA sẽ tự update vì dùng transection.
    }

    @Transactional
    public andondata updateDoneStatus(Long id, andonHandlingDetailRequest request){
        LocalDateTime now = LocalDateTime.now();

        // 1. Lấy andon trước
        andondata data = andonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Andon"));

        // 2. Check duplicate
        if (andonHandlingDetailRepository.existsByRequestId(id)) {
            throw new RuntimeException("Đã có detail rồi");
        }
        andonHandlingDetail detail = new andonHandlingDetail();
        detail.setRequestId(id);
        detail.setMethod(request.getMethod());
        detail.setRepairNotes(request.getRepairNotes());
        detail.setOldDevice(request.getOldDevice());
        detail.setNewDevice(request.getNewDevice());
        detail.setOldStatus(request.getOldStatus());
        detail.setReplaceReason(request.getReplaceReason());
        detail.setCreatedAt(now);
        andonHandlingDetailRepository.save(detail);

        data.setStatus("OK");
        data.setCompletedAt(LocalDateTime.now());
        return data; // JPA sẽ tự update vì dùng transection.
    }


    public List<Map<String, Object>> getChangeGroupData(andonChangeGroupRequest request) {
        List<Map<String, Object>> data =  andonProcessLogRepository.getChangeGroupData((Long) request.getId());
        return data;
    }

    public List<Map<String, Object>> getDataDataPendingLog(String siteCode) {
        List<Map<String, Object>> data = andonRepository.getDataDataPendingLog(siteCode);
        return data;
    }


    public List<Map<String, Object>> andonDashboardData(andonGetDashboardData request) {
        List<Map<String, Object>> data = andonRepository.andonDashboardData();
        return data;
    }








}
