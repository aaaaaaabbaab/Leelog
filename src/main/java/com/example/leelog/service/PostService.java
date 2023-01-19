package com.example.leelog.service;

import com.example.leelog.domain.Post;
import com.example.leelog.exception.PostNotFound;
import com.example.leelog.repository.PostRepository;
import com.example.leelog.request.PostCreate;
import com.example.leelog.request.PostEdit;
import com.example.leelog.request.PostSearch;
import com.example.leelog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

//    public void write(PostCreate postCreate){
//
//        // postCreate -> Entity
//
////        Post post =  new Post(postCreate.getTitle(), postCreate.getContent());
//         //   ->
//        //Post 클래스의 Post메소드에도 @Build를 달아준후이후
//        // ->
//        Post post =  Post.builder()
//                .title(postCreate.getTitle())
//                .content(postCreate.getContent())
//                .build();
//
//
//        // repository.save(postCreate)
//        postRepository.save(post);
//
//    }


    //// save한거를 Json형식(key:value)로 넘겨줄떄
    // Case1. 저장한 데이터 Entity -> response로 응답하기
//    public Post write(PostCreate postCreate){         ////Post로 return 타입 맞춰주기
//           ////
//        // postCreate -> Entity
//
//        //Post post =  new Post(postCreate.getTitle(), postCreate.getContent());
//        //   ->
//        //Post 클래스의 Post메소드에도 @Build를 달아준후이후
//        // ->
//        Post post =  Post.builder()
//                .title(postCreate.getTitle())
//                .content(postCreate.getContent())
//                .build();
//
//
//        // repository.save(postCreate)
//        return postRepository.save(post);       ////return 추가
//        //////
//    }

    // Case2. 저장한 데이터의  primary_id -> response로 응답하기
    //      Client에서는 수시한 id를 post 글 조회 API를 통해서 글 데이터를 수신받음
//    public Long write(PostCreate postCreate){
//        ////
//        // postCreate -> Entity
//
//        //Post post =  new Post(postCreate.getTitle(), postCreate.getContent());
//        //   ->
//        //Post 클래스의 Post메소드에도 @Build를 달아준후이후
//        // ->
//        Post post =  Post.builder()
//                .title(postCreate.getTitle())
//                .content(postCreate.getContent())
//                .build();
//
//
//        // repository.save(postCreate)
//        postRepository.save(post);
//
//        return post.getId();
//    }


    // Case3. 응답 필요 없음 -> Client에서 모든 POST(글) 데이터 context를 잘 관리함
    public void write(PostCreate postCreate){
        ////
        // postCreate -> Entity

        //Post post =  new Post(postCreate.getTitle(), postCreate.getContent());
        //   ->
        //Post 클래스의 Post메소드에도 @Build를 달아준후이후
        // ->
        Post post =  Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
        System.out.println("hello world1");
        System.out.println("11111111");

        // repository.save(postCreate)
        postRepository.save(post);

    }

//    public Post get(Long id){
//        Post post = postRepository.findById(id)
//                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 글입니다."));
//        return post;
//    }



    // 클라이언트 요구사항
    // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
    // (이런 처리는 클라이언트에서 하는게 좋음)
    // entity(Post클래스파일)에서 --> getTitle() 직접작성
    //-->이 요구사항정책과 이후의 바뀌는 요구사항의정책이 충돌할수가있음
    /// 이 블로그가 잘되서 RSS발행

//    public Post getRss(Long id){
//        Post post = postRepository.findById(id)
//                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 글입니다."));
//        return post;
//    }

    public PostResponse get(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(()->new PostNotFound());

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

//    public List<PostResponse> getList() {
//        return postRepository.findAll().stream()
//                .map(post ->
//                        {
//                    return PostResponse.builder()
//                            .id(post.getId())
//                            .title(post.getTitle())
//                            .content(post.getContent())
//                            .build();

//                }
//                                new PostResponse(post)
//                )
//                .collect(Collectors.toList());
//    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> DB글 모두 조회하는 경우 -> DB가 뻗을 수 있다.
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽비용 등이 많이 발생할 수 있다.

    ///////////// --> 페이징처리후

//    public List<PostResponse> getList(int page) {
    public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id") );

//        return postRepository.findAll(pageable).stream()            //findAll(pageable) 넣는 것 주의할것
        //PostRepositoryImpl 추가한 이후 ---
        return postRepository.getList(postSearch).stream()

                .map(post ->
//                        {
//                    return PostResponse.builder()
//                            .id(post.getId())
//                            .title(post.getTitle())
//                            .content(post.getContent())
//                            .build();

//                }
                                new PostResponse(post)
                )
                .collect(Collectors.toList());
    }

//    @Transactional                                      // postRepository.save(post); 역할
////    public void edit(Long id, PostEdit postEdit){
//    public PostResponse edit(Long id, PostEdit postEdit){           // \1 return 형태를 void 대신 PostResponse 로 수정 (*클라이언트에서 json 형식으로달라고 요청할경우)
//        Post post = postRepository.findById(id)
//                .orElseThrow(()-> new IllegalArgumentException("존재하지않는 글입니다."));
//        post.change(postEdit.getTitle(),postEdit.getContent());
//        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();
//
//        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
//                                             .content(postEdit.getContent())
//                                             .build();
//
//        post.edit(postEditor);
//
//
//        return new PostResponse(post);                          // \1의 return 값 수정하면서 추가한것
//
//        postRepository.save(post);
//    }


    ////   PostEditor의 개념을 사용하지않고 쓰는거(PostEditor의 개념 이해잘안될때)
    @Transactional                                      // postRepository.save(post); 역할
    public void edit(Long id, PostEdit postEdit){           // \1 return 형태를 void 대신 PostResponse 로 수정 (*클라이언트에서 json 형식으로달라고 요청할경우)
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFound());
//        post.change(postEdit.getTitle(),postEdit.getContent());

        post.edit(postEdit.getTitle(),postEdit.getContent());

//        return new PostResponse(post);                          // \1의 return 값 수정하면서 추가한것

//        postRepository.save(post);
    }


    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(()-> new PostNotFound());

        postRepository.delete(post);

    }
}
