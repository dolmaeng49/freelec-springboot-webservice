package com.iru.book.springboot.web;

import com.iru.book.springboot.config.auth.LoginUser;
import com.iru.book.springboot.config.auth.dto.SessionUser;
import com.iru.book.springboot.service.posts.PostsService;
import com.iru.book.springboot.web.dto.PostsResponseDto;
import com.iru.book.springboot.web.dto.PostsSaveRequestDto;
import com.iru.book.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto, @LoginUser SessionUser sessionUser) {
        return postsService.save(requestDto, sessionUser);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto, @LoginUser SessionUser sessionUser) {
        return postsService.update(id, requestDto, sessionUser);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id) {
        return postsService.findById(id);
    }

    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id, @LoginUser SessionUser sessionUser) {
        postsService.delete(id, sessionUser);
        return id;
    }
}
