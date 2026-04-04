package com.launchpad.backend.repository;

import com.launchpad.backend.model.Training;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TrainingRepository extends MongoRepository<Training, String> {
    List<Training> findByEmployerId(String employerId);
    List<Training> findByStatus(String status);
    List<Training> findByCategory(String category);
}