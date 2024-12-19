package quiz_backs.service;

import quiz_backs.dto.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}