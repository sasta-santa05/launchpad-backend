package com.launchpad.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String employerId;
    private String studentId;
    private String internshipId;
    private String internshipRole;

    private String lastMessage;
    private String lastTime;
    private int unread = 0;

    private List<MessageItem> messages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageItem {
        private String id;
        private String from; // "employer" or "applicant"
        private String text;
        private String time;
    }
}