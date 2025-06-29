package com.prajwal.ecommerce_api.service;

import com.prajwal.ecommerce_api.dto.LoginRequestDTO;
import com.prajwal.ecommerce_api.dto.LoginResponseDTO;
import com.prajwal.ecommerce_api.dto.RegisterRequestDTO;
import com.prajwal.ecommerce_api.dto.UserDTO;
import com.prajwal.ecommerce_api.exception.InvalidCredentialsException;
import com.prajwal.ecommerce_api.exception.UserAlreadyExistsException;
import com.prajwal.ecommerce_api.model.Role;
import com.prajwal.ecommerce_api.model.User;
import com.prajwal.ecommerce_api.repository.UserRepository;
import com.prajwal.ecommerce_api.security.JwtTokenProvider;
import com.prajwal.ecommerce_api.security.UserPrincipal;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, ModelMapper modelMapper,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public UserDTO registerUser(RegisterRequestDTO registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists: " + registerRequest.getUsername());
        }

        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        System.out.println("Encoded password = " + user.getPassword());

        user.setRole(Role.valueOf(registerRequest.getRole().toUpperCase()));
        user.setEnabled(true);
        user = userRepository.save(user);

        return modelMapper.map(user, UserDTO.class);
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO loginUser(LoginRequestDTO loginRequest) {
        try {
            // Delegate entirely to AuthenticationManager
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getIdentifier(),
                            loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
            String token = jwtTokenProvider.generateToken(principal);
            UserDTO userDTO = new UserDTO(principal.getId(), principal.getUsername(), principal.getAuthorities().iterator().next().getAuthority());

            return new LoginResponseDTO(token, userDTO);

        } catch (InvalidCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username/email or password");
        }
    }


}
