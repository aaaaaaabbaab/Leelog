package com.example.leelog.controller;

import com.example.leelog.domain.Post;
import com.example.leelog.repository.PostRepository;
import com.example.leelog.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest                             // @ExtendWith({RestDocumentationExtension.class, SpringExtension.class} 에서 SpringExtension.class역할을함
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.Leelog.com", uriPort = 443)
@AutoConfigureMockMvc
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

//    @BeforeEach
//    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .apply(documentationConfiguration(restDocumentation))
//                .build();
//    }
    ////mockMvc 초기화 메소드이므로 @AutoConfigureMockMvc 어노테이션으로 대체


    @Test
    @DisplayName("글 단건 조회 테스트")
    void test1() throws Exception {

        // given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L)
                        .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry",
                        RequestDocumentation.pathParameters(RequestDocumentation.parameterWithName("postId").description("게시글 ID"))))

                .andDo(document("post-inquiry",
                        responseFields(fieldWithPath("id").description("게시글 ID"),  //PayloadDocumentation.responseFields(PayloadDocumentation.fieldWithPath("id").description("게시글 ID")
                                       fieldWithPath("title").description("제목"),
                                       fieldWithPath("content").description("내용")
                        )));
    }

    @Test
    @DisplayName("글 등록")
    void test2() throws Exception {

        // given
        PostCreate request = PostCreate.builder()
                .title("Leelog제목")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);


        //expected
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/posts/")
                        .contentType(APPLICATION_JSON)   ///////.contentType(APPLICATION_JSON) 해주는것 잊지말것\\\\\\\\\
                        .accept(APPLICATION_JSON)
                        .content(json)                  //////.content(json)  추가하는거 잊지말것\\\\\\\\\\\\
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        requestFields(
                                fieldWithPath("title").description("제목").attributes(Attributes.key("constraint").value("좋은제목 입력해주세요")),
                                fieldWithPath("content").description("내용").optional()
                        )
                ));


    }



}
