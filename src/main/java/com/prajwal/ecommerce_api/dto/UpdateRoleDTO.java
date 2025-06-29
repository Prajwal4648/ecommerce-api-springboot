package com.prajwal.ecommerce_api.dto;

import com.prajwal.ecommerce_api.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRoleDTO {
    @NotBlank(message = "Role is required")
    private String role;
}