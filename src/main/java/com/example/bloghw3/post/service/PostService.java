package com.example.bloghw3.post.service;

import java.util.List;
import java.util.Map;

import com.example.bloghw3.post.dto.PostRequestDTO;
import com.example.bloghw3.post.dto.PostResponseDTO;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequestDTO, String username);

    List<PostResponseDTO> getPosts();

    PostResponseDTO getPost(Long postId);

    PostResponseDTO modifyPost(Long postId, PostRequestDTO postRequestDTO, String username);

    Map<String,String> deletePost(Long postId, String username);
}
