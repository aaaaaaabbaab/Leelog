package com.example.leelog.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
public class PostEdit {
    @NotBlank(message = "타이틀을 입력하세요.")   //gradle 디펜던시에  implementation('org.springframework.boot:spring-boot-starter-validation') 추가
    private String title;
    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder
    public PostEdit(String title, String content) {
        this.title = title;
        this.content = content;
    }



}
