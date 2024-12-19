package quiz_backs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quiz_backs.dto.UserDto;
import quiz_backs.model.Role;
import quiz_backs.model.User;
import quiz_backs.repository.RoleRepository;
import quiz_backs.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public boolean usernameExists(String username) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        return existingUser.isPresent();
    }

    public User createUser(UserDto userDto) {
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Optional<Role> defaultRole = roleRepository.findByName("ROLE_USER");
        if (defaultRole.isPresent()) {
            Set<Role> roles = new HashSet<>();
            roles.add(defaultRole.get());
            user.setRoles(roles);
        } else {
            throw new RuntimeException("Default role 'ROLE_USER' not found in the database.");
        }

        return userRepository.save(user);
    }
}
