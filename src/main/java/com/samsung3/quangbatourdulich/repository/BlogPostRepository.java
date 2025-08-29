package com.samsung3.quangbatourdulich.repository;

import com.samsung3.quangbatourdulich.entity.BlogPost;
import com.samsung3.quangbatourdulich.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    List<BlogPost> findByAuthor(User author);
}