package com.hackathon.memegram.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Comment {

    @Id
    private String id;

    private String text;
    private String userId;
    private Date createDate;

    private List<Like> likes;

    public Comment(String text, String userId, Date createDate, List<Like> likes) {
        this.text = text;
        this.userId = userId;
        this.createDate = createDate;
        this.likes = likes;
    }
}
