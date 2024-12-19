package quiz_backs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quiz_backs.model.Topic;
import quiz_backs.repository.TopicRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public Topic createTopic(String topicName) {
        Optional<Topic> existingTopic = topicRepository.findByName(topicName);

        if (existingTopic.isPresent()) {
            return existingTopic.get();
        }
        Topic topic = new Topic();
        topic.setName(topicName);
        return topicRepository.save(topic);
    }

    public Topic updateTopic(Long id, Topic updatedTopic) {
        Topic existingTopic = topicRepository.findById(id).orElse(null);
        if (existingTopic != null) {
            existingTopic.setName(updatedTopic.getName());
            return topicRepository.save(existingTopic);
        }
        return null;
    }
}
