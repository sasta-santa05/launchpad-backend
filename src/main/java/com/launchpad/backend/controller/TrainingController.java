package com.launchpad.backend.controller;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.model.Training;
import com.launchpad.backend.repository.EmployerRepository;
import com.launchpad.backend.repository.StudentRepository;
import com.launchpad.backend.repository.UserRepository;
import com.launchpad.backend.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingService trainingService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final StudentRepository studentRepository;

    @GetMapping
    public ResponseEntity<List<Training>> getAll() {
        return ResponseEntity.ok(trainingService.getActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Training> getById(@PathVariable String id) {
        return ResponseEntity.ok(trainingService.getById(id));
    }

    @GetMapping("/employer/my")
    public ResponseEntity<List<Training>> getMyTrainings(
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(trainingService.getByEmployer(employerId));
    }

    @PostMapping
    public ResponseEntity<Training> create(
            @RequestBody Training training,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(trainingService.create(training, employerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Training> update(
            @PathVariable String id,
            @RequestBody Training training,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(trainingService.update(id, training, employerId));
    }

    @PostMapping("/{id}/enroll")
    public ResponseEntity<Training> enroll(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String studentId = getStudentId(authHeader);
        return ResponseEntity.ok(trainingService.enroll(id, studentId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        trainingService.updateStatus(id, body.get("status"), employerId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        trainingService.delete(id, employerId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    private String getEmployerId(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return employerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employer not found")).getId();
    }

    private String getStudentId(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found")).getId();
    }
}
