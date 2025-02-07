package com.example.vehicleservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private Long version;  // This field is used for optimistic locking

    @NotBlank(message = "Manufacturer is required")
    @Column(nullable = false)
    private String manufacturer;

    @NotBlank(message = "Model is required")
    @Column(nullable = false)
    private String model;

    @Min(value = 1886, message = "Year must be 1886 or later")
    @Max(value = 2025, message = "Year cannot be in the future")
    @Column(nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Type is required")
    @Column(nullable = false)
    private VehicleTypeEnum type;

    @PositiveOrZero(message = "Price must be non-negative")
    @Column(nullable = false)
    private double price;

    @Size(max = 500, message = "Description can't be longer than 500 characters")
    private String description;
}
