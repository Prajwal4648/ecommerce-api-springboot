package com.prajwal.ecommerce_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank(message = "Username or email is required")
    private String identifier; // new name, can be email or username

    @NotBlank(message = "Password is required")
    private String password;
}
