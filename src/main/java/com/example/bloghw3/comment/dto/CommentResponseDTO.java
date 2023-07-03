package com.example.bloghw3.comment.dto;


import com.example.bloghw3.comment.entity.Comment;

import lombok.Getter;

@Getter
public class CommentResponseDTO {

    private Long commentId;

    private String username;

    private String contents;

    public CommentResponseDTO(Comment comment) {
        this.commentId = comment.getId();
        this.username = comment.getUser().getUsername();
        this.contents = comment.getContents();
    }
}