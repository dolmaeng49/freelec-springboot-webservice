package com.iru.book.springboot.service.posts;

import com.iru.book.springboot.config.auth.dto.SessionUser;
import com.iru.book.springboot.domain.posts.Posts;
import com.iru.book.springboot.domain.posts.PostsRepository;
import com.iru.book.springboot.domain.user.User;
import com.iru.book.springboot.domain.user.UserRepository;
import com.iru.book.springboot.web.dto.PostsListResponseDto;
import com.iru.book.springboot.web.dto.PostsResponseDto;
import com.iru.book.springboot.web.dto.PostsSaveRequestDto;
import com.iru.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto, SessionUser sessionUser) {

        User user = userRepository.findByEmail(sessionUser.getEmail()).orElseThrow(() ->
                new IllegalArgumentException("일치하는 회원이 없습니다. email=" + sessionUser.getEmail()));

        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);
    }
}
