package com.example.tourplannerbackend.mapper;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.dto.TourDto;
import org.springframework.stereotype.Component;

@Component
public class TourMapper extends AbstractMapper<Tour, TourDto> {

    @Override
    public TourDto toDto(Tour tour) {
        TourDto dto = new TourDto();
        dto.setId(tour.getId());
        dto.setName(tour.getName());
        dto.setDescription(tour.getDescription());
        dto.setFromLocation(tour.getFromLocation());
        dto.setToLocation(tour.getToLocation());
        dto.setTransportType(tour.getTransportType());
        dto.setTourDistance(tour.getTourDistance());
        dto.setEstimatedTime(tour.getEstimatedTime());
        return dto;
    }

    @Override
    public Tour toEntity(TourDto dto) {
        Tour tour = new Tour();
        tour.setId(dto.getId());
        tour.setName(dto.getName());
        tour.setDescription(dto.getDescription());
        tour.setFromLocation(dto.getFromLocation());
        tour.setToLocation(dto.getToLocation());
        tour.setTransportType(dto.getTransportType());
        tour.setTourDistance(dto.getTourDistance());
        tour.setEstimatedTime(dto.getEstimatedTime());
        return tour;
    }
}