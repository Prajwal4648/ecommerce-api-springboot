package com.prajwal.ecommerce_api.security;

import com.prajwal.ecommerce_api.model.User;
import com.prajwal.ecommerce_api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        // Try username, then email
        User user = userRepository
                .findByUsername(identifier)
                .or(() -> userRepository.findByEmail(identifier))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + identifier)
                );
        return new UserPrincipal(user);
    }
}
