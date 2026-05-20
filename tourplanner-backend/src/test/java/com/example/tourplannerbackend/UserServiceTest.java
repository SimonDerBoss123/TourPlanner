package com.example.tourplannerbackend;

import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.dto.UserResponse;
import com.example.tourplannerbackend.repository.UserRepository;
import com.example.tourplannerbackend.security.JwtUtil;
import com.example.tourplannerbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_savesUserAndReturnsResponse() {
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("simon");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken("simon")).thenReturn("token123");

        UserResponse result = userService.registerUser("simon", "123456");

        assertEquals("simon", result.getUsername());
        assertEquals("token123", result.getToken());
        assertEquals(1L, result.getId());
    }

    @Test
    void registerUser_generatesToken() {
        User savedUser = new User();
        savedUser.setUsername("simon");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken("simon")).thenReturn("mytoken");

        UserResponse result = userService.registerUser("simon", "123456");

        assertNotNull(result.getToken());
        assertEquals("mytoken", result.getToken());
    }

    @Test
    void loginUser_returnsResponseWithToken() {
        User user = new User();
        user.setId(1L);
        user.setUsername("simon");
        user.setPassword("123456");

        when(userRepository.findByUsername("simon")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken("simon")).thenReturn("token123");

        UserResponse result = userService.loginUser("simon", "123456");

        assertEquals("simon", result.getUsername());
        assertEquals("token123", result.getToken());
    }

    @Test
    void loginUser_throwsExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.loginUser("unknown", "123456"));
    }

    @Test
    void loginUser_throwsExceptionWhenWrongPassword() {
        User user = new User();
        user.setUsername("simon");
        user.setPassword("richtig");

        when(userRepository.findByUsername("simon")).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.loginUser("simon", "falsch"));
    }

    @Test
    void loginUser_throwsExceptionWhenPasswordIsEmpty() {
        User user = new User();
        user.setUsername("simon");
        user.setPassword("123456");

        when(userRepository.findByUsername("simon")).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.loginUser("simon", ""));
    }
}