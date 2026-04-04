package com.launchpad.backend.service;

import com.launchpad.backend.model.Student;
import com.launchpad.backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public Student getProfile(String userId) {
        return studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
    }

    public Student updateProfile(String userId, Student updated) {
        Student existing = studentRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Student profile not found"));
        updated.setId(existing.getId());
        updated.setUserId(userId);
        return studentRepository.save(updated);
    }
}
