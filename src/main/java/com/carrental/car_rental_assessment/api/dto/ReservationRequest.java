package com.carrental.car_rental_assessment.api.dto;

import com.carrental.car_rental_assessment.domain.model.CarType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequest {
    private CarType type;
    private LocalDateTime startTime;
    private int durationDays;
}
