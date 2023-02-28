package com.iru.book.springboot.domain.posts;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void postAndGetPost() {

        String title = "제목";
        String content = "내용";

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("dm49@naver.com")
                .build());

        List<Posts> postsList = postsRepository.findAll();

        Posts posts = postsList.get(0);
        Assertions.assertThat(posts.getTitle()).isEqualTo(title);
        Assertions.assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void checkBaseTimeEntity() {
        // given
        LocalDateTime now = LocalDateTime.of(2023, 2, 7, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("제목")
                .content("내용")
                .author("글쓴이")
                .build());

        // when
        List<Posts> allList = postsRepository.findAll();

        // then
        Posts posts = allList.get(0);

        System.out.println(">>>>>>>>>>>>> createdDate=" + posts.getCreatedDate()
                + ", modifiedDate=" + posts.getModifiedDate());

        Assertions.assertThat(posts.getCreatedDate()).isAfter(now);
        Assertions.assertThat(posts.getModifiedDate()).isAfter(now);

    }
}
