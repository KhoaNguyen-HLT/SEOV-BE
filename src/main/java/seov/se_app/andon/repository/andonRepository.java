package seov.se_app.andon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.andon.entity.andondata;

public interface andonRepository extends JpaRepository<andondata, Long> {

}
