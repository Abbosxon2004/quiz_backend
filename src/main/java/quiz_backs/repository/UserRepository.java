package quiz_backs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quiz_backs.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
}