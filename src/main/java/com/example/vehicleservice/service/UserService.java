package com.example.vehicleservice.service;

import static org.springframework.http.HttpStatus.GONE;

import com.example.vehicleservice.model.User;
import com.example.vehicleservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(GONE, "The user account has been deleted or inactivated"));
    }
}