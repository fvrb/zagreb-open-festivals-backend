package com.zagrebopenfestivals.mapper;

import com.zagrebopenfestivals.dto.response.UserResponse;
import com.zagrebopenfestivals.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }
}
