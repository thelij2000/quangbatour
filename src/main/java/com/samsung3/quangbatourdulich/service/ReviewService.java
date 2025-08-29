package com.samsung3.quangbatourdulich.service;

import com.samsung3.quangbatourdulich.dto.respone.ReviewResponseDTO;
import com.samsung3.quangbatourdulich.entity.Review;
import com.samsung3.quangbatourdulich.entity.Tour;
import com.samsung3.quangbatourdulich.entity.User;
import com.samsung3.quangbatourdulich.exception.ResourceNotFoundException;
import com.samsung3.quangbatourdulich.repository.ReviewRepository;
import com.samsung3.quangbatourdulich.repository.TourRepository;
import com.samsung3.quangbatourdulich.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReviewService(ReviewRepository reviewRepository, TourRepository tourRepository,
                         UserRepository userRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.tourRepository = tourRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public List<ReviewResponseDTO> getReviewsByTourId(Integer tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));
        return reviewRepository.findByTour(tour)
                .stream()
                .map(r -> modelMapper.map(r, ReviewResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO createReview(Integer userId, Integer tourId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));

        Review review = new Review();
        review.setUser(user);
        review.setTour(tour);
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        return modelMapper.map(review, ReviewResponseDTO.class);
    }
}