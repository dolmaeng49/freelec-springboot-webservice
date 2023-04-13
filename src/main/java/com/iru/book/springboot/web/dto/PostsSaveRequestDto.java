package com.iru.book.springboot.web.dto;

import com.iru.book.springboot.domain.posts.Posts;
import com.iru.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsSaveRequestDto {

    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Posts toEntity(User user) {
        return Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .user(user)
                .build();
    }
}
