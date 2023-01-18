package com.example.leelog.repository;

import com.example.leelog.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {       //PostRepositoryCustom 추가함으로 PostRepositoryImpl의 기능들이 자동으로 PostRepository에 주입댐
}
