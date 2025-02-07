package com.example.vehicleservice.service;

import com.example.vehicleservice.model.VehicleType;
import com.example.vehicleservice.model.VehicleTypeEnum;
import com.example.vehicleservice.repository.VehicleTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class VehicleTypeServiceIT {

    @Autowired
    private VehicleTypeRepository repository;

    private VehicleType vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new VehicleType(
            UUID.randomUUID(), // id
            1L,                // version
            "Toyota",          // manufacturer
            "Corolla",         // model
            2020,              // year
            VehicleTypeEnum.SEDAN, // type
            25000.0,           // price
            "A reliable and fuel-efficient sedan." // description
        );
        repository.save(vehicle);
    }

    @Test
    void testSaveAndRetrieveVehicleType() {
        Optional<VehicleType> foundVehicle = repository.findById(vehicle.getId());

        assertThat(foundVehicle).isPresent();
        assertThat(foundVehicle.get().getManufacturer()).isEqualTo("Toyota");
    }

    @Test
    void testFindAllVehicles() {
        List<VehicleType> vehicles = repository.findAll();
        assertThat(vehicles).isNotEmpty();
    }

    @Test
    void testDeleteVehicle() {
        repository.deleteById(vehicle.getId());
        Optional<VehicleType> deletedVehicle = repository.findById(vehicle.getId());

        assertThat(deletedVehicle).isEmpty();
    }
}

