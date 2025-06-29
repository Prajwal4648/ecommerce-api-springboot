package com.prajwal.ecommerce_api.service;

import com.prajwal.ecommerce_api.dto.*;
import com.prajwal.ecommerce_api.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    void deleteUser(Long id);
    UserProfileDTO getUserProfile(String username);
    UserProfileDTO updateUserProfile(String username, UserUpdateDTO userUpdateDTO);
    Page<UserProfileDTO> getAllUsers(Pageable pageable);
    UserProfileDTO updateUserRole(Long userId, Role role);
    UserProfileDTO toggleUserEnabled(Long userId, boolean enabled);
}

