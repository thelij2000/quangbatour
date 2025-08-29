package com.samsung3.quangbatourdulich.repository;

import com.samsung3.quangbatourdulich.entity.Review;
import com.samsung3.quangbatourdulich.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByTour(Tour tour);
}