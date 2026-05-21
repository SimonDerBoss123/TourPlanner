package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.security.JwtUtil;
import com.example.tourplannerbackend.service.UserService;
import io.jsonwebtoken.Jwt;
import org.antlr.v4.runtime.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.tourplannerbackend.dto.*;


@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        if(jwtUtil.validateToken(token)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(401).build();
    }
}
