package com.launchpad.backend.controller;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.model.Student;
import com.launchpad.backend.repository.UserRepository;
import com.launchpad.backend.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @GetMapping("/profile")
    public ResponseEntity<Student> getProfile(
            @RequestHeader("Authorization") String authHeader) {
        String userId = getUserId(authHeader);
        return ResponseEntity.ok(studentService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<Student> updateProfile(
            @RequestBody Student student,
            @RequestHeader("Authorization") String authHeader) {
        String userId = getUserId(authHeader);
        return ResponseEntity.ok(studentService.updateProfile(userId, student));
    }

    private String getUserId(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
    }
}
