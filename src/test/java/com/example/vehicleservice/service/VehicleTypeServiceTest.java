package com.example.vehicleservice.service;

import com.example.vehicleservice.model.VehicleType;
import com.example.vehicleservice.model.VehicleTypeEnum;
import com.example.vehicleservice.repository.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleTypeServiceTest {

    @Mock
    private VehicleTypeRepository repository;

    @InjectMocks
    private VehicleTypeService service;

    private VehicleType vehicle1;
    private VehicleType vehicle2;

    @BeforeEach
    void setUp() {
        vehicle1 = new VehicleType(
            UUID.randomUUID(), // id
            1L,                // version
            "Toyota",          // manufacturer
            "Corolla",         // model
            2020,              // year
            VehicleTypeEnum.SEDAN, // type
            25000.0,           // price
            "A reliable and fuel-efficient sedan." // description
        );
        vehicle2 = new VehicleType(
            UUID.randomUUID(), // id
            1L,                // version
            "Honda",          // manufacturer
            "Civic",         // model
            2020,              // year
            VehicleTypeEnum.COUPE, // type
            26000.0,           // price
            "A reliable and fuel-efficient car." // description
        );
    }

    @Test
    void testSaveOrUpdateVehicleType_NewVehicle() {
        when(repository.save(any(VehicleType.class))).thenReturn(vehicle1);

        VehicleType savedVehicle = service.saveOrUpdateVehicleType(vehicle1);

        assertNotNull(savedVehicle);
        assertEquals("Toyota", savedVehicle.getManufacturer());
        verify(repository, times(1)).save(vehicle1);
    }

    @Test
    void testGetAllVehicleTypes() {
        List<VehicleType> vehicles = Arrays.asList(vehicle1, vehicle2);
        Page<VehicleType> page = new PageImpl<>(vehicles);
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        List<VehicleType> result = service.getAllVehicleTypes(null, null, null, "year", "asc");

        assertEquals(2, result.size());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testGetVehicleById_Found() {
        when(repository.findById(vehicle1.getId())).thenReturn(Optional.of(vehicle1));

        VehicleType foundVehicle = service.getVehicleById(vehicle1.getId());

        assertNotNull(foundVehicle);
        assertEquals("Toyota", foundVehicle.getManufacturer());
        verify(repository, times(1)).findById(vehicle1.getId());
    }

    @Test
    void testGetVehicleById_NotFound() {
        UUID randomId = UUID.randomUUID();
        when(repository.findById(randomId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> service.getVehicleById(randomId));
        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    void testDeleteVehicleById() {
        doNothing().when(repository).deleteById(vehicle1.getId());

        service.deleteVehicleById(vehicle1.getId());

        verify(repository, times(1)).deleteById(vehicle1.getId());
    }
}
