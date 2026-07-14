package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.response.UserResponse;
import com.zagrebopenfestivals.entity.User;
import com.zagrebopenfestivals.exception.ResourceNotFoundException;
import com.zagrebopenfestivals.mapper.UserMapper;
import com.zagrebopenfestivals.repository.UserRepository;
import com.zagrebopenfestivals.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik ne postoji: " + username));
        return userMapper.toResponse(user);
    }
}