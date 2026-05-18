package com.example.tourplannerbackend;


import org.springframework.web.bind.annotation.*;
import com.example.tourplannerbackend.TourRepository;

import java.util.List;

@RestController
@RequestMapping("/api/tours")
@CrossOrigin(origins = "http://localhost:3000")

public class TourController {
    private final TourRepository tourRepository;

    public TourController(TourRepository tourRepository){
        this.tourRepository = tourRepository;
    }

    @GetMapping
    public List<Tour> getAllTours(){
        return tourRepository.findAll();
    }

    @PostMapping
    public Tour createTour(@RequestBody Tour tour){
        return tourRepository.save(tour);
    }

}
