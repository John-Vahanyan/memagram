package com.hackathon.memegram.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {
    @Id
    private String id;

    private String email;
    private String username;
    private String password;
    private String fullName;
    private String avatarUrl;
    private String gender;
    private String bio;
    private Date regDate;

    // Followers users' ids
    private List<String> followers;

    // Following users' ids
    private List<String> following;

    // Users that want to follow you
    private List<String> requestedFollowers;


    // Uploaded files ids
    private List<String> uploadedFiles;


}
