package com.launchpad.backend.controller;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.model.Internship;
import com.launchpad.backend.repository.UserRepository;
import com.launchpad.backend.repository.EmployerRepository;
import com.launchpad.backend.service.InternshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internships")
@RequiredArgsConstructor
public class InternshipController {

    private final InternshipService internshipService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;

    @GetMapping
    public ResponseEntity<List<Internship>> getAll() {
        return ResponseEntity.ok(internshipService.getAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Internship> getById(@PathVariable String id) {
        return ResponseEntity.ok(internshipService.getById(id));
    }

    @GetMapping("/employer/my")
    public ResponseEntity<List<Internship>> getMyInternships(
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(internshipService.getByEmployer(employerId));
    }

    @PostMapping
    public ResponseEntity<Internship> create(
            @RequestBody Internship internship,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(internshipService.create(internship, employerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Internship> update(
            @PathVariable String id,
            @RequestBody Internship internship,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(internshipService.update(id, internship, employerId));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        internshipService.updateStatus(id, body.get("status"), employerId);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        internshipService.delete(id, employerId);
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
}
