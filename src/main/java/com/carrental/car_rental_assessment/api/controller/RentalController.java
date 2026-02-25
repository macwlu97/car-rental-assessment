package com.carrental.car_rental_assessment.api.controller;

import com.carrental.car_rental_assessment.api.dto.ReservationRequest;
import com.carrental.car_rental_assessment.domain.model.Reservation;
import com.carrental.car_rental_assessment.domain.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;

    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserve(@RequestBody ReservationRequest request) {
        return rentalService.createReservation(request.getType(), request.getStartTime(), request.getDurationDays())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<Reservation>> getAll() {
        return ResponseEntity.ok(rentalService.getAllReservations());
    }
}