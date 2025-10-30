package com.quickserve.controller;

import com.quickserve.model.ServiceProvider;
import com.quickserve.service.ServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/providers")
@CrossOrigin(origins = "*")
public class ServiceProviderController {
    
    @Autowired
    private ServiceProviderService serviceProviderService;
    
    // Get provider by email
    @GetMapping("/{email}")
    public ResponseEntity<?> getProviderByEmail(@PathVariable String email) {
        Optional<ServiceProvider> provider = serviceProviderService.getProviderByEmail(email);
        
        if (provider.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(provider.get());
    }
    
    // Get providers by service type
    @GetMapping("/service/{serviceType}")
    public ResponseEntity<List<ServiceProvider>> getProvidersByServiceType(@PathVariable String serviceType) {
        List<ServiceProvider> providers = serviceProviderService.getProvidersByServiceType(serviceType);
        return ResponseEntity.ok(providers);
    }
    
    // Update provider profile
    @PutMapping("/{email}")
    public ResponseEntity<Map<String, Object>> updateProvider(
            @PathVariable String email,
            @RequestBody ServiceProvider updatedProvider) {
        
        Map<String, Object> response = serviceProviderService.updateProvider(email, updatedProvider);
        return ResponseEntity.ok(response);
    }
    
    // Update availability
    @PutMapping("/{email}/availability")
    public ResponseEntity<Map<String, Object>> updateAvailability(
            @PathVariable String email,
            @RequestBody Map<String, Object> availability) {
        
        Map<String, Object> response = serviceProviderService.updateAvailability(email, availability);
        return ResponseEntity.ok(response);
    }
}