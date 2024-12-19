package quiz_backs.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDto {
    private String fullName;
    private String username;
    private String password;
}
