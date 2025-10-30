package com.quickserve.service;

import com.quickserve.model.*;
import com.quickserve.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    
    // USER LOGIN
    public Map<String, Object> loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            return createErrorResponse("User not found");
        }
        
        User user = userOpt.get();
        if (!user.getPassword().equals(request.getPassword())) {
            return createErrorResponse("Invalid password");
        }
        
        return createSuccessResponse("user", user.getName(), user.getEmail());
    }
    
    // ADMIN LOGIN
    public Map<String, Object> loginAdmin(LoginRequest request) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(request.getEmail());
        
        if (adminOpt.isEmpty()) {
            return createErrorResponse("Admin not found");
        }
        
        Admin admin = adminOpt.get();
        if (!admin.getPassword().equals(request.getPassword())) {
            return createErrorResponse("Invalid password");
        }
        
        return createSuccessResponse("admin", admin.getName(), admin.getEmail());
    }
    
    // SERVICE PROVIDER LOGIN
    public Map<String, Object> loginServiceProvider(LoginRequest request) {
        Optional<ServiceProvider> spOpt = serviceProviderRepository.findByEmail(request.getEmail());
        
        if (spOpt.isEmpty()) {
            return createErrorResponse("Service provider not found");
        }
        
        ServiceProvider sp = spOpt.get();
        if (!sp.getPassword().equals(request.getPassword())) {
            return createErrorResponse("Invalid password");
        }
        
        return createSuccessResponse("sp", sp.getName(), sp.getEmail());
    }
    
    // USER SIGNUP
    public Map<String, Object> signupUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return createErrorResponse("Email already registered");
        }
        
        user.setCreatedAt(LocalDateTime.now().toString());
        User savedUser = userRepository.save(user);
        
        // Don't return the full user object, just success message
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User registered successfully");
        return response;
    }
    
    // ADMIN SIGNUP
    public Map<String, Object> signupAdmin(Admin admin, String adminCode) {
        if (!adminCode.equals("ADMIN2025")) {
            return createErrorResponse("Invalid admin code");
        }
        
        if (adminRepository.existsByEmail(admin.getEmail())) {
            return createErrorResponse("Email already registered");
        }
        
        admin.setCreatedAt(LocalDateTime.now().toString());
        Admin savedAdmin = adminRepository.save(admin);
        
        // Don't return the full admin object, just success message
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Admin registered successfully");
        return response;
    }
    
    // SERVICE PROVIDER SIGNUP
    public Map<String, Object> signupServiceProvider(ServiceProvider sp) {
        if (serviceProviderRepository.existsByEmail(sp.getEmail())) {
            return createErrorResponse("Email already registered");
        }
        
        sp.setCreatedAt(LocalDateTime.now().toString());
        sp.setWorkDays("Monday,Tuesday,Wednesday,Thursday,Friday");
        ServiceProvider savedSp = serviceProviderRepository.save(sp);
        
        // Don't return the full provider object, just success message
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Service provider registered successfully");
        return response;
    }
    
    // Helper methods
    private Map<String, Object> createSuccessResponse(String role, String name, String email) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("role", role);
        response.put("name", name);
        response.put("email", email);
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}