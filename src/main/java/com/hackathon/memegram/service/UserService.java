package com.hackathon.memegram.service;

import com.hackathon.memegram.dto.UserDto;
import com.hackathon.memegram.dto.UserLogin;
import com.hackathon.memegram.entity.Post;
import com.hackathon.memegram.entity.User;
import com.hackathon.memegram.repository.PostRepository;
import com.hackathon.memegram.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;
    private PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {

        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void requestFollowing(String requestedUserId, String userId) {

        User user = userRepository.findOne(userId);

        controlRequestedFollowersList(user, requestedUserId);
    }

    public void answerFollowRequest(String answeredUserId, String waiterUserId, Boolean accepted) {

        User answeredUser = userRepository.findOne(answeredUserId);

        controlRequestedFollowersList(answeredUser, waiterUserId);

        if (accepted) {
            User waiterUser = userRepository.findOne(waiterUserId);
            followEachOther(answeredUser, waiterUser);
            userRepository.save(answeredUser);
            userRepository.save(waiterUser);
        }
    }

    /**
     * @param answeredUser
     * @param waiterUser   answeredUser accepted waiterUser request
     */
    private void followEachOther(User answeredUser, User waiterUser) {

        answeredUser.getFollowers().add(waiterUser.getId());
        answeredUser.getFollowing().add(waiterUser.getId());

        waiterUser.getFollowers().add(answeredUser.getId());
        waiterUser.getFollowing().add(answeredUser.getId());
    }

    /**
     * @param userId User wanted to follow you
     */
    private void controlRequestedFollowersList(User user, String userId) {
        List<String> requestedFollowers = user.getRequestedFollowers();
        if (requestedFollowers == null) {
            requestedFollowers = new ArrayList<String>();
        } else if (requestedFollowers.contains(userId)) {

            //User canceled his  follow request
            requestedFollowers.remove(userId);
            return;
        }

        requestedFollowers.add(userId);
    }

    public void unfollowUser(String userId, String unfollowerId) {
        User unfollower = userRepository.findOne(unfollowerId);
        User user = userRepository.findOne(userId);

        unfollower.getFollowing().remove(userId);
        user.getFollowers().remove(unfollowerId);

        userRepository.save(unfollower);
        userRepository.save(user);
    }

    public List<Post> getAllPostsByUserId(String userId) {

        User user = userRepository.findOne(userId);

        return user.getUploadedFiles()
                .stream()
                .map(postId -> postRepository.findOne(postId))
                .collect(Collectors.toList());
    }

    public String registerUser(User newUser) {
        User user = userRepository.findByEmail(newUser.getEmail());
        if (user != null) {
            return user.getId();
        } else {
            newUser.setRegDate(new Date());
            newUser = userRepository.save(newUser);
            return newUser.getId();
        }
    }

    public List<Post> getFeed(String userId) {
        /*List<User> users = userRepository.findAllByFollowersContains(userId);
        User user = userRepository.findOne(userId);
        List<Post> posts = new ArrayList<>();
        users.add(user);
        users.forEach(user1 -> {
            if (user1.getUploadedFiles() != null)
                posts.addAll(user1.getUploadedFiles()
                        .stream()
                        .map(fileId -> postRepository.findOne(fileId))
                        .collect(Collectors.toList()));
        });*/

        return postRepository.findAll();
    }

    public User loginUser(UserLogin userLogin) {
        return userRepository.findByUsernameEqualsAndPasswordEquals(userLogin.getUsername(), userLogin.getPassword());
    }

    public User getUserByUserId(String userId) {
        return userRepository.findOne(userId);
    }

    public UserDto getUserFullInfoById(String userId) {

        User user = userRepository.findOne(userId);

        List<Post> list = user
                .getUploadedFiles()
                .stream()
                .map(postId -> postRepository.findOne(postId))
                .collect(Collectors.toList());

        UserDto userDto = new UserDto();
        userDto.setAvatarUrl(user.getAvatarUrl());
        userDto.setBio(user.getBio());
        userDto.setEmail(user.getEmail());
        userDto.setFollowers(user.getFollowers());
        userDto.setFollowing(user.getFollowing());
        userDto.setId(user.getId());
        userDto.setGender(user.getGender());
        userDto.setFullName(user.getFullName());
        userDto.setPassword(user.getPassword());
        userDto.setRegDate(user.getRegDate());
        userDto.setUploadedFiles(list);
        userDto.setUsername(user.getUsername());

        return userDto;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
