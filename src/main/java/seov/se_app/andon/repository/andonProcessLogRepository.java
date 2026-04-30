package seov.se_app.andon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.andon.entity.andonProcessLog;

public interface andonProcessLogRepository extends JpaRepository<andonProcessLog, Long> {
}
