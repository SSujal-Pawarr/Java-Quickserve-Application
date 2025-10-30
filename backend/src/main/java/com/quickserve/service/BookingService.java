package com.quickserve.service;

import com.quickserve.model.Booking;
import com.quickserve.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    // Create new booking
    public Map<String, Object> createBooking(Booking booking) {
        booking.setStatus("Pending");
        booking.setBookedAt(LocalDateTime.now().toString());
        
        Booking savedBooking = bookingRepository.save(booking);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Booking created successfully");
        response.put("booking", savedBooking);
        return response;
    }
    
    // Get all bookings
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    // Get bookings by customer email
    public List<Booking> getBookingsByCustomerEmail(String email) {
        return bookingRepository.findByCustomerEmail(email);
    }
    
    // Get bookings by service type
    public List<Booking> getBookingsByService(String service) {
        return bookingRepository.findByService(service);
    }
    
    // Get bookings by status
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }
    
    // Update booking status
    public Map<String, Object> updateBookingStatus(Long id, String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(id);
        
        if (bookingOpt.isEmpty()) {
            return createErrorResponse("Booking not found");
        }
        
        Booking booking = bookingOpt.get();
        booking.setStatus(status);
        bookingRepository.save(booking);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Booking status updated to " + status);
        response.put("booking", booking);
        return response;
    }
    
    // Delete booking
    public Map<String, Object> deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            return createErrorResponse("Booking not found");
        }
        
        bookingRepository.deleteById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Booking deleted successfully");
        return response;
    }
    
    // Get booking statistics
    public Map<String, Object> getBookingStats() {
        List<Booking> allBookings = bookingRepository.findAll();
        
        long total = allBookings.size();
        long pending = allBookings.stream().filter(b -> b.getStatus().equals("Pending")).count();
        long accepted = allBookings.stream().filter(b -> b.getStatus().equals("Accepted")).count();
        long completed = allBookings.stream().filter(b -> b.getStatus().equals("Completed")).count();
        long rejected = allBookings.stream().filter(b -> b.getStatus().equals("Rejected")).count();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("pending", pending);
        stats.put("accepted", accepted);
        stats.put("completed", completed);
        stats.put("rejected", rejected);
        
        return stats;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}