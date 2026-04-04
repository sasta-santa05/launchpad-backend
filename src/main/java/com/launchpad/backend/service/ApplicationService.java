package com.launchpad.backend.service;

import com.launchpad.backend.model.Application;
import com.launchpad.backend.model.Internship;
import com.launchpad.backend.repository.ApplicationRepository;
import com.launchpad.backend.repository.InternshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final InternshipRepository internshipRepository;

    public Application apply(String studentId, String internshipId) {
        // Check already applied
        List<Application> existing = applicationRepository
                .findByStudentIdAndInternshipId(studentId, internshipId);
        if (!existing.isEmpty()) {
            throw new RuntimeException("Already applied to this internship");
        }

        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        Application application = new Application();
        application.setStudentId(studentId);
        application.setInternshipId(internshipId);
        application.setEmployerId(internship.getEmployerId());
        application.setInternshipRole(internship.getRole());
        application.setStatus("applied");
        application.setAppliedDate(LocalDate.now().toString());

        // Increment applicant count
        internship.setApplicants(internship.getApplicants() + 1);
        internshipRepository.save(internship);

        return applicationRepository.save(application);
    }

    public List<Application> getStudentApplications(String studentId) {
        return applicationRepository.findByStudentId(studentId);
    }

    public List<Application> getInternshipApplicants(String internshipId) {
        return applicationRepository.findByInternshipId(internshipId);
    }

    public List<Application> getEmployerApplicants(String employerId) {
        return applicationRepository.findByEmployerId(employerId);
    }

    public Application updateStatus(String applicationId, String status, String employerId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }

        String oldStatus = application.getStatus();
        application.setStatus(status);

        // Update internship counts
        Internship internship = internshipRepository.findById(application.getInternshipId())
                .orElse(null);

        if (internship != null) {
            if ("shortlisted".equals(status) && !"shortlisted".equals(oldStatus)) {
                internship.setShortlisted(internship.getShortlisted() + 1);
            }
            if ("hired".equals(status) && !"hired".equals(oldStatus)) {
                internship.setHired(internship.getHired() + 1);
            }
            internshipRepository.save(internship);
        }

        return applicationRepository.save(application);
    }

    public void withdraw(String applicationId, String studentId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!application.getStudentId().equals(studentId)) {
            throw new RuntimeException("Unauthorized");
        }

        applicationRepository.deleteById(applicationId);
    }
}
