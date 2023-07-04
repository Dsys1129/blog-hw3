package com.example.bloghw3.post.service;

import java.util.List;

import com.example.bloghw3.global.BaseResponseDTO;
import com.example.bloghw3.jwtutil.UserDetails;
import com.example.bloghw3.post.dto.PostRequestDTO;
import com.example.bloghw3.post.dto.PostResponseDTO;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequestDTO, UserDetails userDetails);

    List<PostResponseDTO> getPosts();

    PostResponseDTO getPost(Long postId);

    PostResponseDTO modifyPost(Long postId, PostRequestDTO postRequestDTO, UserDetails userDetails);

    BaseResponseDTO deletePost(Long postId, UserDetails userDetails);
}
