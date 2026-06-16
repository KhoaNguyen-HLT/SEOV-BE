package seov.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.user.entity.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

}
