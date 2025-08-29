package com.samsung3.quangbatourdulich.controller;

import com.samsung3.quangbatourdulich.dto.respone.ReviewResponseDTO;
import com.samsung3.quangbatourdulich.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/tour/{tourId}")
    public List<ReviewResponseDTO> getReviewsByTourId(@PathVariable Integer tourId) {
        return reviewService.getReviewsByTourId(tourId);
    }

    @PostMapping
    public ResponseEntity<ReviewResponseDTO> createReview(
            @RequestParam Integer userId,
            @RequestParam Integer tourId,
            @RequestParam Integer rating,
            @RequestParam String comment) {
        return ResponseEntity.ok(reviewService.createReview(userId, tourId, rating, comment));
    }
}