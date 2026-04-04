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
@Document(collection = "students")
public class Student {

    @Id
    private String id;

    private String userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String bio;

    // Education
    private String college;
    private String degree;
    private String branch;
    private String year;
    private String cgpa;

    // Skills
    private List<String> skills;

    // Preferences
    private String lookingFor;
    private String availableFrom;
    private List<String> preferredRoles;
    private List<String> preferredLocations;

    // Links
    private String linkedin;
    private String github;
    private String portfolio;
    private String resumeUrl;
}