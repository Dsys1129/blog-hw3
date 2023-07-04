package com.example.bloghw3;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bloghw3.post.entity.Post;
import com.example.bloghw3.post.repository.PostRepository;
import com.example.bloghw3.user.entity.User;

@Component
public class PostSetup {

    @Autowired
    private PostRepository postRepository;

    public void clearPosts() {
        postRepository.deleteAll();
    }

    public Post savePosts(User user) {
        Post post = Post.builder()
            .title("title1")
            .contents("contents")
            .user(user)
            .build();
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    public List<Post> findAll(){
        return postRepository.findAll();
    }
}
