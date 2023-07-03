package com.example.bloghw3.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bloghw3.comment.dto.CommentRequestDTO;
import com.example.bloghw3.comment.dto.CommentResponseDTO;
import com.example.bloghw3.comment.service.CommentService;
import com.example.bloghw3.global.BaseResponseDTO;
import com.example.bloghw3.jwtutil.LoginUser;
import com.example.bloghw3.jwtutil.UserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentRequestDTO commentRequestDTO, @LoginUser UserDetails userDetails) {
        CommentResponseDTO response = commentService.createComment(commentRequestDTO, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> modifyComment(@PathVariable Long commentId, @RequestBody CommentRequestDTO commentRequestDTO,
        @LoginUser UserDetails userDetails) {
        CommentResponseDTO response = commentService.modifyComment(commentId, commentRequestDTO, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<BaseResponseDTO> deleteComment(@PathVariable Long commentId, @LoginUser UserDetails userDetails) {
        BaseResponseDTO response = commentService.deleteComment(commentId, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
