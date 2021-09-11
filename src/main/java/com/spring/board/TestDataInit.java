package com.spring.board;

import com.spring.board.domain.posts.Posts;
import com.spring.board.domain.posts.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final PostsRepository postsRepository;

    @PostConstruct
    public void init() {

        for (int i = 0; i < 199; i++) {
            postsRepository.save(Posts.builder()
                    .title("title " + i)
                    .author("author " + i)
                    .content("content " + i)
                    .build());
        }
    }
}
