package com.prajwal.ecommerce_api.service;

import com.prajwal.ecommerce_api.dto.*;
import com.prajwal.ecommerce_api.exception.UserNotFoundException;
import com.prajwal.ecommerce_api.model.Role;
import com.prajwal.ecommerce_api.model.User;
import com.prajwal.ecommerce_api.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileDTO getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return modelMapper.map(user, UserProfileDTO.class);
    }

    @Override
    @Transactional
    public UserProfileDTO updateUserProfile(String username, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        if (userUpdateDTO.getFirstName() != null)
            user.setFirstName(userUpdateDTO.getFirstName());

        if (userUpdateDTO.getLastName() != null)
            user.setLastName(userUpdateDTO.getLastName());

        if (userUpdateDTO.getEmail() != null &&
                !userUpdateDTO.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(userUpdateDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setEmail(userUpdateDTO.getEmail());
        userRepository.save(user);
        return modelMapper.map(user, UserProfileDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserProfileDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserProfileDTO.class));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileDTO updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        user.setRole(role);
        userRepository.save(user);
        return modelMapper.map(user, UserProfileDTO.class);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileDTO toggleUserEnabled(Long userId, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        user.setEnabled(enabled);
        userRepository.save(user);
        return modelMapper.map(user, UserProfileDTO.class);
    }
}
