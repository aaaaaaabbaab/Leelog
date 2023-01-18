package com.example.leelog.controller;

import com.example.leelog.domain.Post;
import com.example.leelog.repository.PostRepository;
import com.example.leelog.request.PostCreate;
import com.example.leelog.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){       //각 테스트 메서드들의 영향이 안받게
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 HELLO WORLD를 출력한다.")           //application / json
    void test() throws Exception{
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        // 글 제목
        // 글 내용
        // 사용자
            // id
            // name
            // level

        //JOSON형태로 받는것의 이점
        /***
         *{
         *     "title": "xxx"
         *     "content": "xxx,
         *     "user":{
         *         "id": "xxx",
         *         "name": "xxx",
         *         "level": "xxx";
         *     }
         *}
         * */



        //expected
//        mockMvc.perform(MockMvcRequestBuilders.post("/posts").contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("title","글 제목입니다.")
//                        .param("content","글 내용입니다.")
//                )
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("HELLO WORLD"))
//                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post("/posts").contentType(APPLICATION_JSON)
                        .content("{\"title\":  \"제목입니다.\", \"content\":  \"내용입니다.\"}")
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string("{}"))   //원래 HELLO WORLD였으나 debug시 오류메시지 띄워줄떄 BindingResult 추가하는과정에서 PostController에 초기화값을 {}해놔서 수정
                .andExpect(MockMvcResultMatchers.content().string(""))   //PostController의 Post메소드를 void로 바꾸면서 수정
                .andDo(MockMvcResultHandlers.print());
        // db -> post 1개 등록
    }



    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception{

        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

//        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);



        // expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts").contentType(APPLICATION_JSON)

                                // {"tile": ""}
//                        .content("{\"title\":  \"\", \"content\":  \"내용입니다.\"}")
                                //{"title": null}
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect((ResultMatcher) jsonPath("$.code").value("400"))
//                .andExpect((ResultMatcher) jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.validation.title").value("타이틀을 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test3() throws Exception{

        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);


        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts").contentType(APPLICATION_JSON)

                                // {"tile": ""}
//                        .content("{\"title\":  \"\", \"content\":  \"내용입니다.\"}")
                                //{"title": null}
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andDo(MockMvcResultHandlers.print());

        // then
        Assertions.assertEquals(1L, postRepository.count());
        //위의 void clean()를 만들어두지않으면
        // test3의 개별 메소드를 돌리는게 아니고 전체로 돌릴때는 test메소드에서 이미 db1개를 저장하기때문에 여기서는 2L로 테스트
        // db-> post2개 등록

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        // expected
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}",post.getId()).contentType(APPLICATION_JSON)

                                // {"tile": ""}
//                        .content("{\"title\":  \"\", \"content\":  \"내용입니다.\"}")
                                //{"title": null}
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());

    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void test5() throws Exception {
        // given
//        Post post1 = Post.builder()
//                .title("title_1")
//                .content("content_1")
//                .build();
//        postRepository.save(post1);
//
//        Post post2 = Post.builder()
//                .title("title_2")
//                .content("content_2")
//                .build();
//        postRepository.save(post2);


        // expected
        //단건조회시(test4)
        /**
         * {id: ..., title: ...}
         */
        // 여러건조회시(test5)
        /**
         * [{},{}]
         */
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
//                        .contentType(APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(post1.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("title_1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("content_1"))
//                .andDo(MockMvcResultHandlers.print());


        // given
//        Post post1 = postRepository.save(Post.builder()
//                .title("title_1")
//                .content("content_1")
//                .build());
//
//
//        Post post2 = postRepository.save(Post.builder()
//                .title("title_2")
//                .content("content_2")
//                .build());

        // expected
        //단건조회시(test4)
        /**
         * {id: ..., title: ...}
         */
        // 여러건조회시(test5)
        /**
         * [{},{}]
         */
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
//                        .contentType(APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(post1.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("title_1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("content_1"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(post2.getId()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].title").value("title_2"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].content").value("content_2"))
//                .andDo(MockMvcResultHandlers.print());
//    }


//    @Test
//    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
//    void test5() throws Exception {
//        // given
//        List<Post> requestPosts = IntStream.range(1, 31)
//                .mapToObj(i->{
//                    return Post.builder()
//                            .title("이종욱 제목 " + i)
//                            .content("반포자이 " + i)
//                            .build();
//                })
//                .collect(Collectors.toList());
//        postRepository.saveAll(requestPosts);
//
//        // expected
//        //단건조회시(test4)
//        /**
//         * {id: ..., title: ...}
//         */
//        // 여러건조회시(test5)
//        /**
//         * [{},{}]
//         */
////        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
////                        .contentType(APPLICATION_JSON))
////                .andExpect(MockMvcResultMatchers.status().isOk())
////                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(post1.getId()))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("title_1"))
////                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].content").value("content_1"))
////                .andDo(MockMvcResultHandlers.print());
//
//
//
//        // expected
//        //단건조회시(test4)
//        /**
//         * {id: ..., title: ...}
//         */
//        // 여러건조회시(test5)
//        /**
//         * [{},{}]
//         */
//        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&sort=id,desc")
//                        .contentType(APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(5)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(30))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("이종욱 제목 30"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("반포자이 30"))
//                .andDo(MockMvcResultHandlers.print());
//    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test5_1() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i->{
                    return Post.builder()
                            .title("이종욱 제목 " + i)
                            .content("반포자이 " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // expected
        //단건조회시(test4)
        /**
         * {id: ..., title: ...}
         */
        // 여러건조회시(test5)
        /**
         * [{},{}]
         */

        mockMvc.perform(MockMvcRequestBuilders.get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(10)))
//                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(30))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("이종욱 제목 19"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].content").value("반포자이 19"))
                .andDo(MockMvcResultHandlers.print());
    }



    @Test
    @DisplayName("글 제목 수정")
    void test6() throws Exception {
        // given

        Post post = Post.builder()
                .title("이종욱")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이종욱수정")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", post.getId())        //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글  삭제")
    void test7() throws Exception {
        // given

        Post post = Post.builder()
                .title("이종욱")
                .content("반포자이")
                .build();

        postRepository.save(post);


        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())        //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())

                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception{

//        Post post = Post.builder()
//                .title("이종욱")
//                .content("반포자이")
//                .build();
//
//        postRepository.save(post);

        // expected

//    mockMvc.perform(delete("/posts/{postId}",post.getId() + 1L)                   ///// (한건저장후 그이후를 조회해서 내가원하는결과인 404오류나게하는것도 가능)
     mockMvc.perform(delete("/posts/{postId}", 1L)        //// (아예 저장안하고 조회해서 내가원하는 결과인 404오류나는지 테스트하는것)
            .contentType(APPLICATION_JSON)
                )
                        .andExpect(MockMvcResultMatchers.status().isNotFound())

            .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception{
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("이종욱수정")
                .content("반포자이")
                .build();

        // expected

        mockMvc.perform(MockMvcRequestBuilders.patch("/posts/{postId}", 1L)        //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit))
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void tes11() throws Exception{

        // given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다..")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);


        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                                .contentType(APPLICATION_JSON)

                                // {"tile": ""}
//                        .content("{\"title\":  \"\", \"content\":  \"내용입니다.\"}")
                                //{"title": null}
                                .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())

                .andDo(MockMvcResultHandlers.print());


    }


}