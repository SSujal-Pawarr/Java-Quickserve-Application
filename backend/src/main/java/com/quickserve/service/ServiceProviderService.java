package com.quickserve.service;

import com.quickserve.model.ServiceProvider;
import com.quickserve.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ServiceProviderService {
    
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;
    
    // Get provider by email
    public Optional<ServiceProvider> getProviderByEmail(String email) {
        return serviceProviderRepository.findByEmail(email);
    }
    
    // Get providers by service type
    public List<ServiceProvider> getProvidersByServiceType(String serviceType) {
        return serviceProviderRepository.findByServiceType(serviceType);
    }
    
    // Update provider profile
   // Update provider profile
public Map<String, Object> updateProvider(String email, ServiceProvider updatedProvider) {
    Optional<ServiceProvider> providerOpt = serviceProviderRepository.findByEmail(email);
    
    if (providerOpt.isEmpty()) {
        return createErrorResponse("Service provider not found");
    }
    
    ServiceProvider provider = providerOpt.get();
    
    // Only update if values are provided
    if (updatedProvider.getName() != null && !updatedProvider.getName().isEmpty()) {
        provider.setName(updatedProvider.getName());
    }
    if (updatedProvider.getPhone() != null && !updatedProvider.getPhone().isEmpty()) {
        provider.setPhone(updatedProvider.getPhone());
    }
    if (updatedProvider.getServiceType() != null && !updatedProvider.getServiceType().isEmpty()) {
        provider.setServiceType(updatedProvider.getServiceType());
    }
    if (updatedProvider.getExperience() != null) {
        provider.setExperience(updatedProvider.getExperience());
    }
    if (updatedProvider.getArea() != null && !updatedProvider.getArea().isEmpty()) {
        provider.setArea(updatedProvider.getArea());
    }
    if (updatedProvider.getBio() != null) {
        provider.setBio(updatedProvider.getBio());
    }
    
    // Don't update password unless explicitly provided and not empty
    if (updatedProvider.getPassword() != null && !updatedProvider.getPassword().isEmpty()) {
        provider.setPassword(updatedProvider.getPassword());
    }
    
    serviceProviderRepository.save(provider);
    
    Map<String, Object> response = new HashMap<>();
    response.put("status", "success");
    response.put("message", "Profile updated successfully");
    response.put("provider", provider);
    return response;
}
    
    // Update availability
   // Update availability
public Map<String, Object> updateAvailability(String email, Map<String, Object> availability) {
    Optional<ServiceProvider> providerOpt = serviceProviderRepository.findByEmail(email);
    
    if (providerOpt.isEmpty()) {
        return createErrorResponse("Service provider not found");
    }
    
    ServiceProvider provider = providerOpt.get();
    
    try {
        if (availability.containsKey("status")) {
            provider.setAvailabilityStatus((String) availability.get("status"));
        }
        if (availability.containsKey("workDays")) {
            provider.setWorkDays((String) availability.get("workDays"));
        }
        if (availability.containsKey("workStart")) {
            provider.setWorkStart((String) availability.get("workStart"));
        }
        if (availability.containsKey("workEnd")) {
            provider.setWorkEnd((String) availability.get("workEnd"));
        }
        if (availability.containsKey("serviceRadius")) {
            Object radiusObj = availability.get("serviceRadius");
            if (radiusObj instanceof Integer) {
                provider.setServiceRadius((Integer) radiusObj);
            } else if (radiusObj instanceof String) {
                provider.setServiceRadius(Integer.parseInt((String) radiusObj));
            }
        }
        
        serviceProviderRepository.save(provider);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Availability updated successfully");
        return response;
    } catch (Exception e) {
        return createErrorResponse("Error updating availability: " + e.getMessage());
    }
}
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        return response;
    }
}