package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.service.TourService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "http://localhost:3000")
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    @GetMapping
    public List<Tour> getAllTours() {
        return tourService.getAllTours();
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour) {
        return tourService.createTour(tour);
    }
}