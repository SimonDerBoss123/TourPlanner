package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.service.TourLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/tours/{tourId}/tourlogs")
@CrossOrigin(origins = "http://localhost:5173")
public class TourLogController {

    private final TourLogService tourLogService;

    public TourLogController(TourLogService tourLogService){
        this.tourLogService = tourLogService;
    }

    @GetMapping
    public List<TourLog> getAllTourLogs(@PathVariable Long tourId){
        return tourLogService.getTourLogsByTourId(tourId);
    }

    @PostMapping
    public TourLog createTourLog(@PathVariable Long tourId, @RequestBody TourLog tourLog){
        return tourLogService.createTourLog(tourId,tourLog);
    }

    @DeleteMapping("/{id}")
    public void deleteTourLog(@PathVariable Long id){
        tourLogService.deleteTourLog(id);
    }

    @PutMapping("/{id}")
    public TourLog updateTourLog(@PathVariable Long id, @RequestBody TourLog tourLog){
        return tourLogService.updateTourLog(id,tourLog);
    }

    @GetMapping("/{id}")
    public TourLog getTourLogById(@PathVariable Long id){
        return tourLogService.getTourLogById(id);
    }

}
