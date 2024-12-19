package quiz_backs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswerDto {
    private UUID quizId;       // ID of the quiz
    private String caseType;   // Case type (kr_uz, uz_kr, writing)
    private String answer;     // User's answer
    private long answerTime;   // Time taken to answer in milliseconds
}
