package com.example.vehicleservice.service;

import com.example.vehicleservice.model.VehicleType;
import com.example.vehicleservice.repository.VehicleTypeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VehicleTypeService {

    private final VehicleTypeRepository repository;

    // Create or update a vehicle type
    @Transactional
    public VehicleType saveOrUpdateVehicleType(VehicleType vehicleType) {
        // Use merge for update operations to properly handle version and entity state
        if (vehicleType.getId() != null) {
            // If the entity is detached and has an ID, merge it to re-attach to the session
            return repository.save(vehicleType);  // Save will do the right thing based on the entity state
        } else {
            // Create a new entity
            return repository.save(vehicleType);
        }
    }

    // Get all vehicle types with filtering and sorting
    public List<VehicleType> getAllVehicleTypes(String manufacturer, String model, String type, String sortBy, String sortOrder) {
        Sort sort = Sort.by(Sort.Order.asc("year"));  // Default sorting by year ascending

        if ("desc".equalsIgnoreCase(sortOrder)) {
            sort = Sort.by(Sort.Order.desc(sortBy != null ? sortBy : "year"));
        }

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort); // Fetch all with pagination
        List<VehicleType> vehicles = repository.findAll(pageable).getContent();

        if (manufacturer != null) {
            vehicles = vehicles.stream().filter(v -> v.getManufacturer().equalsIgnoreCase(manufacturer)).collect(Collectors.toList());
        }
        if (model != null) {
            vehicles = vehicles.stream().filter(v -> v.getModel().equalsIgnoreCase(model)).collect(Collectors.toList());
        }
        if (type != null) {
            vehicles = vehicles.stream().filter(v -> v.getType().name().equalsIgnoreCase(type)).collect(Collectors.toList());
        }

        return vehicles;
    }

    // Get vehicle by ID
    public VehicleType getVehicleById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    // Delete vehicle by ID
    public void deleteVehicleById(UUID id) {
        repository.deleteById(id);
    }
}
