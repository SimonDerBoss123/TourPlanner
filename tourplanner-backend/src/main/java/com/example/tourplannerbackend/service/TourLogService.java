package com.example.tourplannerbackend.service;

import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.repository.TourLogRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourLogService {

    private final TourLogRepository tourLogRepository;

    public TourLogService(TourLogRepository tourLogRepository){
        this.tourLogRepository = tourLogRepository;
    }

    public List<TourLog> getAllTourLogs(){
        return tourLogRepository.findAll();
    }

    public TourLog createTourLog(TourLog tourLog){
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




}
