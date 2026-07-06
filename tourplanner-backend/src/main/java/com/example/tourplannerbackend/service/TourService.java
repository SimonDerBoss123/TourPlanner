package com.example.tourplannerbackend.service;
import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.domain.User;
import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.dto.RouteInfo;
import com.example.tourplannerbackend.repository.TourLogRepository;
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
    private final OpenRouteService openRouteService;
    private final TourLogRepository tourLogRepository;

    public TourService(TourRepository tourRepository, JwtUtil jwtUtil, UserRepository userRepository, OpenRouteService openRouteService, TourLogRepository tourLogRepository) {
        this.tourRepository = tourRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.openRouteService = openRouteService;
        this.tourLogRepository = tourLogRepository;
    }

    public List<Tour> getAllTours(String username) {
        logger.info("Getting all tours for user '{}'", username);
        List<Tour> tours = tourRepository.findByUserUsername(username);
        tours.forEach(tour -> tour.setPopularity(tourLogRepository.countByTourId(tour.getId())));
        return tours;
    }

    public Tour createTour(Tour tour, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User nicht gefunden"));
        tour.setUser(user);

        RouteInfo routeInfo = openRouteService.getRouteInfo(tour.getFromLocation(), tour.getToLocation(), tour.getTransportType());
        tour.setTourDistance(routeInfo.getDistance());
        tour.setEstimatedTime(routeInfo.getDuration());
        tour.setGeometry(routeInfo.getGeometry());

        Tour savedTour = tourRepository.save(tour);
        logger.info("Tour '{}' created for user '{}'", savedTour.getName(), username);
        return savedTour;
    }

    public void deleteTour(Long id, String username){
        Tour tour = tourRepository.findById(id).orElseThrow();
        if (!tour.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }

        tourRepository.deleteById(id);
        logger.info("Tour deleted: {} by: {}", id, username);
    }

    public Tour updateTour(Long id, Tour tour, String username){
        Tour existing = tourRepository.findById(id).orElseThrow();
        if (!existing.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Unauthorized");
        }
        tour.setId(id);
        tour.setUser(existing.getUser());

        RouteInfo routeInfo = openRouteService.getRouteInfo(tour.getFromLocation(), tour.getToLocation(), tour.getTransportType());
        tour.setTourDistance(routeInfo.getDistance());
        tour.setEstimatedTime(routeInfo.getDuration());
        tour.setGeometry(routeInfo.getGeometry());

        logger.info("Tour updated: {} by: {}", id, username);
        return tourRepository.save(tour);
    }

    public Tour getTourById(Long id) {
        Tour tour = tourRepository.findById(id).orElseThrow();
        int logCount = tourLogRepository.countByTourId(id);
        tour.setPopularity(logCount);

        //child friendliness ausrechnen
        List<TourLog> logs = tourLogRepository.findByTourId(id);
        boolean childFriendly = !logs.isEmpty()
                && logs.stream().mapToInt(TourLog::getDifficulty).average().orElse(5) <= 2
                && tour.getTourDistance() != null && tour.getTourDistance() <= 10
                && tour.getEstimatedTime() != null && tour.getEstimatedTime() <= 60;
        tour.setChildFriendliness(childFriendly);

        return tour;
    }
}