package com.example.leelog.domain;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PostEditor {

    @NotBlank(message = "타이틀을 입력하세요.")   //gradle 디펜던시에  implementation('org.springframework.boot:spring-boot-starter-validation') 추가
    private final String title;
    @NotBlank(message = "컨텐츠를 입력해주세요")
    private final String content;

    @Builder
    public PostEditor(String title, String content) {

            this.title = title;
            this.content = content;
    }

    

}
