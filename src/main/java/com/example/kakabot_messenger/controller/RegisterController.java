package com.example.kakabot_messenger.controller;

import com.example.kakabot_messenger.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        authService.register(body.get("username"), body.get("password"));
        return ResponseEntity.ok(Map.of("message", "Registered successfully"));
    }
}
