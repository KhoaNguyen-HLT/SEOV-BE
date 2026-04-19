package seov.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import seov.ws.entity.MachinesData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MachineDataRepository extends JpaRepository<MachinesData,String> {

    @Query(value = """
    SELECT 
        m.machine_code AS machineCode,
        Sum(m.qty) AS qty,
        5000 AS target
    FROM machines_data m
    WHERE m.created_at >= :today group by machine_code
""", nativeQuery = true)
    List<Map<String, Object>> findMachinesDataByCreatedAt(LocalDateTime today);
//    @Query("SELECT m.machineCode, SUM(m.qty) FROM MachinesData m WHERE m.created_at > :today GROUP BY m.machineCode")
//    List<getMachineDataDto> findMachinesDataByCreatedAt(@Param("today") LocalDateTime today);
    @Query("SELECT SUM(md.qty) as qtySum FROM MachinesData md")
    List<Object[]> getDataTotal();

}
