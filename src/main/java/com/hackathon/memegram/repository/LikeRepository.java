package com.hackathon.memegram.repository;

import com.hackathon.memegram.entity.Like;
import org.springframework.data.repository.CrudRepository;

public interface LikeRepository extends CrudRepository<Like, String> {
    Like findByUserId(String userId);
}
