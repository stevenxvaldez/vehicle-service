package com.example.vehicleservice.controller;

import com.example.vehicleservice.model.VehicleType;
import com.example.vehicleservice.model.VehicleTypeEnum;
import com.example.vehicleservice.service.VehicleTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VehicleTypeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleTypeService service;

    @InjectMocks
    private VehicleTypeController controller;

    private VehicleType sampleVehicle;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        sampleVehicle = new VehicleType(
            UUID.randomUUID(), // id
            1L,                // version
            "Toyota",          // manufacturer
            "Corolla",         // model
            2020,              // year
            VehicleTypeEnum.SEDAN, // type
            25000.0,           // price
            "A reliable and fuel-efficient sedan." // description
        );
    }

    @Test
    void testSaveOrUpdateVehicleType() throws Exception {
        when(service.saveOrUpdateVehicleType(any(VehicleType.class))).thenReturn(sampleVehicle);

        String requestBody = """
            {
                "manufacturer": "Toyota",
                "model": "Corolla",
                "type": "SEDAN",
                "year": 2022
            }
        """;

        mockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturer").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"));
    }

    @Test
    void testGetAllVehicleTypes() throws Exception {
        when(service.getAllVehicleTypes(any(), any(), any(), any(), any())).thenReturn(Collections.singletonList(sampleVehicle));

        mockMvc.perform(get("/api/vehicles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].manufacturer").value("Toyota"));
    }

    @Test
    void testGetVehicleById() throws Exception {
        UUID id = sampleVehicle.getId();
        when(service.getVehicleById(id)).thenReturn(sampleVehicle);

        mockMvc.perform(get("/api/vehicles/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.manufacturer").value("Toyota"));
    }

    @Test
    void testDeleteVehicle() throws Exception {
        UUID id = sampleVehicle.getId();
        doNothing().when(service).deleteVehicleById(id);

        mockMvc.perform(delete("/api/vehicles/{id}", id))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteVehicleById(id);
    }
}
