package com.example.tourplannerbackend.service;
import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.repository.TourRepository;
import com.example.tourplannerbackend.repository.UserRepository;
import com.example.tourplannerbackend.security.JwtUtil;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class TourService {

    private static final Logger logger = LoggerFactory.getLogger(TourService.class);
    private final TourRepository tourRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public TourService(TourRepository tourRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.tourRepository = tourRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public List<Tour> getAllTours(String username) {
        logger.info("Getting all tours for user '{}'", username);
        return tourRepository.findByUserUsername(username);
    }

    public Tour createTour(Tour tour, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));
        tour.setUser(user);
        Tour savedTour = tourRepository.save(tour);
        logger.info("Tour '{}' created for user '{}'", savedTour.getName(), username);
        return savedTour;
    }

    public void deleteTour(Long id){
        tourRepository.deleteById(id);
        logger.info("Tour deleted: {}", id);
    }

    public Tour updateTour(Long id, Tour tour){
        tour.setId(id);
        logger.info("Tour updated: {}", id);
        return tourRepository.save(tour);
    }

    public Tour getTourById(Long id){
        return tourRepository.findById(id).orElseThrow();
    }

}