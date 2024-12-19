package quiz_backs.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import quiz_backs.model.Quiz;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;


public interface QuizRepository extends JpaRepository<Quiz, UUID> {

    @Query("SELECT q FROM Quiz q WHERE q.topic.id = :topicId ORDER BY FUNCTION('RANDOM')")
    List<Quiz> findRandomQuizzesByTopicId(Long topicId, PageRequest pageRequest);

}