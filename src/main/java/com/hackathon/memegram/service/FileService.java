package com.hackathon.memegram.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.hackathon.memegram.entity.Comment;
import com.hackathon.memegram.entity.Like;
import com.hackathon.memegram.entity.Post;
import com.hackathon.memegram.entity.User;
import com.hackathon.memegram.repository.CommentRepository;
import com.hackathon.memegram.repository.LikeRepository;
import com.hackathon.memegram.repository.PostRepository;
import com.hackathon.memegram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FileService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private LikeRepository likeRepository;
    private CommentRepository commentRepository;
    private Cloudinary cloudinary;

    public FileService(UserRepository userRepository, PostRepository postRepository, LikeRepository likeRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "johnvahanyan",
                "api_key", "962723739486642",
                "api_secret", "TEgvHB2pRDtZGLq2vTA6ZxxHSSo"));
    }

    public String createNewPost(Post post) {
        User user = post.getUploader();
        post = postRepository.save(post);

        List<String> uploadedFiles = user.getUploadedFiles();
        if (uploadedFiles == null) {
            uploadedFiles = new ArrayList<>();
        }
        uploadedFiles.add(post.getId());
        user.setUploadedFiles(uploadedFiles);
        userRepository.save(user);

        return post.getId();
    }

    public void likePost(String userId, String postId) {
        Post post = postRepository.findOne(postId);
        userLikedFile(post, userId);
        postRepository.save(post);
    }

    public String saveImageAndGetUrl(MultipartFile file) throws Exception {
        java.io.File newFile = multipartToFile(file);
        Map map = cloudinary.uploader().upload(newFile, ObjectUtils.emptyMap());
        newFile.delete();
        return map.get("secure_url").toString();
    }

    private java.io.File multipartToFile(MultipartFile file) throws Exception {
        if (file.getSize() < 1048576) {
            java.io.File convFile = new java.io.File(file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } else {
            throw new Exception();
        }
    }

    /**
     * @param userId User pressed like button
     */
    private void userLikedFile(Post post, String userId) {
        List<Like> likes = post.getLikes();
        Like like = likeRepository.findByUserId(userId);
        if (likes == null) {
            likes = new ArrayList<>();
        } else {
            if (like != null) {
                if (likes.contains(like)) {
                    //  User get back his like for this file
                    likes.remove(like);
                    return;
                }
            }
        }
        if (like == null) {
            like = likeRepository.save(new Like(userId));
        }
        likes.add(like);
    }

    public void comment(String postId, String userId, String text) {
        Post post = postRepository.findOne(postId);
        Comment comment = new Comment(text, userId, new Date(), new ArrayList<>());
        comment = commentRepository.save(comment);
        post.getComments().add(comment);
        postRepository.save(post);
    }

    public List<String> getLikedUsernames(String postId) {
        Post post = postRepository.findOne(postId);
        List<Like> likes = post.getLikes();
        return likes
                .stream()
                .map(like -> userRepository.findOne(like.getUserId()).getUsername())
                .collect(Collectors.toList());
    }
}
