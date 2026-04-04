package com.launchpad.backend.controller;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.model.Application;
import com.launchpad.backend.repository.EmployerRepository;
import com.launchpad.backend.repository.StudentRepository;
import com.launchpad.backend.repository.UserRepository;
import com.launchpad.backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final EmployerRepository employerRepository;

    // Student: apply to internship
    @PostMapping
    public ResponseEntity<?> apply(
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String studentId = getStudentId(authHeader);
            Application application = applicationService.apply(studentId, body.get("internshipId"));
            return ResponseEntity.ok(application);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Student: get my applications
    @GetMapping("/my")
    public ResponseEntity<List<Application>> getMyApplications(
            @RequestHeader("Authorization") String authHeader) {
        String studentId = getStudentId(authHeader);
        return ResponseEntity.ok(applicationService.getStudentApplications(studentId));
    }

    // Employer: get all applicants
    @GetMapping("/employer")
    public ResponseEntity<List<Application>> getEmployerApplicants(
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(applicationService.getEmployerApplicants(employerId));
    }

    // Employer: get applicants for specific internship
    @GetMapping("/internship/{internshipId}")
    public ResponseEntity<List<Application>> getInternshipApplicants(
            @PathVariable String internshipId) {
        return ResponseEntity.ok(applicationService.getInternshipApplicants(internshipId));
    }

    // Employer: move applicant to next stage
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String employerId = getEmployerId(authHeader);
            Application updated = applicationService.updateStatus(id, body.get("status"), employerId);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Student: withdraw application
    @DeleteMapping("/{id}")
    public ResponseEntity<?> withdraw(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String studentId = getStudentId(authHeader);
        applicationService.withdraw(id, studentId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    private String getStudentId(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student not found")).getId();
    }

    private String getEmployerId(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();
        return employerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employer not found")).getId();
    }
}
