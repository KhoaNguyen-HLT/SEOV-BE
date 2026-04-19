package seov.user.repository;

import seov.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
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
