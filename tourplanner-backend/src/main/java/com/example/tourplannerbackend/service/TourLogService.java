package com.example.tourplannerbackend.service;

import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.repository.TourLogRepository;
import com.example.tourplannerbackend.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class TourLogService {

    private final TourLogRepository tourLogRepository;
    private final TourRepository tourRepository;
    private static final Logger logger = LoggerFactory.getLogger(TourLogService.class);


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
        logger.info("TourLog created for tour: {}", tourId);
        return tourLogRepository.save(tourLog);
    }

    public void deleteTourLog(Long id){
        tourLogRepository.deleteById(id);
        logger.info("TourLog deleted: {}", id);
    }

    public TourLog updateTourLog(Long id, TourLog updatedLog){
        TourLog existing = tourLogRepository.findById(id).orElseThrow();
        updatedLog.setId(id);
        updatedLog.setTour(existing.getTour());
        logger.info("TourLog updated: {}", id);
        return tourLogRepository.save(updatedLog);
    }

    public TourLog getTourLogById(Long id){
        return tourLogRepository.findById(id).orElseThrow();
    }

    public List<TourLog> getTourLogsByTourId(Long tourId){
        return tourLogRepository.findByTourId(tourId);
    }




}
