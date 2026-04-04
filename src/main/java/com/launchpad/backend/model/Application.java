package com.launchpad.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "applications")
public class Application {

    @Id
    private String id;

    private String studentId;
    private String internshipId;
    private String employerId;

    private String internshipRole;
    private String companyName;

    private String status; // applied, shortlisted, interview, hired, rejected

    private String appliedDate;
    private String note;
}