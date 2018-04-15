package com.hackathon.memegram.repository;

import com.hackathon.memegram.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    User findByEmail(String email);
    List<User> findAllByFollowersContains(String userId);
    User findByUsernameEqualsAndPasswordEquals(String username,String password);
}
