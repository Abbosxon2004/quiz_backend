package quiz_backs.controller;

import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import quiz_backs.config.JwtTokenProvider;
import quiz_backs.dto.QuizAnswerDto;
import quiz_backs.dto.QuizBaseDto;
import quiz_backs.dto.QuizDto;
import quiz_backs.dto.QuizResultDto;
import quiz_backs.model.Quiz;
import quiz_backs.repository.UserRepository;
import quiz_backs.service.QuizService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class QuizController {
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/quizes/{topic_id}")
    public ResponseEntity<?> getQuizesByTopicId(@PathVariable Long topic_id) {
        List<QuizDto> randomQuizzesByTopic = quizService.getRandomQuizzesByTopic(topic_id);
        return ResponseEntity.ok(randomQuizzesByTopic);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/quizes/{topic_id}")
    public ResponseEntity<?> checkAnswersAndSaveResult(
            @PathVariable Long topic_id,
            @RequestBody List<QuizAnswerDto> answers,
            Principal principal) {

        Long userId = userRepository.findByUsername(principal.getName()).get().getId();
        // Call service method
        QuizResultDto result = quizService.checkAnswersAndSaveResult(topic_id, userId, answers);

        // Return result
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/quizes/create")
    public ResponseEntity<?> createQuiz(@RequestBody QuizBaseDto quizBaseDto) {
        Quiz quiz = quizService.createQuiz(quizBaseDto);
        if (quiz == null) {
            return new ResponseEntity<>("Topic not found", HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(quiz);
    }

}