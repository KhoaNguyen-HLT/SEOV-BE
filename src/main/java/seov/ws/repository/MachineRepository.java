package seov.ws.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.ws.entity.Machines;

public interface MachineRepository extends JpaRepository<Machines,String> {

}
