package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.service.TourLogService;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/tourLogs")
@CrossOrigin(origins = "http://localhost:3000")
public class TourLogController {

    private final TourLogService tourLogService;

    public TourLogController(TourLogService tourLogService){
        this.tourLogService = tourLogService;
    }

    @GetMapping
    public List<TourLog> getAllTourLogs(){
        return tourLogService.getAllTourLogs();
    }

    @PostMapping
    public TourLog createTourLog(@RequestBody TourLog tourLog){
        return tourLogService.createTourLog(tourLog);
    }
}
