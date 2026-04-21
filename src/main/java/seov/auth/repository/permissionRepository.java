package seov.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import seov.auth.entity.Permissions;
import seov.auth.entity.Role;

public interface permissionRepository extends JpaRepository<Permissions, Long> {

}
