package quiz_backs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import quiz_backs.dto.QuizAnswerDto;
import quiz_backs.dto.QuizBaseDto;
import quiz_backs.dto.QuizDto;
import quiz_backs.dto.QuizResultDto;
import quiz_backs.mapper.QuizResultMapper;
import quiz_backs.model.Quiz;
import quiz_backs.model.QuizResult;
import quiz_backs.model.Topic;
import quiz_backs.model.User;
import quiz_backs.repository.QuizRepository;
import quiz_backs.repository.QuizResultRepository;
import quiz_backs.repository.TopicRepository;
import quiz_backs.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private GenerationService generationService;

    public List<QuizDto> getRandomQuizzesByTopic(Long topicId) {
        PageRequest pageRequest = PageRequest.of(0, 20); // Limit to 20 quizzes
        List<Quiz> quizzes = quizRepository.findRandomQuizzesByTopicId(topicId, pageRequest);

        List<QuizDto> quizDtos = new ArrayList<>();
        Random random = new Random();

        for (Quiz quiz : quizzes) {
            QuizDto quizDto = new QuizDto();
            quizDto.setKorean(quiz.getKorean());
            quizDto.setUzbek(quiz.getUzbek());

            // Randomly select a case for the quiz
            int caseType = random.nextInt(3); // 0, 1, or 2
            switch (caseType) {
                case 0: // Korean to Uzbek
                    quizDto.setCaseType("kr_uz");
                    quizDto.setOptions(randomizeOptions(quiz.getKrToUzOptions()));
                    break;
                case 1: // Uzbek to Korean
                    quizDto.setCaseType("uz_kr");
                    quizDto.setOptions(randomizeOptions(quiz.getUzToKrOptions()));
                    break;
                case 2: // Writing test
                    quizDto.setCaseType("writing");
                    quizDto.setOptions(Collections.emptyList()); // No options for writing
                    break;
            }
            quizDtos.add(quizDto);
        }

        return quizDtos;
    }

    private List<String> randomizeOptions(List<String> options) {
        List<String> randomizedOptions = new ArrayList<>(options);
        Collections.shuffle(randomizedOptions);
        return randomizedOptions;
    }


    public QuizResultDto checkAnswersAndSaveResult(Long topicId, Long userId, List<QuizAnswerDto> answers) {
        // Fetch quizzes for validation
        List<Quiz> quizzes = quizRepository.findAllById(
                answers.stream().map(QuizAnswerDto::getQuizId).toList()
        );

        // Calculate results
        int correctTests = 0;
        for (QuizAnswerDto answer : answers) {
            Optional<Quiz> quizOptional = quizzes.stream()
                    .filter(quiz -> quiz.getId().equals(answer.getQuizId()))
                    .findFirst();

            if (quizOptional.isPresent()) {
                Quiz quiz = quizOptional.get();

                // Validate based on case type
                if (answer.getCaseType().equals("kr_uz") && quiz.getUzbek().equals(answer.getAnswer())) {
                    correctTests++;
                } else if (answer.getCaseType().equals("uz_kr") && quiz.getKorean().equals(answer.getAnswer())) {
                    correctTests++;
                } else if (answer.getCaseType().equals("writing") && quiz.getKorean().equals(answer.getAnswer())) {
                    correctTests++;
                }
            }
        }

        int totalTests = answers.size();
        long takenTime = answers.stream().mapToLong(QuizAnswerDto::getAnswerTime).sum();

        // Save result to DB
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        QuizResult quizResult = new QuizResult();
        quizResult.setCorrectTests(correctTests);
        quizResult.setTotalTests(totalTests);
        quizResult.setTakenTime(takenTime);
        quizResult.setDateTime(LocalDateTime.now());
        quizResult.setUser(user);
        quizResult.setTopic(topic);

        quizResultRepository.save(quizResult);
        return QuizResultMapper.toDto(quizResult);
    }

    public Quiz createQuiz(QuizBaseDto quizBaseDto) {
        // Fetch the topic
        Topic topic = topicRepository.findById(quizBaseDto.getTopicId()).orElse(null);

        // Generate options using Gemini API
        List<String> krToUzOptions = generationService.generateUzbekOptions(quizBaseDto.getKorean(), quizBaseDto.getUzbek());
        List<String> uzToKrOptions = generationService.generateKoreanOptions(quizBaseDto.getUzbek(), quizBaseDto.getKorean());

        // Create the quiz object
        Quiz quiz = new Quiz();
        quiz.setKorean(quizBaseDto.getKorean());
        quiz.setUzbek(quizBaseDto.getUzbek());
        quiz.setKrToUzOptions(krToUzOptions);
        quiz.setUzToKrOptions(uzToKrOptions);
        quiz.setTopic(topic);

        // Save and return the quiz
        return quizRepository.save(quiz);
    }

}
