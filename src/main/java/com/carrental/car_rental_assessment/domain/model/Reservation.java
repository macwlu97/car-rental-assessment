package com.carrental.car_rental_assessment.domain.model;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Reservation {
    private String id;
    private String carId;
    private CarType carType;
    private LocalDateTime startTime;
    private int durationDays;

    public LocalDateTime getEndTime() {
        return startTime.plusDays(durationDays);
    }

    public boolean overlapsWith(LocalDateTime start, int days) {
        LocalDateTime end = start.plusDays(days);
        // The new reservation starts before the old one ends AND the new reservation ends after the old one starts.
        return (start.isBefore(this.getEndTime()) && end.isAfter(this.startTime));
    }
}