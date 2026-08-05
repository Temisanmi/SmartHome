package com.example.SmartHome.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUpdateHomeRequest {
    @NotBlank(message = "Home name is required")
    @Size(max = 100, message = "Home name is too long")
    private String name;

    @NotBlank(message = "Home address is required")
    @Size(max = 200, message = "Home address is too long")
    private String address;

    @NotNull(message = "Tariff is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Tariff cannot be negative")
    private Double tariffPerKwh;
}
