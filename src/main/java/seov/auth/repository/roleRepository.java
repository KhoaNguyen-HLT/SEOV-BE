package seov.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.auth.entity.Role;

public interface roleRepository extends JpaRepository<Role, Long> {

}
