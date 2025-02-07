package com.example.vehicleservice.mapper;

import com.example.vehicleservice.dto.UserProfileDto;
import com.example.vehicleservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(user.getEmail(), user.getUsername());
    }

}