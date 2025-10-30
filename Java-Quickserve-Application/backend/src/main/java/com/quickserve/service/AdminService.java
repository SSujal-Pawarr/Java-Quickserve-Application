package com.quickserve.service;

import com.quickserve.model.Admin;
import com.quickserve.model.User;
import com.quickserve.model.ServiceProvider;
import com.quickserve.repository.AdminRepository;
import com.quickserve.repository.UserRepository;
import com.quickserve.repository.ServiceProviderRepository;
import com.quickserve.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    
    @Autowired
    private BookingRepository bookingRepository;
    
    // Get dashboard statistics
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalProviders", serviceProviderRepository.count());
        stats.put("totalBookings", bookingRepository.count());
        stats.put("pendingBookings", bookingRepository.findByStatus("Pending").size());
        return stats;
    }
    
    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Get all service providers
    public List<ServiceProvider> getAllProviders() {
        return serviceProviderRepository.findAll();
    }
    
    // Delete user
    public Map<String, Object> deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return createErrorResponse("User not found");
        }
        
        userRepository.deleteById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User deleted successfully");
        return response;
    }
    
    // Delete service provider
    public Map<String, Object> deleteProvider(Long id) {
        if (!serviceProviderRepository.existsById(id)) {
            return createErrorResponse("Service provider not found");
        }
        
        serviceProviderRepository.deleteById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Service provider deleted successfully");
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}