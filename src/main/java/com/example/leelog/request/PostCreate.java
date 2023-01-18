package com.example.leelog.request;

import com.example.leelog.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
@Setter
@Getter
public class PostCreate {

    @NotBlank(message = "타이틀을 입력하세요.")   //gradle 디펜던시에  implementation('org.springframework.boot:spring-boot-starter-validation') 추가
    private String title;
    @NotBlank(message = "컨텐츠를 입력해주세요")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate(){
        if(title.contains("바보")){      /////예를 들어 제목에는 "바보"라는 단어가 포함되면안되게 정책이생겼다고 가정
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }

    // 빌더의 장점
    // .가독성에 좋다
    //  값 생성에대한 유연함
    // 필요한 값만 받을 수 있다.  // -->(오버로딩 가능한 조건 찾아보세요)
    // 객체의 불변성


//ROOMBOOK의 @Setter @Getter 사용으로 대체
//    public String getTitle() {
//        return title;
//    }
//
//    public String getContent() {
//        return content;
//    }


//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
}
