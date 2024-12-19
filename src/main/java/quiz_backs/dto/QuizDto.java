package quiz_backs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private String korean;
    private String uzbek;
    private String caseType; // kr_uz, uz_kr, or writing
    private List<String> options; // Options for MCQ, empty for writing type
}
