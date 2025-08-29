package com.samsung3.quangbatourdulich.controller;

import com.samsung3.quangbatourdulich.dto.request.BlogPostRequestDTO;
import com.samsung3.quangbatourdulich.dto.respone.BlogPostResponseDTO;
import com.samsung3.quangbatourdulich.service.BlogPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/blogposts")
public class BlogPostController {
    private final BlogPostService blogPostService;
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping
    public List<BlogPostResponseDTO> getAllPosts() {
        return blogPostService.getAllPosts();
    }

    @GetMapping("/{id}")
    public BlogPostResponseDTO getPostById(@PathVariable Integer id) {
        return blogPostService.getPostById(id);
    }

    @PostMapping
    public ResponseEntity<BlogPostResponseDTO> createPost(@RequestBody BlogPostRequestDTO dto) {
        return ResponseEntity.ok(blogPostService.createPost(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        blogPostService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}