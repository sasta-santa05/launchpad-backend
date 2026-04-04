package com.launchpad.backend.repository;

import com.launchpad.backend.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByStudentId(String studentId);
    List<Application> findByInternshipId(String internshipId);
    List<Application> findByEmployerId(String employerId);
    List<Application> findByStudentIdAndInternshipId(String studentId, String internshipId);
}