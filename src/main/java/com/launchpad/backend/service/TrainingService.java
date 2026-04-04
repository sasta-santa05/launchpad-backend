package com.launchpad.backend.service;

import com.launchpad.backend.model.Training;
import com.launchpad.backend.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainingService {

    private final TrainingRepository trainingRepository;

    public List<Training> getAll() {
        return trainingRepository.findAll();
    }

    public List<Training> getActive() {
        return trainingRepository.findByStatus("active");
    }

    public List<Training> getByEmployer(String employerId) {
        return trainingRepository.findByEmployerId(employerId);
    }

    public Training getById(String id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training not found"));
    }

    public Training create(Training training, String employerId) {
        training.setEmployerId(employerId);
        training.setStatus("active");
        training.setPostedDate(LocalDate.now().toString());
        return trainingRepository.save(training);
    }

    public Training update(String id, Training updated, String employerId) {
        Training existing = getById(id);
        if (!existing.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }
        updated.setId(id);
        updated.setEmployerId(employerId);
        return trainingRepository.save(updated);
    }

    public Training enroll(String id, String studentId) {
        Training training = getById(id);
        training.setEnrolled(training.getEnrolled() + 1);
        training.setRevenue(training.getRevenue() + training.getPrice());
        return trainingRepository.save(training);
    }

    public void updateStatus(String id, String status, String employerId) {
        Training training = getById(id);
        if (!training.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }
        training.setStatus(status);
        trainingRepository.save(training);
    }

    public void delete(String id, String employerId) {
        Training training = getById(id);
        if (!training.getEmployerId().equals(employerId)) {
            throw new RuntimeException("Unauthorized");
        }
        trainingRepository.deleteById(id);
    }
}
