package com.launchpad.backend.controller;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.model.Employer;
import com.launchpad.backend.repository.UserRepository;
import com.launchpad.backend.service.EmployerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<Employer> getProfile(
            @RequestHeader("Authorization") String authHeader) {
        String userId = getUserId(authHeader);
        return ResponseEntity.ok(employerService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Employer> updateProfile(
            @RequestBody Employer employer,
            @RequestHeader("Authorization") String authHeader) {
        String userId = getUserId(authHeader);
        return ResponseEntity.ok(employerService.updateProfile(userId, employer));
    }

    private String getUserId(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}
