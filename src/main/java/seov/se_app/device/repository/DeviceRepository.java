package seov.se_app.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.se_app.device.entity.Device;
import seov.user.entity.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("""
    SELECT d FROM Device d
    WHERE d.location LIKE CONCAT('%', :location, '%')
      AND d.dateTime BETWEEN :fromDate AND :toDate
""")
    List<Device> getDevices(
            @Param("location") String location,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
}
