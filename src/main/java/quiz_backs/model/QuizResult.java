package quiz_backs.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private int correctTests;

    @Column(nullable = false)
    private int totalTests;

    @Column(nullable = false)
    private long takenTime;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    // Foreign key to User entity
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Foreign key to Topic entity
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;
}
