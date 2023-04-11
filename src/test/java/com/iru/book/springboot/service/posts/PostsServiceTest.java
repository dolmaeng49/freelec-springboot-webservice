package com.iru.book.springboot.service.posts;

import com.iru.book.springboot.config.auth.dto.SessionUser;
import com.iru.book.springboot.domain.posts.PostsRepository;
import com.iru.book.springboot.domain.user.Role;
import com.iru.book.springboot.domain.user.User;
import com.iru.book.springboot.domain.user.UserRepository;
import com.iru.book.springboot.web.dto.PostsListResponseDto;
import com.iru.book.springboot.web.dto.PostsSaveRequestDto;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsServiceTest {

    @Autowired
    PostsService postsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostsRepository postsRepository;

    @Before
    public void joinUser() {
        User user = User.builder()
                .name("이름")
                .email("test@test.com")
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @After
    public void cleanup() {
        postsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void savePostsWithValidUser() {

        // given
        String title = "테스트 글제목";


        User user = userRepository.findAll().get(0);
        SessionUser sessionUser = new SessionUser(user);

        PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content("내용")
                .author("작성자")
                .build();

        // when
        postsService.save(postsSaveRequestDto, sessionUser);

        // then
        List<PostsListResponseDto> postsList = postsService.findAllDesc();
        Assertions.assertThat(postsList.get(0).getTitle()).isEqualTo(title);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void savePostsWithInvalidUser_exceptionThrown() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(Matchers.startsWith("일치하는 회원이 없습니다."));

        // given
        String title = "테스트 글제목";

        User user = User.builder()
                .name("회원1")
                .email("new@new.com")
                .picture("pic")
                .build();

        SessionUser sessionUser = new SessionUser(user);

        PostsSaveRequestDto postsSaveRequestDto = PostsSaveRequestDto.builder()
                .title(title)
                .content("내용")
                .author("작성자")
                .build();

        // when
        postsService.save(postsSaveRequestDto, sessionUser);

        // then
        List<PostsListResponseDto> postsList = postsService.findAllDesc();
        Assertions.assertThat(postsList.get(0).getTitle()).isEqualTo(title);
    }

}