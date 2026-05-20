package com.example.tourplannerbackend.repository;

import com.example.tourplannerbackend.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    List<Tour> findByUserUsername(String username);
}

