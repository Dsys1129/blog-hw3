package com.example.bloghw3.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDTO {

    private Long postId;
    private String contents;

    public CommentRequestDTO(Long postId, String contents) {
        this.postId = postId;
        this.contents = contents;
    }
}
