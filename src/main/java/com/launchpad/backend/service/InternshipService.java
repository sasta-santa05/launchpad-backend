package com.launchpad.backend.service;

import com.launchpad.backend.model.Internship;
import com.launchpad.backend.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipService {

    private final InternshipRepository internshipRepository;

    public List<Internship> getAllActive() {
        return internshipRepository.findByStatus("active");
    }

    public List<Internship> getByEmployer(String employerId) {
        return internshipRepository.findByEmployerId(employerId);
    }

    public Internship getById(String id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Internship not found"));
    }

    public Internship create(Internship internship, String employerId) {
        internship.setEmployerId(employerId);
        internship.setStatus("active");
        internship.setPostedDate(LocalDate.now().toString());
        return internshipRepository.save(internship);
    }

    public Internship update(String id, Internship updated, String employerId) {
        Internship existing = getById(id);
        if (!existing.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }
        updated.setId(id);
        updated.setEmployerId(employerId);
        return internshipRepository.save(updated);
    }

    public void updateStatus(String id, String status, String employerId) {
        Internship internship = getById(id);
        if (!internship.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }
        internship.setStatus(status);
        internshipRepository.save(internship);
    }

    public void delete(String id, String employerId) {
        Internship internship = getById(id);
        if (!internship.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }
        internshipRepository.deleteById(id);
    }
}
