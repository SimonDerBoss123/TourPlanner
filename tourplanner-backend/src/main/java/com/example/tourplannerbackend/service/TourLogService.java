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

    public TourLog createTourLog(Long tourId, TourLog tourLog, String username){
        Tour tour = tourRepository.findById(tourId).orElseThrow(()-> new RuntimeException("Tour nciht gefunden"));

        if (!tour.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        tourLog.setTour(tour);
        logger.info("TourLog created for tour: {} by: {}", tourId, username);
        return tourLogRepository.save(tourLog);
    }

    public void deleteTourLog(Long id, Long tourId, String username){

        Tour tour = tourRepository.findById(tourId).orElseThrow(()-> new RuntimeException("Tour nciht gefunden"));
        if (!tour.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        tourLogRepository.deleteById(id);
        logger.info("TourLog deleted: {} by: {}", id, username);
    }

    public TourLog updateTourLog(Long id, TourLog updatedLog, String username){
        TourLog existing = tourLogRepository.findById(id).orElseThrow();
        if (!existing.getTour().getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        updatedLog.setId(id);
        updatedLog.setTour(existing.getTour());
        logger.info("TourLog updated: {} by: {}", id, username);
        return tourLogRepository.save(updatedLog);
    }

    public TourLog getTourLogById(Long id){
        return tourLogRepository.findById(id).orElseThrow();
    }

    public List<TourLog> getTourLogsByTourId(Long tourId){
        return tourLogRepository.findByTourId(tourId);
    }




}
