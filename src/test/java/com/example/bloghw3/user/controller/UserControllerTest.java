package com.example.bloghw3.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.example.bloghw3.UserSetup;
import com.example.bloghw3.global.BaseResponseDTO;
import com.example.bloghw3.user.dto.UserRequestDTO;
import com.example.bloghw3.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserSetup userSetup;

    @Nested
    @DisplayName("회원가입에 성공")
    class SignUp_O{
        @Test
        @DisplayName("대소문자, 숫자, 특수문자, 길이 충족")
        void signUp_O_1() throws Exception{
            UserRequestDTO userRequestDTO = new UserRequestDTO("username1","Password1!");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("회원가입 성공", 201);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value(expectedResponse.getMsg()))
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("소문자, 숫자, 특수문자, 길이 충족")
        void signUp_O_2() throws Exception{
            UserRequestDTO userRequestDTO = new UserRequestDTO("username1","password1!");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("회원가입 성공", 201);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value(expectedResponse.getMsg()))
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("대문자, 숫자, 특수문자, 길이 충족")
        void signUp_O_3() throws Exception{
            UserRequestDTO userRequestDTO = new UserRequestDTO("username1","PASSWORD1!");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("회원가입 성공", 201);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").value(expectedResponse.getMsg()))
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }
    }

    @Nested
    @DisplayName("회원가입에 실패")
    class SignUp_X {
        @Test
        @DisplayName("중복된 Username 존재")
        void signUp_X_1() throws Exception{
            UserRequestDTO userRequestDTO = new UserRequestDTO("username1","Password1!");
            User existingUser = User.createUser(userRequestDTO.getUsername(), userRequestDTO.getPassword());
            userSetup.saveUser(existingUser);
            UserRequestDTO newUserRequestDTO = new UserRequestDTO("username1","DiffPassword1!");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(newUserRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("userName은 최소 4자 이상")
        void signUp_X_2() throws Exception{

            UserRequestDTO userRequestDTO = new UserRequestDTO("asd","Password1!");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("userName은 최대 10자 이하")
        void signUp_X_3() throws Exception{

            UserRequestDTO userRequestDTO = new UserRequestDTO("qwerasdfzxcv","Password!1P#");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("userName은 소문자, 숫자로 구성")
        void signUp_X_4() throws Exception {

            UserRequestDTO userRequestDTO = new UserRequestDTO("abAb!@12","Password1!");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("password는 최소 8자 이상")
        void signUp_X_5() throws Exception{

            UserRequestDTO userRequestDTO = new UserRequestDTO("username","Pas12!@");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("password는 최대 15자 이하")
        void signUp_X_6() throws Exception{

            UserRequestDTO userRequestDTO = new UserRequestDTO("username","Pas123!@#PASD1!2");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("password는 대문자, 소문자, 숫자, 특수문자로 구성 - 특수문자 누락")
        void signUp_X_7() throws Exception {

            UserRequestDTO userRequestDTO = new UserRequestDTO("username","PassWord12");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("password는 대문자, 소문자, 숫자, 특수문자로 구성 - 특수문자 누락")
        void signUp_X_8() throws Exception {

            UserRequestDTO userRequestDTO = new UserRequestDTO("username","PassWord12");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }

        @Test
        @DisplayName("password는 대문자, 소문자, 숫자, 특수문자로 구성 - 숫자 누락")
        void signUp_X_9() throws Exception {

            UserRequestDTO userRequestDTO = new UserRequestDTO("username","PassWord!@#");
            BaseResponseDTO expectedResponse = new BaseResponseDTO("false",400);
            String request = objectMapper.writeValueAsString(userRequestDTO);

            mockMvc.perform(post("/api/signup")
                    .content(request)
                    .contentType(MediaType.APPLICATION_JSON))

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(expectedResponse.getStatus()))
                .andDo(MockMvcResultHandlers.print());
        }
    }
}
