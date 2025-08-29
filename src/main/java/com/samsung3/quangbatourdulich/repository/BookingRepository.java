package com.samsung3.quangbatourdulich.repository;

import com.samsung3.quangbatourdulich.entity.Booking;
import com.samsung3.quangbatourdulich.entity.User;
import com.samsung3.quangbatourdulich.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUser(User user);
    List<Booking> findByBookingStatus(Status status);
}