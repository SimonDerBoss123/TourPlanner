package com.example.tourplannerbackend.service;

import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.dto.UserResponse;
import com.example.tourplannerbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public UserResponse registerUser(String username, String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        User savedUser = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(savedUser.getId());
        response.setUsername(savedUser.getUsername());
        return response;
    }

    public UserResponse loginUser(String username, String password){
        User user = userRepository.findByUsername(username).
                orElseThrow(()-> new RuntimeException("User nicht gefunden"));

        if(!user.getPassword().equals(password)) {
            throw new RuntimeException("Falsches Passwort");
        }

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }
}
