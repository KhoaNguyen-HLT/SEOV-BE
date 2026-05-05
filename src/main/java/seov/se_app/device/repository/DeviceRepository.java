package seov.se_app.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.se_app.device.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

}
