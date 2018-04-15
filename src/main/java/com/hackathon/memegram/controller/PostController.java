package com.hackathon.memegram.controller;

import com.hackathon.memegram.entity.Post;
import com.hackathon.memegram.entity.User;
import com.hackathon.memegram.service.FileService;
import com.hackathon.memegram.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {

    private FileService postService;
    private final UserService userService;

    public PostController(FileService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping(value = "/test")
    public ResponseEntity<String> test() {
        System.out.println("miban");
        return ResponseEntity.ok("OK");
    }

    /**
     * Saving post and returning it's id
     *
     * @param post
     * @return fileId
     */
    @PostMapping(value = "/create")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        String fileId = postService.createNewPost(post);

        return ResponseEntity.ok(fileId);
    }


    /**
     * User pressed like button
     *
     * @param userId
     * @param postId
     * @return
     */
    @GetMapping("/like/post")
    public ResponseEntity like(@RequestParam("userId") String userId,
                               @RequestParam("postId") String postId) {
        postService.likePost(userId, postId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/save/image", method = RequestMethod.POST)
    public ResponseEntity<String> saveImage(@RequestParam MultipartFile file, @RequestParam String email) throws Exception {

        User user = userService.getUserByEmail(email);
        if (user == null){

        }
        return ResponseEntity.ok(postService.saveImageAndGetUrl(file));
    }

    /**
     * Add comment to post
     *
     * @param postId
     * @param userId
     * @param text
     * @return
     */
    @PostMapping(value = "/comment")
    public ResponseEntity commentToPost(@RequestParam("postId") String postId,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("text") String text) {
        postService.comment(postId, userId, text);
        return ResponseEntity.ok().build();
    }

    /**
     * Getting post likes' usernames by postId
     *
     * @param postId
     * @return
     */
    @GetMapping(value = "/likes")
    public ResponseEntity<List<String>> getLikedUsers(@RequestParam("postId") String postId) {
        List<String> users = postService.getLikedUsernames(postId);
        return ResponseEntity.ok(users);
    }

}
