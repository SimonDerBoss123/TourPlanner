package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.security.JwtUtil;
import com.example.tourplannerbackend.service.TourService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "http://localhost:3000")
public class TourController {

    private final TourService tourService;
    private final JwtUtil jwtUtil;

    public TourController(TourService tourService, JwtUtil jwtUtil) {
        this.tourService = tourService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour, @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return tourService.createTour(tour,username);
    }

    @DeleteMapping("/{id}")
    public void deleteTour(@PathVariable Long id){
        tourService.deleteTour(id);
    }

    @PutMapping("/{id}")
    public Tour updateTour(@PathVariable Long id, @RequestBody Tour tour){
        return tourService.updateTour(id,tour);
    }

    @GetMapping("/{id}")
    public Tour getTourById(@PathVariable Long id){
        return tourService.getTourById(id);
    }
}