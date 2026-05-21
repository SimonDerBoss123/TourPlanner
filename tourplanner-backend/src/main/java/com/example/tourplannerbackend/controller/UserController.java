package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.tourplannerbackend.dto.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody RegisterRequest request){
        return userService.registerUser(request.getUsername(),request.getPassword());
    }

    @PostMapping("/login")
    public UserResponse loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request.getUsername(),request.getPassword());
    }

}
