package quiz_backs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import quiz_backs.model.QuizResult;

import java.util.List;
import java.util.UUID;

public interface QuizResultRepository extends JpaRepository<QuizResult, UUID> {
    List<QuizResult> findByUserId(Long userId);
}