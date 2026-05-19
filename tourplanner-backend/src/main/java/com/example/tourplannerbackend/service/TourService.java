package com.example.tourplannerbackend.service;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.repository.TourRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour createTour(Tour tour) {
        return tourRepository.save(tour);
    }

    public void deleteTour(Long id){
        tourRepository.deleteById(id);
    }

    public Tour updateTour(Long id, Tour tour){
       tour.setId(id);
       return tourRepository.save(tour);
    }

    public Tour getTourById(Long id){
        return tourRepository.findById(id).orElseThrow();
    }

}