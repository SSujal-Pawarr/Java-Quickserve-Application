package com.quickserve.service;

import com.quickserve.model.User;
import com.quickserve.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Update user profile
    public Map<String, Object> updateUser(String email, User updatedUser) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return createErrorResponse("User not found");
        }
        
        User user = userOpt.get();
        user.setName(updatedUser.getName());
        user.setPhone(updatedUser.getPhone());
        user.setAddress(updatedUser.getAddress());
        
        userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Profile updated successfully");
        response.put("user", user);
        return response;
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}