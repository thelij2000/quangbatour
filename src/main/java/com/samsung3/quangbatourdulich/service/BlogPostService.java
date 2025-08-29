package com.samsung3.quangbatourdulich.service;

import com.samsung3.quangbatourdulich.dto.request.BlogPostRequestDTO;
import com.samsung3.quangbatourdulich.dto.respone.BlogPostResponseDTO;
import com.samsung3.quangbatourdulich.entity.BlogPost;
import com.samsung3.quangbatourdulich.entity.User;
import com.samsung3.quangbatourdulich.exception.ResourceNotFoundException;
import com.samsung3.quangbatourdulich.repository.BlogPostRepository;
import com.samsung3.quangbatourdulich.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public BlogPostService(BlogPostRepository blogPostRepository, UserRepository userRepository,
                           ModelMapper modelMapper) {
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }
public List<BlogPostResponseDTO> getAllPosts() {
    return blogPostRepository.findAll()
            .stream()
            .map(post -> modelMapper.map(post, BlogPostResponseDTO.class))
            .collect(Collectors.toList());
}

public BlogPostResponseDTO getPostById(Integer id) {
    BlogPost post = blogPostRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Blog post not found with id: " + id));
    return modelMapper.map(post, BlogPostResponseDTO.class);
}

public BlogPostResponseDTO createPost(BlogPostRequestDTO dto) {
    User author = userRepository.findById(dto.getAuthorId())
            .orElseThrow(() -> new ResourceNotFoundException("Author not found"));

    BlogPost post = modelMapper.map(dto, BlogPost.class);
    post.setAuthor(author);
    post.setCreatedAt(LocalDateTime.now());
    blogPostRepository.save(post);
    return modelMapper.map(post, BlogPostResponseDTO.class);
}

public void deletePost(Integer id) {
    if (!blogPostRepository.existsById(id)) {
        throw new ResourceNotFoundException("Blog post not found with id: " + id);
    }
    blogPostRepository.deleteById(id);
}
}