package com.zagrebopenfestivals.service;

import com.zagrebopenfestivals.dto.response.UserResponse;

public interface UserService {

    UserResponse getByUsername(String username);
}