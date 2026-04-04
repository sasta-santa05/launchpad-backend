package com.launchpad.backend.controller;

import com.launchpad.backend.config.JwtUtil;
import com.launchpad.backend.model.Message;
import com.launchpad.backend.repository.EmployerRepository;
import com.launchpad.backend.repository.StudentRepository;
import com.launchpad.backend.repository.UserRepository;
import com.launchpad.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final StudentRepository studentRepository;

    // Employer: get all threads
    @GetMapping("/employer/threads")
    public ResponseEntity<List<Message>> getEmployerThreads(
            @RequestHeader("Authorization") String authHeader) {
        String employerId = getEmployerId(authHeader);
        return ResponseEntity.ok(messageService.getThreadsForEmployer(employerId));
    }

    // Student: get all threads
    @GetMapping("/student/threads")
    public ResponseEntity<List<Message>> getStudentThreads(
            @RequestHeader("Authorization") String authHeader) {
        String studentId = getStudentId(authHeader);
        return ResponseEntity.ok(messageService.getThreadsForStudent(studentId));
    }

    // Send a message
    @PostMapping("/send")
    public ResponseEntity<Message> send(
            @RequestBody Map<String, String> body,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String role  = jwtUtil.extractRole(token);
        String email = jwtUtil.extractEmail(token);
        String userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found")).getId();

        String employerId    = body.get("employerId");
        String studentId     = body.get("studentId");
        String text          = body.get("text");
        String internshipId  = body.get("internshipId");
        String internshipRole = body.get("internshipRole");
        String from          = "employer".equals(role) ? "employer" : "applicant";

        Message message = messageService.sendMessage(
                employerId, studentId, text, from, internshipId, internshipRole);
        return ResponseEntity.ok(message);
    }

    // Mark thread as read
    @PatchMapping("/{threadId}/read")
    public ResponseEntity<Message> markRead(@PathVariable String threadId) {
        return ResponseEntity.ok(messageService.markRead(threadId));
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
