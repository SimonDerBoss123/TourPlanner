package com.example.tourplannerbackend;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.dto.RouteInfo;
import com.example.tourplannerbackend.repository.TourRepository;
import com.example.tourplannerbackend.repository.UserRepository;
import com.example.tourplannerbackend.security.JwtUtil;
import com.example.tourplannerbackend.service.OpenRouteService;
import com.example.tourplannerbackend.service.TourService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @Mock
    private TourRepository tourRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private OpenRouteService openRouteService;
    @InjectMocks
    private TourService tourService;

    @Test
    void getAllTours_returnsToursForUser() {
        Tour tour = new Tour();
        tour.setName("Wienerwald");
        when(tourRepository.findByUserUsername("simon")).thenReturn(List.of(tour));

        List<Tour> result = tourService.getAllTours("simon");

        assertEquals(1, result.size());
        assertEquals("Wienerwald", result.get(0).getName());
    }

    @Test
    void getAllTours_returnsEmptyListWhenNoTours() {
        when(tourRepository.findByUserUsername("simon")).thenReturn(List.of());

        List<Tour> result = tourService.getAllTours("simon");

        assertEquals(0, result.size());
    }

    @Test
    void createTour_savesTourWithUser() {
        User user = new User();
        user.setUsername("simon");

        Tour tour = new Tour();
        tour.setName("Wienerwald");

        RouteInfo routeInfo = new RouteInfo(10.0, 60.0, "geometry");

        when(userRepository.findByUsername("simon")).thenReturn(Optional.of(user));
        when(openRouteService.getRouteInfo(any(), any(), any())).thenReturn(routeInfo);
        when(tourRepository.save(tour)).thenReturn(tour);

        Tour result = tourService.createTour(tour, "simon");

        assertEquals("simon", result.getUser().getUsername());
        verify(tourRepository, times(1)).save(tour);
    }

    @Test
    void createTour_throwsExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tourService.createTour(new Tour(), "unknown"));
    }

    @Test
    void deleteTour_callsDeleteById() {
        tourService.deleteTour(1L);
        verify(tourRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTourById_returnsTour() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setName("Wienerwald");

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));

        Tour result = tourService.getTourById(1L);

        assertEquals("Wienerwald", result.getName());
    }

    @Test
    void getTourById_throwsExceptionWhenNotFound() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> tourService.getTourById(99L));
    }

    @Test
    void updateTour_setsIdAndSaves() {
        Tour existing = new Tour();
        existing.setId(1L);

        Tour tour = new Tour();
        tour.setName("Neu");

        RouteInfo routeInfo = new RouteInfo(10.0, 60.0, "geometry");

        when(tourRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(openRouteService.getRouteInfo(any(), any(), any())).thenReturn(routeInfo);
        when(tourRepository.save(any(Tour.class))).thenReturn(tour);

        Tour result = tourService.updateTour(1L, tour);

        assertEquals(1L, result.getId());
        verify(tourRepository, times(1)).save(tour);
    }
}