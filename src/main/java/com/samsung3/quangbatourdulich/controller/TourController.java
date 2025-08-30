package com.samsung3.quangbatourdulich.controller;

import com.samsung3.quangbatourdulich.dto.request.TourRequestDTO;
import com.samsung3.quangbatourdulich.dto.respone.TourResponseDTO;
import com.samsung3.quangbatourdulich.service.TourService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/tours")
public class TourController {
    private final TourService tourService;
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public List<TourResponseDTO> getAllTours() {
        return tourService.getAllTours();
    }

    @GetMapping("/{id}")
    public TourResponseDTO getTourById(@PathVariable Integer id) {
        return tourService.getTourById(id);
    }

    @PostMapping
    public ResponseEntity<TourResponseDTO> createTour(@RequestBody TourRequestDTO dto) {
        return ResponseEntity.ok(tourService.createTour(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTour(@PathVariable Integer id) {
        tourService.deleteTour(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}")
    public ResponseEntity<TourResponseDTO> updateTour(@PathVariable Integer id, @RequestBody TourRequestDTO dto) {
        return ResponseEntity.ok(tourService.updateTour(id, dto));
    }
}