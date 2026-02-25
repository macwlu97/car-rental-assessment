package com.carrental.car_rental_assessment.api.dto;

import com.carrental.car_rental_assessment.domain.model.CarType;
import java.time.LocalDateTime;

public record ReservationRequest(
        CarType type,
        LocalDateTime startTime,
        int durationDays
) {}