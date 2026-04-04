package com.launchpad.backend.repository;

import com.launchpad.backend.model.Internship;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface InternshipRepository extends MongoRepository<Internship, String> {
    List<Internship> findByEmployerId(String employerId);
    List<Internship> findByStatus(String status);
    List<Internship> findByCategory(String category);
    List<Internship> findByRemote(boolean remote);
}