package com.example.tourplannerbackend.service;

import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.dto.UserResponse;
import com.example.tourplannerbackend.repository.UserRepository;
import com.example.tourplannerbackend.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserResponse registerUser(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(username);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        response.setToken(token);
        return response;
    }

    public UserResponse loginUser(String username, String password){
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User nicht gefunden"));

        if(!user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Falsches Passwort");
        }

        String token = jwtUtil.generateToken(username);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setToken(token);
        return userResponse;
    }
}
