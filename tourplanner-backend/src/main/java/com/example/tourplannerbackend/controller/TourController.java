package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.dto.TourDto;
import com.example.tourplannerbackend.mapper.TourMapper;
import com.example.tourplannerbackend.security.JwtUtil;
import com.example.tourplannerbackend.service.TourService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "http://localhost:5173")
public class TourController {

    private final TourService tourService;
    private final JwtUtil jwtUtil;
    private final TourMapper tourMapper;

    public TourController(TourService tourService, JwtUtil jwtUtil, TourMapper tourMapper) {
        this.tourService = tourService;
        this.jwtUtil = jwtUtil;
        this.tourMapper = tourMapper;
    }

    @GetMapping
    public List<TourDto> getAllTours(@RequestHeader ("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return tourService.getAllTours(username).stream().map(tourMapper::toDto).toList();
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour, @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return tourService.createTour(tour,username);
    }

    @DeleteMapping("/{id}")
    public void deleteTour(@PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        tourService.deleteTour(id, username);
    }

    @PutMapping("/{id}")
    public Tour updateTour(@PathVariable Long id, @RequestBody Tour tour, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return tourService.updateTour(id,tour, username);
    }

    @GetMapping("/{id}")
    public Tour getTourById(@PathVariable Long id){
        return tourService.getTourById(id);
    }
}