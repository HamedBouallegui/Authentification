package Car.carwash.controller;

import Car.carwash.dto.SignupRequest;
import Car.carwash.dto.LoginRequest;
import Car.carwash.dto.UserResponse;
import Car.carwash.model.User;
import Car.carwash.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> handleSignup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            User user = userService.signup(signupRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new SignupResponse("Account created", user.getId()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> handleLogin(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = userService.authenticate(loginRequest);
            return ResponseEntity.ok(new LoginResponse("Login successful", user.getId(), user.getRole().name()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(ex.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<java.util.List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    static class SignupResponse {
        public final String message;
        public final Long userId;
        SignupResponse(String message, Long userId) {
            this.message = message;
            this.userId = userId;
        }
    }

    static class ErrorResponse {
        public final String error;
        ErrorResponse(String error) {
            this.error = error;
        }
    }

    static class LoginResponse {
        public final String message;
        public final Long userId;
        public final String role;
        LoginResponse(String message, Long userId, String role) {
            this.message = message;
            this.userId = userId;
            this.role = role;
        }
    }
}


