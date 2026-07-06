package com.example.tourplannerbackend.controller;

import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.service.TourLogService;
import com.example.tourplannerbackend.service.TourService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.tourplannerbackend.security.JwtUtil;



@RestController
@RequestMapping("/api/tours/{tourId}/tourlogs")
@CrossOrigin(origins = "http://localhost:5173")
public class TourLogController {

    private final TourLogService tourLogService;
    private final JwtUtil jwtUtil;
    private final TourService tourService;


    public TourLogController(TourLogService tourLogService, JwtUtil jwtUtil, TourService tourService){
        this.tourLogService = tourLogService;
        this.jwtUtil = jwtUtil;
        this.tourService = tourService;
    }

    @GetMapping
    public List<TourLog> getAllTourLogs(@PathVariable Long tourId){
        return tourLogService.getTourLogsByTourId(tourId);
    }

    @PostMapping
    public TourLog createTourLog(@PathVariable Long tourId, @RequestBody TourLog tourLog, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return tourLogService.createTourLog(tourId,tourLog, username);
    }

    @DeleteMapping("/{id}")
    public void deleteTourLog(@PathVariable Long tourId, @PathVariable Long id, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        tourLogService.deleteTourLog(id,tourId, username);
    }

    @PutMapping("/{id}")
    public TourLog updateTourLog(@PathVariable Long tourId, @PathVariable Long id, @RequestBody TourLog tourLog, @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return tourLogService.updateTourLog(id,tourLog,username);
    }

    @GetMapping("/{id}")
    public TourLog getTourLogById(@PathVariable Long id){
        return tourLogService.getTourLogById(id);
    }

}
