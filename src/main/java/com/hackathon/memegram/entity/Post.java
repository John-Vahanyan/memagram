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
public class Post {

    @Id
    private String id;

    private String url;
    private String description;
    private List<String> tags;
    private String fileType;
    private Date uploadDate;

    // Uploader's userId;
    private User uploader;

    private List<Like> likes;
    private List<Comment> comments;
    private List<String> reportedUsers;

    private String verificationStatus;


}
