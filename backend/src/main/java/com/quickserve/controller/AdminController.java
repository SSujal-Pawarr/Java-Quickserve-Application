package com.quickserve.controller;

import com.quickserve.model.User;
import com.quickserve.model.ServiceProvider;
import com.quickserve.service.AdminService;
import com.quickserve.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private BookingService bookingService;
    
    // Get dashboard statistics
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    
    // Get all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // Get all service providers
    @GetMapping("/providers")
    public ResponseEntity<List<ServiceProvider>> getAllProviders() {
        List<ServiceProvider> providers = adminService.getAllProviders();
        return ResponseEntity.ok(providers);
    }
    
    // Delete user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            Map<String, Object> response = adminService.deleteUser(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error deleting user: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    // Delete service provider
    @DeleteMapping("/providers/{id}")
    public ResponseEntity<Map<String, Object>> deleteProvider(@PathVariable Long id) {
        try {
            Map<String, Object> response = adminService.deleteProvider(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error deleting provider: " + e.getMessage());
            return ResponseEntity.ok(errorResponse);
        }
    }
    
    // Get all bookings
    @GetMapping("/bookings")
    public ResponseEntity<List> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }
    
    // Get booking statistics
    @GetMapping("/bookings/stats")
    public ResponseEntity<Map<String, Object>> getBookingStats() {
        return ResponseEntity.ok(bookingService.getBookingStats());
    }
}