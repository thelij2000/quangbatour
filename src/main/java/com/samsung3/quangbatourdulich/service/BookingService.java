package com.samsung3.quangbatourdulich.service;

import com.samsung3.quangbatourdulich.dto.respone.BookingReponseDTO;
import com.samsung3.quangbatourdulich.dto.request.BookingRequestDTO;
import com.samsung3.quangbatourdulich.entity.Booking;
import com.samsung3.quangbatourdulich.entity.Tour;
import com.samsung3.quangbatourdulich.entity.User;
import com.samsung3.quangbatourdulich.enums.BookingStatus;
import com.samsung3.quangbatourdulich.exception.ResourceNotFoundException;
import com.samsung3.quangbatourdulich.repository.BookingRepository;
import com.samsung3.quangbatourdulich.repository.TourRepository;
import com.samsung3.quangbatourdulich.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final ModelMapper modelMapper;

    public BookingService(BookingRepository bookingRepository, UserRepository userRepository,
                          TourRepository tourRepository, ModelMapper modelMapper) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tourRepository = tourRepository;
        this.modelMapper = modelMapper;
    }

    public List<BookingReponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(b -> modelMapper.map(b, BookingReponseDTO.class))
                .collect(Collectors.toList());
    }

    public BookingReponseDTO createBooking(BookingRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Tour tour = tourRepository.findById(request.getTourId())
                .orElseThrow(() -> new ResourceNotFoundException("Tour not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTour(tour);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setBookingDate(LocalDateTime.now());
        booking.setNumPeople(request.getNumPeople());
        booking.setNotes(request.getNotes());
        booking.setBookingStatus(BookingStatus.PENDING);
        if (booking.getStartDate() != null && booking.getEndDate() != null
                && !booking.getEndDate().isBefore(booking.getStartDate())) {
            long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;
            BigDecimal pricePerDay = BigDecimal.valueOf(1_000_000L);
            BigDecimal total = pricePerDay
                    .multiply(BigDecimal.valueOf(days))
                    .multiply(BigDecimal.valueOf(booking.getNumPeople()));

            booking.setTotalAmount(total);
        } else {
            booking.setTotalAmount(BigDecimal.ZERO);
        }
        bookingRepository.save(booking);
        return modelMapper.map(booking, BookingReponseDTO.class);
    }

    public void deleteBooking(Integer id) {
        if (!bookingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }
}