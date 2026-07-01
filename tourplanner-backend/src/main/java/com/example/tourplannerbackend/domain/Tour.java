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
}
