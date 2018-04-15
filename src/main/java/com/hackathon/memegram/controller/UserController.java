package com.hackathon.memegram.controller;

import com.hackathon.memegram.dto.UserDto;
import com.hackathon.memegram.dto.UserLogin;
import com.hackathon.memegram.entity.Post;
import com.hackathon.memegram.entity.User;
import com.hackathon.memegram.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        String id = userService.registerUser(user);
        return ResponseEntity.ok(id);
    }
    @GetMapping(value = "/get/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable("userId")String userId){
        User user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<User> login(@RequestBody UserLogin userLogin) {
        User user = userService.loginUser(userLogin);
        return ResponseEntity.ok(user);
    }

    @GetMapping(value = "/follow/request")
    public ResponseEntity requestFollow(@RequestParam("requestedUserId") String requestedUserId,
                                        @RequestParam("userId") String userId) {

        userService.requestFollowing(requestedUserId, userId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/test")
    public ResponseEntity<String> testPost(@RequestBody String fullName) {
        return ResponseEntity.ok("Barlusik");
    }

    @GetMapping(value = "/answer/follow/request")
    public ResponseEntity answer(@RequestParam("answeredUserId") String answeredUserId,
                                 @RequestParam("waiterUserId") String waiterUserId,
                                 @RequestParam("accepted") Boolean accepted) {

        userService.answerFollowRequest(answeredUserId, waiterUserId, accepted);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/unfollow")
    public ResponseEntity unfollowUser(@RequestParam("userId") String userId,
                                       @RequestParam("unfollowerId") String unfollowerId) {

        userService.unfollowUser(userId, unfollowerId);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/all/posts")
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam("userId") String userId) {
        List<Post> list = userService.getAllPostsByUserId(userId);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<Post>> getNewsFeed(@PathVariable("userId") String userId) {
        List<Post> posts = userService.getFeed(userId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping(value = "/full/info")
    public ResponseEntity<UserDto> getFullInfo(@RequestParam("userId") String  userId){
        UserDto userDto = userService.getUserFullInfoById(userId);
        return ResponseEntity.ok(userDto);
    }

}
