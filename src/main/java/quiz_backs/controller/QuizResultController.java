package quiz_backs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import quiz_backs.dto.QuizResultDto;
import quiz_backs.service.QuizResultService;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class QuizResultController {
    @Autowired
    private QuizResultService quizResultService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/results")
    public ResponseEntity<?> findAll() {
        List<QuizResultDto> results = quizResultService.getResults();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/results/{user_id}")
    public ResponseEntity<?> findResultsByUser(@PathVariable Long user_id) {
        List<QuizResultDto> resultsByUserId = quizResultService.getResultsByUserId(user_id);
        return new ResponseEntity<>(resultsByUserId, HttpStatus.OK);
    }
}
