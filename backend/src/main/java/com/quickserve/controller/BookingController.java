package com.quickserve.controller;

import com.quickserve.model.Booking;
import com.quickserve.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    // Create new booking
    @PostMapping
    public ResponseEntity<Map<String, Object>> createBooking(@RequestBody Booking booking) {
        try {
            Map<String, Object> response = bookingService.createBooking(booking);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error creating booking: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    // Get all bookings
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    // Get bookings by customer email
    @GetMapping("/user/{email}")
    public ResponseEntity<List<Booking>> getBookingsByCustomerEmail(@PathVariable String email) {
        List<Booking> bookings = bookingService.getBookingsByCustomerEmail(email);
        return ResponseEntity.ok(bookings);
    }
    
    // Get bookings by service type
    @GetMapping("/service/{serviceType}")
    public ResponseEntity<List<Booking>> getBookingsByService(@PathVariable String serviceType) {
        System.out.println("Getting bookings for service type: " + serviceType);
        List<Booking> bookings = bookingService.getBookingsByService(serviceType);
        System.out.println("Found " + bookings.size() + " bookings");
        return ResponseEntity.ok(bookings);
    }
    
    // Get bookings by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable String status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }
    
    // Update booking status
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateBookingStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> statusUpdate) {
        try {
            System.out.println("Updating booking " + id + " to status: " + statusUpdate.get("status"));
            String status = statusUpdate.get("status");
            Map<String, Object> response = bookingService.updateBookingStatus(id, status);
            System.out.println("Update response: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error updating booking: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error updating booking: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    // Delete booking
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteBooking(@PathVariable Long id) {
        try {
            Map<String, Object> response = bookingService.deleteBooking(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error deleting booking: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    // Get booking statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getBookingStats() {
        Map<String, Object> stats = bookingService.getBookingStats();
        return ResponseEntity.ok(stats);
    }
}