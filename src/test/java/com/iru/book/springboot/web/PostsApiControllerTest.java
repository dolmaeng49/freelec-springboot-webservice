package com.iru.book.springboot.web;

import com.iru.book.springboot.domain.posts.Posts;
import com.iru.book.springboot.domain.posts.PostsRepository;
import com.iru.book.springboot.web.dto.PostsSaveRequestDto;
import com.iru.book.springboot.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }
    @Test
    public void savePostsTest() throws Exception {

        String title = "title";
        String content = "content";
        String author = "author";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/vi/posts";

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> allPosts = postsRepository.findAll();
        Assertions.assertThat(allPosts.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(allPosts.get(0).getContent()).isEqualTo(content);

    }

    @Test
    public void updatePostTest() throws Exception {
        // given
        Posts savedPost = postsRepository.save(Posts.builder()
                .title("old_title")
                .content("old_content")
                .build());

        Long targetId = savedPost.getId();
        String expectedTitle = "New Title";
        String expectedContent = "New Content";

        String url = "http://localhost:" + port + "/api/v1/posts/" + targetId;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // then
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> allPosts = postsRepository.findAll();
        Assertions.assertThat(allPosts.get(0).getTitle()).isEqualTo(expectedTitle);
        Assertions.assertThat(allPosts.get(0).getContent()).isEqualTo(expectedContent);
    }
}
