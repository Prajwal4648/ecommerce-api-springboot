package com.prajwal.ecommerce_api.controller;

import com.prajwal.ecommerce_api.dto.*;
import com.prajwal.ecommerce_api.model.Role;
import com.prajwal.ecommerce_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "Operations related to user management and profiles")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users (non-paginated)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of users",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get all users (paginated)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Paginated list of users",
                            content = @Content(mediaType = "application/json"))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserProfileDTO>> getAllUsersPaginated(Pageable pageable) {
        Page<UserProfileDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Get a user by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Delete a user by ID",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get authenticated user's profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User profile data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserProfileDTO> getUserProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileDTO profile = userService.getUserProfile(username);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Update authenticated user's profile",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Updated user profile",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            })
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> updateUserProfile(@Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileDTO updatedProfile = userService.updateUserProfile(username, userUpdateDTO);
        return ResponseEntity.ok(updatedProfile);
    }

    @Operation(summary = "Update a user's role",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User role updated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid role"),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PutMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> updateUserRole(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoleDTO updateRoleDTO) {
        Role role = Role.valueOf(updateRoleDTO.getRole().toUpperCase());
        UserProfileDTO updatedUser = userService.updateUserRole(id, role);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Toggle user's enabled status",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User enabled status toggled",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserProfileDTO.class))),
                    @ApiResponse(responseCode = "403", description = "Forbidden")
            })
    @PutMapping("/{id}/enabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileDTO> toggleUserEnabled(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEnabledDTO updateEnabledDTO) {
        UserProfileDTO updatedUser = userService.toggleUserEnabled(id, updateEnabledDTO.isEnabled());
        return ResponseEntity.ok(updatedUser);
    }
}
