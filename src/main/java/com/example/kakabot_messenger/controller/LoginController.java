package com.example.kakabot_messenger.controller;

import com.example.kakabot_messenger.repository.UserRepository;
import com.example.kakabot_messenger.config.JwtUtil;
import com.example.kakabot_messenger.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RestController
@RequestMapping("/api")
public class LoginController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public LoginController(UserRepository userRepository,
                           BCryptPasswordEncoder encoder,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {

        return userRepository.findByUsername(body.get("username"))
                .filter(u -> encoder.matches(body.get("password"), u.getPassword()))
                .map(u -> ResponseEntity.ok(
                        Map.of("token", jwtUtil.generateToken(u.getUsername()))
                ))
                .orElse(ResponseEntity.status(401)
                        .body(Map.of("error", "Invalid credentials")));
    }
}