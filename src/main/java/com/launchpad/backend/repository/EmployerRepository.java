package com.launchpad.backend.repository;

import com.launchpad.backend.model.Employer;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface EmployerRepository extends MongoRepository<Employer, String> {
    Optional<Employer> findByUserId(String userId);
    Optional<Employer> findByEmail(String email);
}