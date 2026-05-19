package com.example.tourplannerbackend.service;
import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.repository.TourRepository;
import com.example.tourplannerbackend.repository.UserRepository;
import com.example.tourplannerbackend.security.JwtUtil;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    public TourService(TourRepository tourRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.tourRepository = tourRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public Tour createTour(Tour tour, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));
        tour.setUser(user);
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