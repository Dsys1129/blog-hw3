package com.example.bloghw3.post.dto;

import java.time.LocalDateTime;

import com.example.bloghw3.post.entity.Post;

import lombok.Getter;

@Getter
public class PostResponseDTO {

    private final Long postId;

    private final String title;

    private final String author;

    private final String contents;

    private final LocalDateTime createdDate;

    public PostResponseDTO(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.author = post.getUser().getUsername();
        this.contents = post.getContents();
        this.createdDate = post.getCreatedDate();
    }
}
