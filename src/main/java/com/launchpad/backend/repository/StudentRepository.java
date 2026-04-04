package com.launchpad.backend.repository;

import com.launchpad.backend.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    Optional<Student> findByUserId(String userId);
    Optional<Student> findByEmail(String email);
}