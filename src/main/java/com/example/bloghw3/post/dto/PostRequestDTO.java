package com.example.bloghw3.post.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequestDTO {

    @NotBlank(message = "제목은 공백이 불가능 합니다.")
    private final String title;

    private final String contents;

    public PostRequestDTO(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
