package seov.se_app.material.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.device.entity.Device;
import seov.se_app.material.entity.MaterialRequest;

import java.time.LocalDate;
import java.util.List;

public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Long> {

}
