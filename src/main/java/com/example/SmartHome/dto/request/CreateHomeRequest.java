package com.example.SmartHome.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CreateHomeRequest {
    @NotBlank(message = "Home name is required")
    @Size(min = 3, max = 25, message = "Invalid home name length!")
    private String homeName;

    @NotBlank(message = "Home address is required")
    @Size(min = 3, max = 100, message = "Invalid home address length!")
    private String homeAddress;
}
