package com.samsung3.quangbatourdulich.controller;

import com.samsung3.quangbatourdulich.dto.respone.BookingReponseDTO;
import com.samsung3.quangbatourdulich.dto.request.BookingRequestDTO;
import com.samsung3.quangbatourdulich.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingReponseDTO> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @PostMapping
    public ResponseEntity<BookingReponseDTO> createBooking(@Valid @RequestBody BookingRequestDTO request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }
}