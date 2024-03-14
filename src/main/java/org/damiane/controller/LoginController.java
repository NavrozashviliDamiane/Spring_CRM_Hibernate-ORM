package org.damiane.controller;


import org.damiane.service.AuthenticateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticateService authenticateService;

    public LoginController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = authenticateService.matchUserCredentials(username, password);

        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}
