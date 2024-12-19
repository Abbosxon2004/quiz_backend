package quiz_backs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quiz_backs.model.Topic;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<Topic> findByName(String name);
}