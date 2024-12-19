package quiz_backs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz_backs.dto.QuizResultDto;
import quiz_backs.mapper.QuizResultMapper;
import quiz_backs.model.QuizResult;
import quiz_backs.repository.QuizResultRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizResultService {
    @Autowired
    private QuizResultRepository quizResultRepository;

    public List<QuizResultDto> getResults() {
        List<QuizResult> quizResults = quizResultRepository.findAll();
        return quizResults.stream()
                .map(QuizResultMapper::toDto)
                .collect(Collectors.toList());
    }


    public List<QuizResultDto> getResultsByUserId(Long userId) {
        List<QuizResult> quizResults = quizResultRepository.findByUserId(userId);

        return quizResults.stream()
                .map(QuizResultMapper::toDto)
                .collect(Collectors.toList());
    }
}
