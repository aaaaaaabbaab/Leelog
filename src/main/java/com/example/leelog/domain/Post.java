package com.example.leelog.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public String getTitle(){
//        return this.title.substring(0,10);
//    }
// ------------>서비스의 정책을 넣지마세요!!! 절때!!

//    public void change(String title, String content){
//        this.title = title;
//        this.content = content;
//    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);

    }

    public void edit(String title, String content) {   //postEditor라는 딱한개의 인자만 받는 메소드로 개선한것
        this.title = title;
        this.content = content;
    }


//    public void edit(PostEditor postEditor) {   //postEditor라는 딱한개의 인자만 받는 메소드로 개선한것
//        title = postEditor.getTitle();
//        content = postEditor.getContent();
//    }


}
