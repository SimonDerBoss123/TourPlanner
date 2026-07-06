package com.example.tourplannerbackend;

import com.example.tourplannerbackend.domain.Tour;
import com.example.tourplannerbackend.domain.TourLog;
import com.example.tourplannerbackend.repository.TourLogRepository;
import com.example.tourplannerbackend.repository.TourRepository;
import com.example.tourplannerbackend.service.TourLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourLogServiceTest {

    @Mock
    private TourLogRepository tourLogRepository;

    @Mock
    private TourRepository tourRepository;

    @InjectMocks
    private TourLogService tourLogService;

    @Test
    void createTourLog_savesTourLogWithTour() {
        Tour tour = new Tour();
        tour.setId(1L);
        tour.setName("Wienerwald");
        com.example.tourplannerbackend.domain.User user = new com.example.tourplannerbackend.domain.User();
        user.setUsername("simon");
        tour.setUser(user);

        TourLog tourLog = new TourLog();
        tourLog.setComment("Super!");

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));
        when(tourLogRepository.save(tourLog)).thenReturn(tourLog);

        TourLog result = tourLogService.createTourLog(1L, tourLog, "simon");

        assertEquals("Wienerwald", result.getTour().getName());
        verify(tourLogRepository, times(1)).save(tourLog);
    }

    @Test
    void createTourLog_throwsExceptionWhenTourNotFound() {
        when(tourRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> tourLogService.createTourLog(99L, new TourLog(),"simon"));
    }

    @Test
    void deleteTourLog_callsDeleteById() {
        Tour tour = new Tour();
        tour.setId(1L);
        com.example.tourplannerbackend.domain.User user = new com.example.tourplannerbackend.domain.User();
        user.setUsername("simon");
        tour.setUser(user);

        when(tourRepository.findById(1L)).thenReturn(Optional.of(tour));

        tourLogService.deleteTourLog(1L, 1L, "simon");
        verify(tourLogRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTourLogsByTourId_returnsTourLogs() {
        TourLog log1 = new TourLog();
        log1.setComment("Schön");

        TourLog log2 = new TourLog();
        log2.setComment("Toll");

        when(tourLogRepository.findByTourId(1L)).thenReturn(List.of(log1, log2));

        List<TourLog> result = tourLogService.getTourLogsByTourId(1L);

        assertEquals(2, result.size());
        assertEquals("Schön", result.get(0).getComment());
    }

    @Test
    void getTourLogsByTourId_returnsEmptyListWhenNoLogs() {
        when(tourLogRepository.findByTourId(1L)).thenReturn(List.of());

        List<TourLog> result = tourLogService.getTourLogsByTourId(1L);

        assertEquals(0, result.size());
    }

    @Test
    void getTourLogById_returnsTourLog() {
        TourLog tourLog = new TourLog();
        tourLog.setId(1L);
        tourLog.setComment("Super!");

        when(tourLogRepository.findById(1L)).thenReturn(Optional.of(tourLog));

        TourLog result = tourLogService.getTourLogById(1L);

        assertEquals("Super!", result.getComment());
    }

    @Test
    void getTourLogById_throwsExceptionWhenNotFound() {
        when(tourLogRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> tourLogService.getTourLogById(99L));
    }

    @Test
    void updateTourLog_setsIdAndSaves() {
        Tour tour = new Tour();
        com.example.tourplannerbackend.domain.User user = new com.example.tourplannerbackend.domain.User();
        user.setUsername("simon");
        tour.setUser(user);

        TourLog existing = new TourLog();
        existing.setId(1L);
        existing.setTour(tour);

        TourLog tourLog = new TourLog();
        tourLog.setComment("Updated");

        when(tourLogRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(tourLogRepository.save(any(TourLog.class))).thenReturn(tourLog);

        TourLog result = tourLogService.updateTourLog(1L, tourLog, "simon");

        assertEquals(1L, result.getId());
        verify(tourLogRepository, times(1)).save(tourLog);
    }
}