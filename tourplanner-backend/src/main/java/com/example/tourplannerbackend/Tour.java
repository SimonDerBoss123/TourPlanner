package com.example.tourplannerbackend;

import jakarta.persistence.*;
import lombok.Data;

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
}
