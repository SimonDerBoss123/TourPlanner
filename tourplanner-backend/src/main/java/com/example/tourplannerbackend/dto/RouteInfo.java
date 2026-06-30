package com.example.tourplannerbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RouteInfo {
    private double distance;
    private double duration;
    private String geometry;
}
