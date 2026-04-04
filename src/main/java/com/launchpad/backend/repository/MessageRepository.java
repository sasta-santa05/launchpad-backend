package com.launchpad.backend.repository;

import com.launchpad.backend.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByEmployerId(String employerId);
    List<Message> findByStudentId(String studentId);
    Optional<Message> findByEmployerIdAndStudentId(String employerId, String studentId);
}