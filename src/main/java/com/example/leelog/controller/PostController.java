package com.example.leelog.controller;

import com.example.leelog.request.PostCreate;
import com.example.leelog.request.PostEdit;
import com.example.leelog.request.PostSearch;
import com.example.leelog.response.PostResponse;
import com.example.leelog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    // SSR -> jsp, thymeleaf, mustache, freemarker
                // - > html rendering
    ///////// SPA형식으로 만들거
    // SPA -> vue+SSR = nuxt
            // -> javascript + < - > API (JSON)
    // react, next


    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, TREACE, CONNECT

    ////////////////// $$$$$$$$$  글 등록 $$$$$$$$$$  ////////////////////////////////////
    // CRUD   ----- Create
    // POST Method


    // Request 클래스
    @PostMapping("/posts")
//    public String post(@RequestParam String title, @RequestParam String content){
//        log.info("title={},content={}",title, content);
//        return "HELLO WORLD";
//    }
//    public String post(@RequestParam Map<String, String> params){
//        log.info("params={},", params);
//        return "HELLO WORLD";
//    }

    ////이하
    //////////////////dto 생성시 (requset패키지의 PostCreate클래스 파일로 생성)


//        public String post(PostCreate params){
//        log.info("params={},", params);
//        return "HELLO WORLD";
//      }

//    public String post(@RequestBody @Valid PostCreate params) throws Exception {
//
//        // 데이터를 검증하는 이유
//
//        //1. client 개발자가 깜박할 수 있다. 실수로 값을 안보낼 수 있다.
//        //2. client bug로 값이 누락될 수 있다.
//        //3. 외부의 나쁜 사람이 값을 임의로 조작해서 보낼수 있다.
//        //4. DB에 값을 저장할 떄 의도치 않은 오류가 발생할 수 있다.
//        //5. 서버 개발자의 편안함을 위해서
//        log.info("params={},", params.toString());
//
//       String title =  params.getTitle();
//
//       if(title == null || title.equals("")){
//           throw new Exception("타이틀값이 없어요");
//       }
//
//       String content = params.getContent();
//        if(content == null || content.equals("")){
//            throw new Exception("내용이 없어요");
//        }
//        //이런방법의 검증은 1. 빡세다 (노가다)
//        // 2. 개발팁 -> 무언가 3번이상의 반복작업을 할떄 내가 뭔가 잘못하고 있는건 아닐지 의심한다.
//        // 3. 누락가능성
//        // 4. 생각보다 검증해야할 게 많다.(꼼꼼하지 않을 수 있다.)
//        // 5. 개발자스럽지 않다.
//
//        return "HELLO WORLD";
//    }


    //해결-> dto에 @NotBlank어노테이션 추가 , 컨트롤러에 @Valid추가

//    public String post(@RequestBody @Valid PostCreate params){
//
//        log.info("params={},", params.toString());
//
//        String title =  params.getTitle();
//        return "HELLO WORLD";
//    }
    // debug시 오류메시지 띄워줄떄 BindingResult 추가
//    public Map<String, String> post(@RequestBody @Valid PostCreate params, BindingResult result){

//        log.info("params={},", params.toString());

        // 1. 매번 매서드마다값을 검증해야한다. ->검증 부분에서 버그가 발생할 여지가 높다


//        if(result.hasErrors()){
//            List<FieldError> fieldErrors = result.getFieldErrors();
//            FieldError firstFieldError = fieldErrors.get(0);
//            String fieldName = firstFieldError.getField();              //title
//            String errorMessage = firstFieldError.getDefaultMessage();  // ..에러 메시짖
//
//            Map<String, String> error = new HashMap<>();
//            error.put(fieldName,errorMessage);
//            return error;
//
//        }
//        return Map.of();
//    }
//    public Map<String, String> post(@RequestBody @Valid PostCreate request){
//        // repository.save(params)
//        postService.write(request);
//
//        return Map.of();
//    }

//    public void post(@RequestBody @Valid PostCreate request){
//        // repository.save(params)
//        postService.write(request);
//
//    }

    //// save한거를 Json형식(key:value)로 넘겨줄떄
    // Case1. 저장한 데이터 Entity -> response로 응답하기
//    public Post post(@RequestBody @Valid PostCreate request){
//        // repository.save(params)
//        return postService.write(request);
//
//    }

    // Case2. 저장한 데이터의  primary_id -> response로 응답하기
    //                  Client에서는 수시한 id를 post 글 조회 API를 통해서 글 데이터를 수신받음
//    public Map post(@RequestBody @Valid PostCreate request){
//        // repository.save(params)
//        Long postId =  postService.write(request);
//        return Map.of("postId",postId);
//    }

    // Case3. 응답 필요 없음 -> Client에서 모든 POST(글) 데이터 context를 잘 관리함
        public void post(@RequestBody @Valid PostCreate request){
        // repository.save(params)

//        if(request.getTitle().contains("바보")){      /////예를 들어 제목에는 "바보"라는 단어가 포함되면안되게 정책이생겼다고 가정
//            throw new InvalidRequest();
//        }
        ////위의 if는 메소드를 만들어 해결하기위해 PostCreate클래스에 다시작성하므로  --> request.validate(); 추가함
        request.validate();


        postService.write(request);
        }


    // Bad Case : 서버에서 -> 반드시 이렇게 할껍니다! fix
    //          -> 서버에서 차라리 유연하게 대응하는게 좋습니다. -> 코드를 잘짜야함
    //          -> 한 번에 일괄적으로 잘 처리되는 케이스가 없습니다 -> 잘 관리하는 형태가 중요.

    /**
     *  /posts -> 글 전체 조회(검색 + 페이징)
     *  /posts/{postId} -> 글 한개만 조회
     */


    ///////////////// 조회 API
    /////////////////  $$$$$$$$$$$$$$$$  단건 조회 API (1개의 글Post를 가져오는 기능)  $$$$$$$$$$$  ///////////
    // CRUD   ----- Read
//    @GetMapping("/posts/{postId}")
//    public Post get(@PathVariable(name = "postId")Long id){
//       Post post =  postService.get(id);
//       return post;
//    }
    ////////////응답클래스 분리로인해 get메소드 사용안하게됌



    // 클라이언트 요구사항
    // json 응답에서 title 값 길이를 최대 10글자로 해주세요.
    // (이런 처리는 클라이언트에서 하는게 좋음)
    // entity(Post클래스파일)에서 --> getTitle() 직접작성
    //-->이 요구사항정책과 이후의 바뀌는 요구사항의정책이 충돌할수가있음
    /// 이 블로그가 잘되서 RSS발행

//    @GetMapping("/posts/{postId}/rss")
//    public Post getRss(@PathVariable(name = "postId")Long id){
//        Post post =  postService.getRss(id);
//        return post;
//        //실제 rss에서는 10글자이상의 전체가 내려가야함에도 10글자만 내려가는 상황발생
//        // 15 save -> 10 save
//    }
    //// 해결 방안 -> 응답클래스를 분리 (PostResponse 클래스)

    // Response 클래스
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId){

       return postService.get(postId);

    }

    // 조회 API
    ////////////////////////////////////// $$$$$$$$$$$$$$$ 여래개의 글을 조회 API   $$$$$$$$$$$$$$$$$////////////////////////
    // CRUD   ----- Read
    // /posts

//    @GetMapping("/posts")
//    public List<PostResponse> getList() {
//        return postService.getList();
//    }

    /////////  -->  페이징 처리후

    @GetMapping("/posts")
//    public List<PostResponse> getList(@RequestParam int page) {         //PostControllerTest 에서 넘긴 /posts?page=0 받기위해 @RequestParam 사용
//    public List<PostResponse> getList(Pageable pageable) {         //org.springframework.data.domain.Pageable 사용
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {            // PostSearch클래스 생성후
        return postService.getList(postSearch);                               // getList() 에 넣어줌
    }


    ////////////////////////////////////   $$$$$$$$$$$$$$ 게시글의 수정 API  $$$$$$$$$$$$$$$$///////////////////////
    //CRUD -> Update

    @PatchMapping("posts/{postId}")
//    public PostResponse edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){    //  \1 return 형태를 void 대신 PostResponse 로 수정 (*클라이언트에서 json 형식으로달라고 요청할경우)
//      return  postService.edit(postId, request);
//    }

    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request){    //  \1 return 형태를 void 대신 PostResponse 로 수정 (*클라이언트에서 json 형식으로달라고 요청할경우)
          postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }

}


// API 문서 생성

// GET /posts/{postId] -> 단건 조회

// POST / posts -> 게시글 등록

// 클라이언트 입장 어떤 API 있는지 모름

// Spring RestDocs
// - 운영코드에 -> 영향x
// - Test 케이스 실행 -> 무서를 생성해준다.
