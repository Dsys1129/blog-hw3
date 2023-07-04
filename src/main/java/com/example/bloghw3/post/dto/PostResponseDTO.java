package com.example.bloghw3.post.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.bloghw3.comment.dto.CommentResponseDTO;
import com.example.bloghw3.post.entity.Post;

import lombok.Getter;

@Getter
public class PostResponseDTO {

    private final Long postId;

    private final String title;

    private final String username;

    private final String contents;

    private final LocalDateTime createdDate;

    private final List<CommentResponseDTO> commentList;

    public PostResponseDTO(Post post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.username = post.getUser().getUsername();
        this.contents = post.getContents();
        this.createdDate = post.getCreatedDate();
        this.commentList = post.getCommentList().stream()
            .map(CommentResponseDTO::new)
            .collect(Collectors.toList());
    }
}
