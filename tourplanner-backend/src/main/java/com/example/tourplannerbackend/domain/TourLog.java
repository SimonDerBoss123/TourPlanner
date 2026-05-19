package com.example.tourplannerbackend.domain;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Data

public class TourLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private Integer difficulty;
    private Integer rating;
    private Double totalDistance;
    private Integer totalTime;
    private LocalDateTime dateTime;

    @ManyToOne
    private Tour tour;

}
