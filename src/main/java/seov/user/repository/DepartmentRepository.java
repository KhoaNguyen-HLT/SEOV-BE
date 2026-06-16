package seov.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.user.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
