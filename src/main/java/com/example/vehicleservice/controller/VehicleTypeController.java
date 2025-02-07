package com.example.vehicleservice.controller;

import com.example.vehicleservice.model.VehicleType;
import com.example.vehicleservice.service.VehicleTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleTypeController {

    private final VehicleTypeService service;

    // Create a new vehicle type
    @PostMapping
    @Operation(summary = "Create a new vehicle type", description = "Creates a new vehicle type entry in the system.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created vehicle type"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<VehicleType> saveOrUpdateVehicleType(@Valid @RequestBody VehicleType vehicleType) {
        return ResponseEntity.ok(service.saveOrUpdateVehicleType(vehicleType));
    }

    // Fetch all vehicles with optional filtering and sorting
    @GetMapping
    @Operation(summary = "Get all vehicle types", description = "Fetch all vehicle types with optional filtering by manufacturer, model, or type. Supports sorting by year, price, etc.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully fetched vehicle types"),
        @ApiResponse(responseCode = "400", description = "Invalid query parameters")
    })
    public ResponseEntity<List<VehicleType>> getAllVehicleTypes(
            @RequestParam(required = false) @Parameter(description = "Filter by manufacturer") String manufacturer,
            @RequestParam(required = false) @Parameter(description = "Filter by model") String model,
            @RequestParam(required = false) @Parameter(description = "Filter by type (e.g., sedan, SUV)") String type,
            @RequestParam(required = false) @Parameter(description = "Sort by (e.g., year, price)") String sortBy,
            @RequestParam(defaultValue = "asc") @Parameter(description = "Sort order: asc or desc") String sortOrder) {

        return ResponseEntity.ok(service.getAllVehicleTypes(manufacturer, model, type, sortBy, sortOrder));
    }

    // Get details of a specific vehicle by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get details of a specific vehicle", description = "Fetch details of a specific vehicle by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully fetched vehicle details"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<VehicleType> getVehicleById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getVehicleById(id));
    }

    // Remove a vehicle entry by ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a vehicle entry", description = "Delete a specific vehicle entry by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted vehicle"),
        @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID id) {
        service.deleteVehicleById(id);
        return ResponseEntity.noContent().build();
    }
}
