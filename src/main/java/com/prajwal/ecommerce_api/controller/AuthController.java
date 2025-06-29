package com.prajwal.ecommerce_api.controller;

import com.prajwal.ecommerce_api.dto.LoginRequestDTO;
import com.prajwal.ecommerce_api.dto.LoginResponseDTO;
import com.prajwal.ecommerce_api.dto.RegisterRequestDTO;
import com.prajwal.ecommerce_api.dto.UserDTO;
import com.prajwal.ecommerce_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ Register a new user
    @Operation(summary = "Register a new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
            })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        UserDTO user = authService.registerUser(registerRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    // ✅ Login with username or email
    @Operation(summary = "Login user and get JWT token",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User logged in successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = authService.loginUser(loginRequest);
        return ResponseEntity.ok(response);
    }
}
