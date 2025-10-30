package com.quickserve.controller;

import com.quickserve.model.User;
import com.quickserve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // Get user by email
    @GetMapping("/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(user.get());
    }
    
    // Update user profile
    @PutMapping("/{email}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable String email, 
            @RequestBody User updatedUser) {
        
        Map<String, Object> response = userService.updateUser(email, updatedUser);
        return ResponseEntity.ok(response);
    }
}