package quiz_backs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResultDto {

    private int correctTests;

    private int totalTests;

    private long takenTime;

    private LocalDateTime dateTime;

    private String username;

    private String topicName;
}
