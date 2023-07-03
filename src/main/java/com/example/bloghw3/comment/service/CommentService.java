package com.example.bloghw3.comment.service;



import com.example.bloghw3.comment.dto.CommentRequestDTO;
import com.example.bloghw3.comment.dto.CommentResponseDTO;
import com.example.bloghw3.global.BaseResponseDTO;
import com.example.bloghw3.jwtutil.UserDetails;

public interface CommentService {

    CommentResponseDTO createComment(CommentRequestDTO commentRequestDTO, UserDetails userDetails);

    CommentResponseDTO modifyComment(Long commentId, CommentRequestDTO commentRequestDTO, UserDetails userDetails);

    BaseResponseDTO deleteComment(Long commentId, UserDetails userDetails);

}
