package quiz_backs.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import quiz_backs.dto.UserDto;
import quiz_backs.model.User;
import quiz_backs.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user")
    public ResponseEntity<?> findall() {
        List<User> allUser = userService.getAllUser();

        if (allUser == null || allUser.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(allUser, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        boolean usernameExists = userService.usernameExists(userDto.getUsername());
        if (usernameExists) return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created");
    }
}