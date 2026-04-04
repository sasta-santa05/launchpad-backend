package com.launchpad.backend.service;

import com.launchpad.backend.model.Message;
import com.launchpad.backend.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> getThreadsForEmployer(String employerId) {
        return messageRepository.findByEmployerId(employerId);
    }

    public List<Message> getThreadsForStudent(String studentId) {
        return messageRepository.findByStudentId(studentId);
    }

    public Message sendMessage(String employerId, String studentId,
                               String text, String from,
                               String internshipId, String internshipRole) {

        Message thread = messageRepository
                .findByEmployerIdAndStudentId(employerId, studentId)
                .orElse(null);

        Message.MessageItem newMsg = new Message.MessageItem(
                UUID.randomUUID().toString(),
                from,
                text,
                Instant.now().toString()
        );

        if (thread == null) {
            thread = new Message();
            thread.setEmployerId(employerId);
            thread.setStudentId(studentId);
            thread.setInternshipId(internshipId);
            thread.setInternshipRole(internshipRole);
            thread.setMessages(new ArrayList<>());
            thread.setUnread(0);
        }

        thread.getMessages().add(newMsg);
        thread.setLastMessage(text);
        thread.setLastTime(Instant.now().toString());

        if ("employer".equals(from)) {
            thread.setUnread(thread.getUnread() + 1);
        }

        return messageRepository.save(thread);
    }

    public Message markRead(String threadId) {
        Message thread = messageRepository.findById(threadId)
                .orElseThrow(() -> new RuntimeException("Thread not found"));
        thread.setUnread(0);
        return messageRepository.save(thread);
    }
}
