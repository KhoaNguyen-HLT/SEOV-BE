package seov.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import seov.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findAllById(Long username);
    Optional<User> findByUsername(String userName);

    @Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
    List<User> getUserListCustom(@Param("username") String username);

    @Query("""
        SELECT u FROM User u
        JOIN FETCH u.roles r
        JOIN FETCH r.permissions
        WHERE u.username = :username
    """)
    Optional<User> findFullUserByUsername(String username);
}
