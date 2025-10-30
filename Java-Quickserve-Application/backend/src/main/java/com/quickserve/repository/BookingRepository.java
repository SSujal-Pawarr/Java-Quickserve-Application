package com.quickserve.repository;

import com.quickserve.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerEmail(String customerEmail);
    List<Booking> findByService(String service);
    List<Booking> findByStatus(String status);
}