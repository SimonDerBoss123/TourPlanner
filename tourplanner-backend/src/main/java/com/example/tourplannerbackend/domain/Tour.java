package com.example.tourplannerbackend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data

public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String fromLocation;
    private String toLocation;
    private String transportType;
    private Double tourDistance;
    private Double estimatedTime;

    //erlaubt beliebig langen text
    @Column(columnDefinition = "TEXT")
    private String geometry;

    @JsonIgnore
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourLog> tourLogs;
    @ManyToOne
    private User user;

    public int getPopularity() {
        if (tourLogs == null) return 0;
        return tourLogs.size();
    }

    public boolean getChildFriendliness() {
        if (tourLogs == null || tourLogs.isEmpty()) return false;

        double avgDifficulty = tourLogs.stream()
                .mapToInt(TourLog::getDifficulty)
                .average()
                .orElse(5);

        boolean easyDifficulty = avgDifficulty <= 2;
        boolean shortDistance = tourDistance != null && tourDistance <= 10;
        boolean shortTime = estimatedTime != null && estimatedTime <= 60;

        return easyDifficulty && shortDistance && shortTime;
    }
}
