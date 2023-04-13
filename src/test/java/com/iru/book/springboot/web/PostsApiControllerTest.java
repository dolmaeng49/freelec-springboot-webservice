package com.iru.book.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iru.book.springboot.config.auth.dto.SessionUser;
import com.iru.book.springboot.domain.posts.Posts;
import com.iru.book.springboot.domain.posts.PostsRepository;
import com.iru.book.springboot.domain.user.Role;
import com.iru.book.springboot.domain.user.User;
import com.iru.book.springboot.domain.user.UserRepository;
import com.iru.book.springboot.web.dto.PostsSaveRequestDto;
import com.iru.book.springboot.web.dto.PostsUpdateRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        User user = User.builder()
                .name("이름")
                .email("test@test.com")
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void savePostsTest() throws Exception {

        // given
        String title = "title";
        String content = "content";
        String author = "author";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";
        User user = userRepository.findAll().get(0);

        // when
        mvc.perform(MockMvcRequestBuilders.post(url)
                        .sessionAttr("user", new SessionUser(user))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        List<Posts> allPosts = postsRepository.findAll();
        Assertions.assertThat(allPosts.get(0).getTitle()).isEqualTo(title);
        Assertions.assertThat(allPosts.get(0).getContent()).isEqualTo(content);

    }

    @Test
    @WithMockUser(roles = "USER")
    public void updatePostTest() throws Exception {
        // given
        User user = userRepository.findAll().get(0);
        Posts savedPost = postsRepository.save(Posts.builder()
                .title("old_title")
                .content("old_content")
                .user(user)
                .build());

        Long targetId = savedPost.getId();
        String expectedTitle = "New Title";
        String expectedContent = "New Content";

        String url = "http://localhost:" + port + "/api/v1/posts/" + targetId;

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();


        // when
        mvc.perform(MockMvcRequestBuilders.put(url)
                        .sessionAttr("user", new SessionUser(user))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        List<Posts> allPosts = postsRepository.findAll();
        Assertions.assertThat(allPosts.get(0).getTitle()).isEqualTo(expectedTitle);
        Assertions.assertThat(allPosts.get(0).getContent()).isEqualTo(expectedContent);
    }
}
