package com.hackathon.memegram.repository;

import com.hackathon.memegram.entity.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, String> {
    List<Post> findAll();
}
