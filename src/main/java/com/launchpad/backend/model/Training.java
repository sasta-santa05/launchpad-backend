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
@Document(collection = "trainings")
public class Training {

    @Id
    private String id;

    private String employerId;

    private String title;
    private String category;
    private String tagColor;
    private String level;
    private String description;
    private String duration;
    private int price;
    private String status;

    private int enrolled = 0;
    private int chapters = 0;
    private int lessons = 0;
    private double rating = 0.0;
    private int reviews = 0;
    private long revenue = 0;

    private String postedDate;
    private List<String> whatYouLearn;
    private List<CurriculumChapter> curriculum;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CurriculumChapter {
        private String title;
        private int lessons;
    }
}