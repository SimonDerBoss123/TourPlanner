package com.example.tourplannerbackend.repository;

import com.example.tourplannerbackend.domain.TourLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourLogRepository extends JpaRepository<TourLog, Long> {
    List<TourLog> findByTourId(Long tourId);


}
