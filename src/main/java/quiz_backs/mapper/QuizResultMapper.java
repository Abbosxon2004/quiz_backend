package quiz_backs.mapper;

import quiz_backs.dto.QuizResultDto;
import quiz_backs.model.QuizResult;

public class QuizResultMapper {

    public static QuizResultDto toDto(QuizResult quizResult) {
        return new QuizResultDto(
                quizResult.getCorrectTests(),
                quizResult.getTotalTests(),
                quizResult.getTakenTime(),
                quizResult.getDateTime(),
                quizResult.getUser().getUsername(),
                quizResult.getTopic().getName()
        );
    }
}
