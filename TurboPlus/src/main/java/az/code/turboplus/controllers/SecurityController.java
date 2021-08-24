package az.code.turboplus.controllers;

import az.code.turboplus.dtos.LoginDTO;
import az.code.turboplus.dtos.RegisterDTO;
import az.code.turboplus.exceptions.EmailNotVerified;
import az.code.turboplus.exceptions.LoginException;
import az.code.turboplus.exceptions.ValidationIsIncorrect;
import az.code.turboplus.security.SecurityService;
import az.code.turboplus.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class SecurityController {

    UserService userService;
    SecurityService securityService;

    public SecurityController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleNotFound(LoginException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailNotVerified.class)
    public ResponseEntity<String> handleNotFound(EmailNotVerified e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
  
    @ExceptionHandler(ValidationIsIncorrect.class)
    public ResponseEntity<String> handleNotFound(ValidationIsIncorrect e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        return ResponseEntity.ok(securityService.login(loginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return ResponseEntity.ok(securityService.register(registerDTO));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam String token, @RequestParam String username) {
        return ResponseEntity.ok(securityService.verify(token, username));
    }
}
