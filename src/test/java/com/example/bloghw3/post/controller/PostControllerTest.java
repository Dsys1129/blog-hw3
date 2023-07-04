package com.example.bloghw3.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloghw3.PostSetup;
import com.example.bloghw3.UserSetup;
import com.example.bloghw3.jwtutil.JwtProvider;
import com.example.bloghw3.post.dto.PostRequestDTO;
import com.example.bloghw3.post.entity.Post;
import com.example.bloghw3.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostSetup postSetup;

    @Autowired
    UserSetup userSetup;

    @Autowired
    JwtProvider jwtProvider;

    private String mockUserLogin(String userName) {
        User user = User.createUser(userName,"Password1!");
        userSetup.saveUser(user);
        return jwtProvider.createAccessToken(user.getUsername(),user.getUserRole());
    }
    
    private String mockAdminLogin(String userName) {
        User admin = User.createAdmin(userName, "Password1!");
        userSetup.saveUser(admin);
        return jwtProvider.createAccessToken(admin.getUsername(), admin.getUserRole());
    }

    @DisplayName("게시글 작성에 성공한다.")
    @Nested
    class CreatePost_O {
        @DisplayName("유효한 토큰")
        @Test
        void CreatePost_O_1() throws Exception {
            //given
            String username = "username1";
            String accessToken = mockUserLogin(username);
            PostRequestDTO postRequestDTO = new PostRequestDTO("title", "contents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
                    .header(JwtProvider.AUTHORIZATION_HEADER, accessToken))

                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(postRequestDTO.getTitle()))
                .andExpect(jsonPath("$.contents").value(postRequestDTO.getContents()))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.commentList").isEmpty())
                .andDo(print());

        }
    }

    @DisplayName("게시글 작성에 실패한다.")
    @Nested
    class createPost_X {
        @DisplayName("JWT 누락")
        @Test
        void createPost_X_1() throws Exception {
            //given
            mockUserLogin("username1");
            PostRequestDTO postRequestDTO = new PostRequestDTO("title", "contents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request))

                //then
                .andExpect(status().isBadRequest())
                .andDo(print());
        }

        @DisplayName("Invalid Token")
        @Test
        void createPost_X_2() throws Exception {
            //given
            String accessToken = mockUserLogin("username1");

            String InvalidToken = accessToken + "abc";
            PostRequestDTO postRequestDTO = new PostRequestDTO("title", "contents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
                    .header(JwtProvider.AUTHORIZATION_HEADER, InvalidToken))
                //then
                .andExpect(status().isBadRequest())
                .andDo(print());
        }

        @DisplayName("Expired Token")
        @Test
        void createPost_X_3() throws Exception {
            //given
            mockUserLogin("username1");
            String expiredToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE2ODgwMjE5ODksImlhdCI6MTY4ODAxODM4OX0.50O_HuH5Vl_tjMbvmh5h3ztYLtHwK4MJLPXsLwwUd6o";
            PostRequestDTO postRequestDTO = new PostRequestDTO("title", "contents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
                    .header(JwtProvider.AUTHORIZATION_HEADER, expiredToken))
                //then
                .andExpect(status().isBadRequest())
                .andDo(print());
        }

        @DisplayName("회원가입 되지 않은 유저")
        @Test
        void createPost_X_4() throws Exception {
            //given
            String accessToken = mockUserLogin("username1");
            userSetup.clearUsers();
            PostRequestDTO postRequestDTO = new PostRequestDTO("title", "contents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(post("/api/posts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request)
                    .header(JwtProvider.AUTHORIZATION_HEADER, accessToken))
                //then
                .andExpect(status().isBadRequest())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 수정에 성공한다.")
    class ModifyPost_O {
        @DisplayName("유효한 토큰 + 자신의 게시글")
        @Test
        void modifyPost_O_1() throws Exception {
            //given
            String accessToken = mockUserLogin("username1");
            User user = userSetup.findUser("username1");

            Post savedPost = postSetup.savePosts(user);
            PostRequestDTO postRequestDTO = new PostRequestDTO("modifiedTitle", "modifiedContents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(put("/api/posts/{postId}", savedPost.getPostId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtProvider.AUTHORIZATION_HEADER, accessToken)
                    .content(request))

                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(postRequestDTO.getTitle()))
                .andExpect(jsonPath("$.contents").value(postRequestDTO.getContents()))
                .andDo(print());
        }
        @DisplayName("Admin 권한")
        @Test
        void modifyPost_O_2() throws Exception {
            //given
            mockUserLogin("username1");
            User user = userSetup.findUser("username1");
            Post savedPost = postSetup.savePosts(user);

            String adminName = "adminName1";
            String adminToken = mockAdminLogin(adminName);
            PostRequestDTO postRequestDTO = new PostRequestDTO("AdminModifiedTitle", "AdminModifiedContents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(put("/api/posts/{postId}", savedPost.getPostId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtProvider.AUTHORIZATION_HEADER, adminToken)
                    .content(request))

                //then
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(postRequestDTO.getTitle()))
                .andExpect(jsonPath("$.contents").value(postRequestDTO.getContents()))
                .andDo(print());
        }
    }
    @Nested
    @DisplayName("게시글 수정 실패")
    class ModifyPost_X {
        @DisplayName("권한 없음")
        @Test
        void modifyPost_X1() throws Exception {
            //given
            String accessToken = mockUserLogin("username1");
            User diffUser = User.createUser("diffUser", "DiffPass12!@");
            userSetup.saveUser(diffUser);

            Post otherUserPost = postSetup.savePosts(diffUser);
            PostRequestDTO postRequestDTO = new PostRequestDTO("modifiedTitle", "modifiedContents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(put("/api/posts/{postId}", otherUserPost.getPostId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtProvider.AUTHORIZATION_HEADER, accessToken)
                    .content(request))

                //then
                .andExpect(status().isBadRequest())
                .andDo(print());
        }
    }

    @Nested
    @DisplayName("게시글 삭제 성공")
    class DeletePost_O {
        @DisplayName("유효한 토큰 + 자신의 게시글")
        @Test
        void deletePost_O_1() throws Exception {
            //given
            String accessToken = mockUserLogin("username1");
            User user = userSetup.findUser("username1");
            Post savedPost = postSetup.savePosts(user);

            //when
            mockMvc.perform(delete("/api/posts/{postId}", savedPost.getPostId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtProvider.AUTHORIZATION_HEADER, accessToken))

                //then
                .andExpect(status().isOk())
                .andDo(print());

            List<Post> allPosts = postSetup.findAll();
            Assertions.assertThat(allPosts.size()).isEqualTo(0);
        }

        @DisplayName("Admin 권한")
        @Test
        void deletePost_O_2() throws Exception {
            //given
            mockUserLogin("username1");
            User user = userSetup.findUser("username1");
            Post savedPost = postSetup.savePosts(user);
            String adminName = "adminName1";
            String adminToken = mockAdminLogin(adminName);

            //when
            mockMvc.perform(delete("/api/posts/{postId}", savedPost.getPostId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtProvider.AUTHORIZATION_HEADER, adminToken))

                //then
                .andExpect(status().isOk())
                .andDo(print());

            List<Post> allPosts = postSetup.findAll();
            Assertions.assertThat(allPosts.size()).isEqualTo(0);
        }

    }



    @Nested
    @DisplayName("게시글 삭제 실패")
    class DeletePost_X {
        @DisplayName("권한 없음")
        @Test
        void deletePosts_X() throws Exception {
            //given
            String accessToken = mockUserLogin("username1");
            User diffUser = User.createUser("diffUser", "DiffPass12!@");
            userSetup.saveUser(diffUser);

            Post otherUserPost = postSetup.savePosts(diffUser);
            PostRequestDTO postRequestDTO = new PostRequestDTO("modifiedTitle", "modifiedContents");
            String request = objectMapper.writeValueAsString(postRequestDTO);

            //when
            mockMvc.perform(delete("/api/posts/{postId}", otherUserPost.getPostId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(JwtProvider.AUTHORIZATION_HEADER, accessToken)
                    .content(request))

                //then
                .andExpect(status().isBadRequest())
                .andDo(print());
            List<Post> allPosts = postSetup.findAll();
            Assertions.assertThat(allPosts.size()).isGreaterThan(0);
        }
    }
}
