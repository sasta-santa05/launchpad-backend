package com.launchpad.backend.service;

import com.launchpad.backend.model.Employer;
import com.launchpad.backend.repository.EmployerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployerService {

    private final EmployerRepository employerRepository;

    public Employer getProfile(String userId) {
        return employerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employer profile not found"));
    }

    public Employer updateProfile(String userId, Employer updated) {
        Employer existing = employerRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Employer profile not found"));
        updated.setId(existing.getId());
        updated.setUserId(userId);
        return employerRepository.save(updated);
    }
}
