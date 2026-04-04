package com.launchpad.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "employers")
public class Employer {

    @Id
    private String id;

    private String userId;

    private String name;
    private String logo;
    private String logoColor;
    private String industry;
    private String size;
    private String location;
    private String website;
    private String about;
    private String email;
    private String phone;
    private String founded;
    private String tagline;
}