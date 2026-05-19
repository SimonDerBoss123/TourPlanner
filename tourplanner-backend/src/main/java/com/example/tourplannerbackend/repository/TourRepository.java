package com.example.tourplannerbackend.repository;

import com.example.tourplannerbackend.domain.Tour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<Tour, Long> {

}

