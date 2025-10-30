package com.quickserve.repository;

import com.quickserve.model.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceProviderRepository extends JpaRepository<ServiceProvider, Long> {
    Optional<ServiceProvider> findByEmail(String email);
    boolean existsByEmail(String email);
    List<ServiceProvider> findByServiceType(String serviceType);
}