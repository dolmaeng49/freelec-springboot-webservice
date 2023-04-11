package com.iru.book.springboot.domain.posts;

import com.iru.book.springboot.domain.BaseTimeEntity;
import com.iru.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Posts(String title, String content, String author, User user) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.user = user;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
