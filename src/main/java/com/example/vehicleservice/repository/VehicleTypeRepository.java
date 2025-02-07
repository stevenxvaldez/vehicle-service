package com.example.vehicleservice.repository;

import com.example.vehicleservice.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleTypeRepository extends JpaRepository<VehicleType, UUID> {

    // Find by manufacturer (case insensitive)
    List<VehicleType> findByManufacturerContainingIgnoreCase(String manufacturer);

    // You can also add other custom queries like:
    Optional<VehicleType> findById(UUID id);
}
