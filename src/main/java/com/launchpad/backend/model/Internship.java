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
@Document(collection = "internships")
public class Internship {

    @Id
    private String id;

    private String employerId;

    private String role;
    private String category;
    private String location;
    private boolean remote;
    private int stipend;
    private String duration;
    private int openings;
    private String tagColor;
    private String description;
    private String requirements;
    private String responsibilities;

    private List<String> skills;
    private List<String> perks;
    private List<String> tags;

    private String status; // active, paused, closed

    private int applicants = 0;
    private int shortlisted = 0;
    private int hired = 0;
    private int views = 0;

    private String postedDate;
}