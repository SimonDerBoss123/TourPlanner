package com.example.tourplannerbackend.service;

import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.repository.TourLogRepository;
import com.example.tourplannerbackend.repository.TourRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourLogService {

    private final TourLogRepository tourLogRepository;
    private final TourRepository tourRepository;

    public TourLogService(TourLogRepository tourLogRepository, TourRepository tourRepository){
        this.tourLogRepository = tourLogRepository;
        this.tourRepository = tourRepository;
    }

    public List<TourLog> getAllTourLogs(){
        return tourLogRepository.findAll();
    }

    public TourLog createTourLog(Long tourId, TourLog tourLog){
        Tour tour = tourRepository.findById(tourId).orElseThrow(()-> new RuntimeException("Tour nciht gefunden"));
        tourLog.setTour(tour);
        return tourLogRepository.save(tourLog);
    }

    public void deleteTourLog(Long id){
        tourLogRepository.deleteById(id);
    }

    public TourLog updateTourLog(Long id, TourLog tourLog){
        tourLog.setId(id);
        return tourLogRepository.save(tourLog);
    }

    public TourLog getTourLogById(Long id){
        return tourLogRepository.findById(id).orElseThrow();
    }

    public List<TourLog> getTourLogsByTourId(Long tourId){
        return tourLogRepository.findByTourId(tourId);
    }




}
