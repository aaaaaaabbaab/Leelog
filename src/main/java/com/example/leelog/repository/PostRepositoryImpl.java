package com.example.leelog.repository;

import com.example.leelog.domain.Post;
import com.example.leelog.domain.QPost;
import com.example.leelog.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

   private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {               //글10개만 가져오는 메소드
        return jpaQueryFactory.selectFrom(QPost.post)  //select from 같이적용되는거
                .limit(postSearch.getSize())
                .offset((postSearch.getOffset()))
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
