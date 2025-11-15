package com.example.bankcards.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bankcards.repository.UserRepository;

@Service
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    public CustomUserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).map(CustomUserDetails::new).orElseThrow(
            () -> new UsernameNotFoundException(username)
        );
    }

    
}
