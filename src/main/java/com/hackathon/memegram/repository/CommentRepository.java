package com.hackathon.memegram.repository;

import com.hackathon.memegram.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment,String> {
}
