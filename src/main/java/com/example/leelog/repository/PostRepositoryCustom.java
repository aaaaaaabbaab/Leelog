package com.example.leelog.repository;

import com.example.leelog.domain.Post;
import com.example.leelog.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
