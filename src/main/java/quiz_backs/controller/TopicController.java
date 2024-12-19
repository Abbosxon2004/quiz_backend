package quiz_backs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import quiz_backs.dto.TopicDto;
import quiz_backs.model.Topic;
import quiz_backs.service.TopicService;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/topic")
    public ResponseEntity<?> create(@RequestBody TopicDto topicDto) {
        Topic topic = topicService.createTopic(topicDto.getName());
        return new ResponseEntity<>(topic, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/topic")
    public ResponseEntity<?> findall() {
        List<Topic> allTopics = topicService.getAllTopics();

        if (allTopics == null || allTopics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allTopics, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/topic/{id}")
    public ResponseEntity<?> updateTopic(@PathVariable Long id, @RequestBody Topic updatedTopic) {
        try {
            Topic topic = topicService.updateTopic(id, updatedTopic);
            if (topic != null) {
                return ResponseEntity.ok(topic);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Topic not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the topic");
        }
    }
}