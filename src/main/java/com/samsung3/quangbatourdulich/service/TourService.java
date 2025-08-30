package com.samsung3.quangbatourdulich.service;

import com.samsung3.quangbatourdulich.dto.request.TourRequestDTO;
import com.samsung3.quangbatourdulich.dto.respone.TourResponseDTO;
import com.samsung3.quangbatourdulich.entity.Tour;
import com.samsung3.quangbatourdulich.enums.Status;
import com.samsung3.quangbatourdulich.exception.ResourceNotFoundException;
import com.samsung3.quangbatourdulich.repository.TourRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final ModelMapper modelMapper;

    public TourService(TourRepository tourRepository, ModelMapper modelMapper) {
        this.tourRepository = tourRepository;
        this.modelMapper = modelMapper;
    }

    public List<TourResponseDTO> getAllTours() {
        return tourRepository.findAll()
                .stream()
                .map(tour -> modelMapper.map(tour, TourResponseDTO.class))
                .collect(Collectors.toList());
    }

    public TourResponseDTO getTourById(Integer id) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + id));
        return modelMapper.map(tour, TourResponseDTO.class);
    }

    public TourResponseDTO createTour(TourRequestDTO dto) {
        Tour tour = modelMapper.map(dto, Tour.class);
        tour.setStatus(Status.AVAILABLE);
        tourRepository.save(tour);
        return modelMapper.map(tour, TourResponseDTO.class);
    }

    public void deleteTour(Integer id) {
        if (!tourRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tour not found with id: " + id);
        }
        tourRepository.deleteById(id);
    }

    public TourResponseDTO updateTour(Integer id, TourRequestDTO dto) {
        Tour existingTour = tourRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found with id: " + id));
        existingTour.setTourName(dto.getTourName());
        existingTour.setDescription(dto.getDescription());
        existingTour.setLocation(dto.getLocation());
        existingTour.setThumbnailUrl(dto.getThumbnailUrl());
        return modelMapper.map(tourRepository.save(existingTour), TourResponseDTO.class);
    }
}