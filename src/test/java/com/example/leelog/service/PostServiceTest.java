package com.example.leelog.service;

import com.example.leelog.domain.Post;
import com.example.leelog.exception.PostNotFound;
import com.example.leelog.repository.PostRepository;
import com.example.leelog.request.PostCreate;
import com.example.leelog.request.PostEdit;
import com.example.leelog.request.PostSearch;
import com.example.leelog.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){       //각 테스트 메서드들의 영향이 안받게
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
//        Assertions.assertEquals("1L",postRepository.count());
        //-->alt + enter(static메소드로 만드는 단축키)
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // 클라이언트 요구사항
        // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
        // (이런 처리는 클라이언트에서 하는게 좋음)
        // entity(Post클래스파일)에서 --> getTitle() 직접작성
                //-->이 요구사항정책과 이후의 바뀌는 요구사항의정책이 충돌할수가있음


        // when
//        Post post = postService.get(requestPost.getId());
        //응답클래스 분리로인해
        PostResponse response = postService.get(requestPost.getId());

        // then
//        Assertions.assertNotNull(post);
        ////-->alt + enter(static메소드로 만드는 단축키)
//        assertNotNull(post);
//        assertEquals(1L, postRepository.count());
//        assertEquals("foo", post.getTitle());
//        assertEquals("bar", post.getContent());

        //응답클래스 분리로인해
        //        Assertions.assertNotNull(post);

        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());

    }


//    @Test
//    @DisplayName("글 1페이지 조회")
//    void test3(){
        // given
//        Post requestPost1 = Post.builder()
//                .title("foo1")
//                .content("bar2")
//                .build();
//        postRepository.save(requestPost1);
//
//        Post requestPost2 = Post.builder()
//                .title("foo2")
//                .content("bar2")
//                .build();
//        postRepository.save(requestPost2);

//        postRepository.saveAll(List.of(
//                Post.builder()
//                .title("foo1")
//                .content("bar2")
//                .build(),
//                Post.builder()
//                .title("foo2")
//                .content("bar2")
//                .build()
//
//        ));

        // when
//        Post post = postService.get(requestPost.getId());
        //응답클래스 분리로인해
//        List<PostResponse> posts = postService.getList();

        // then
//        Assertions.assertNotNull(post);
        ////-->alt + enter(static메소드로 만드는 단축키)
//        assertNotNull(post);
//        assertEquals(1L, postRepository.count());
//        assertEquals("foo", post.getTitle());
//        assertEquals("bar", post.getContent());

        //응답클래스 분리로인해
        //        Assertions.assertNotNull(post);
//        assertEquals(2L, posts.size());
//    }

    /////////  -->페이징처리
    @Test
    @DisplayName("글 1페이지 조회")
    void test3(){
        // given
//        for(int i=0; i<30; i++){
//
//        }

        List<Post> requestPosts = IntStream.range(0, 20)
                        .mapToObj(i->{
                            return Post.builder()
                                    .title("이종욱 제목 " + i)
                                    .content("반포자이 " + i)
                                    .build();
                        })
                        .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // SQL -> select, limit, offset         /////

        //        Pageable pageable = PageRequest.of(0,5, Sort.by(Sort.Direction.DESC,"id"));
        //ALT + ENTER -> Sort.Direction.DESC를 static import로 만들어줌 ->
//        Pageable pageable = PageRequest.of(0,5, Sort.by(DESC,"id"));
//        Pageable pageRequest = PageRequest.of(0,5);
        PostSearch postSearch = PostSearch.builder()
                .page(1)
//                .size(10)
                .build();




        // when
//        Post post = postService.get(requestPost.getId());
        //응답클래스 분리로인해
//        List<PostResponse> posts = postService.getList(pageable);
        List<PostResponse> posts = postService.getList(postSearch);



        // then
//        Assertions.assertNotNull(post);
        ////-->alt + enter(static메소드로 만드는 단축키)
//        assertNotNull(post);
//        assertEquals(1L, postRepository.count());
//        assertEquals("foo", post.getTitle());
//        assertEquals("bar", post.getContent());

        //응답클래스 분리로인해
        //        Assertions.assertNotNull(post);
        assertEquals(10L, posts.size());
        assertEquals("이종욱 제목 19", posts.get(0).getTitle());

    }

    @Test
    @DisplayName("글 제목 수정")
    void test4(){
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

        // when
        postService.edit(post.getId(),postEdit);


        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("글이 존재하지 않습니다 id=" + post.getId()));
        //        Assertions.assertNotNull(post);

        assertEquals("이종욱수정", changedPost.getTitle());
    }


    @Test
    @DisplayName("글 내용 수정")
    void test5(){
        // given

        Post post = Post.builder()
                .title("이종욱")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이종욱")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(),postEdit);


        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("글이 존재하지 않습니다 id=" + post.getId()));
        //        Assertions.assertNotNull(post);

        assertEquals("초가집", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6(){
        // given

        Post post = Post.builder()
                .title("이종욱")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());


        // then
        Assertions.assertEquals(0,postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회시 - 존재하지않는 글")
    void test2_1(){
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        //ex) post.getId() // primary_id = 1

        // expected

//       IllegalArgumentException e = Assertions.assertThrows(IllegalArgumentException.class, ()->{
//           postService.get(post.getId() + 1L);
//        });
//
//       Assertions.assertEquals("존재하지 않는 글입니다.", e.getMessage());

         Assertions.assertThrows(PostNotFound.class, ()->{
            postService.get(post.getId() + 1L);
         });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test6_1(){
        // given

        Post post = Post.builder()
                .title("이종욱")
                .content("반포자이")
                .build();

        postRepository.save(post);


        // expected
        Assertions.assertThrows(PostNotFound.class, ()->{
            postService.delete(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("글 내용 수정 - 존재 하지 않는 글")
    void test5_1(){
        // given

        Post post = Post.builder()
                .title("이종욱")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("이종욱")
                .content("초가집")
                .build();

        Assertions.assertThrows(PostNotFound.class, ()->{
            postService.edit(post.getId() + 1L,postEdit);
        });
    }


}