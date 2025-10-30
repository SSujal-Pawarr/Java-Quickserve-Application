package com.quickserve.controller;

import com.quickserve.model.*;
import com.quickserve.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    // USER LOGIN
    @PostMapping("/login/user")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody LoginRequest request) {
        Map<String, Object> response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }
    
    // ADMIN LOGIN
    @PostMapping("/login/admin")
    public ResponseEntity<Map<String, Object>> loginAdmin(@RequestBody LoginRequest request) {
        Map<String, Object> response = authService.loginAdmin(request);
        return ResponseEntity.ok(response);
    }
    
    // SERVICE PROVIDER LOGIN
    @PostMapping("/login/sp")
    public ResponseEntity<Map<String, Object>> loginServiceProvider(@RequestBody LoginRequest request) {
        Map<String, Object> response = authService.loginServiceProvider(request);
        return ResponseEntity.ok(response);
    }
    
    // USER SIGNUP
    @PostMapping("/signup/user")
    public ResponseEntity<Map<String, Object>> signupUser(@RequestBody User user) {
        Map<String, Object> response = authService.signupUser(user);
        return ResponseEntity.ok(response);
    }
    
    // ADMIN SIGNUP
    @PostMapping("/signup/admin")
    public ResponseEntity<Map<String, Object>> signupAdmin(
            @RequestBody Map<String, Object> requestBody) {
        
        Admin admin = new Admin();
        admin.setName((String) requestBody.get("name"));
        admin.setEmail((String) requestBody.get("email"));
        admin.setPassword((String) requestBody.get("password"));
        
        String adminCode = (String) requestBody.get("adminCode");
        
        Map<String, Object> response = authService.signupAdmin(admin, adminCode);
        return ResponseEntity.ok(response);
    }
    
    // SERVICE PROVIDER SIGNUP
    @PostMapping("/signup/sp")
    public ResponseEntity<Map<String, Object>> signupServiceProvider(@RequestBody ServiceProvider sp) {
        Map<String, Object> response = authService.signupServiceProvider(sp);
        return ResponseEntity.ok(response);
    }
}