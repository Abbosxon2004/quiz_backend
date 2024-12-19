package quiz_backs.service;

import lombok.Data;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;

@Data
@Service
public class GenerationService {

    private final WebClient webClient;

    @Value("${gemini.api.key}") // API Key for Gemini
    private String geminiApiKey;

    private final String geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";

    public GenerationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    // Method to generate related words for Uzbek word
    public List<String> generateUzbekOptions(String korean, String uzbek) {
        // Construct the prompt to generate related words (not translations)
        String prompt = "Given the Uzbek word '" + uzbek + "', provide 3 related words in the same category. Example: if the word is 'qalam', provide related words like 'ruchka', 'daftar', and 'kitob'. Return response in this format: {word1, word2, word3}";

        // Create the payload for Gemini model
        Map<String, Object> content = new HashMap<>();
        content.put("text", prompt);

        // Create the request body for Gemini API
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", prompt);
        Map<String, Object> contentWrapper = new HashMap<>();
        contentWrapper.put("parts", List.of(parts));
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(contentWrapper));

        // Make the API call to Gemini
        Mono<Map<String, Object>> response = webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

        // Block to get response synchronously
        Map<String, Object> responseBody = response.block();

        // Extract the generated text from the response
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
        Map<String, Object> contentResponse = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, String>> partsList = (List<Map<String, String>>) contentResponse.get("parts");

        // The 'text' field in 'parts' contains the actual result
        String generatedText = partsList.get(0).get("text");

        // Clean the generated text and split it into a list of related words
        generatedText = generatedText.replaceAll("[{}\\s]", "").split("\n")[0]; // Clean and get the first line
        return List.of(generatedText.split(","));
    }

    // Method to generate related words for Korean word
    public List<String> generateKoreanOptions(String uzbek, String korean) {
        // Construct the prompt to generate related words in Korean
        String prompt = "Given the Korean word '" + korean + "', provide 3 related words in the same category. Example: if the word is '연필', provide related words like '고무', '공책', and '책'. Return response in this format: {word1, word2, word3}";

        // Create the payload for Gemini model
        Map<String, Object> content = new HashMap<>();
        content.put("text", prompt);

        // Create the request body for Gemini API
        Map<String, Object> parts = new HashMap<>();
        parts.put("text", prompt);
        Map<String, Object> contentWrapper = new HashMap<>();
        contentWrapper.put("parts", List.of(parts));
        Map<String, Object> payload = new HashMap<>();
        payload.put("contents", List.of(contentWrapper));

        // Make the API call to Gemini
        Mono<Map<String, Object>> response = webClient.post()
                .uri("/v1beta/models/gemini-1.5-flash:generateContent?key=" + geminiApiKey)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

        // Block to get response synchronously
        Map<String, Object> responseBody = response.block();

        // Extract the generated text from the response
        List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
        Map<String, Object> contentResponse = (Map<String, Object>) candidates.get(0).get("content");
        List<Map<String, String>> partsList = (List<Map<String, String>>) contentResponse.get("parts");

        // The 'text' field in 'parts' contains the actual result
        String generatedText = partsList.get(0).get("text");

        // Clean the generated text and split it into a list of related words
        generatedText = generatedText.replaceAll("[{}\\s]", "").split("\n")[0]; // Clean and get the first line
        return List.of(generatedText.split(","));
    }
}
