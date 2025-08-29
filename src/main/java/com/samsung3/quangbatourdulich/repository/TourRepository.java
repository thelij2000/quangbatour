package com.samsung3.quangbatourdulich.repository;

import com.samsung3.quangbatourdulich.entity.Tour;
import com.samsung3.quangbatourdulich.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Integer> {
    List<Tour> findByStatus(Status status);
    List<Tour> findByLocationContainingIgnoreCase(String location);
}