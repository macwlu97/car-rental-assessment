package com.carrental.car_rental_assessment.domain.service;

import com.carrental.car_rental_assessment.domain.model.Car;
import com.carrental.car_rental_assessment.domain.model.CarType;
import com.carrental.car_rental_assessment.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class RentalService {

    // Hardcoded limited fleet as per requirements
    private final List<Car> inventory = Arrays.asList(
            new Car("SED-1", CarType.SEDAN),
            new Car("SED-2", CarType.SEDAN),
            new Car("SUV-1", CarType.SUV),
            new Car("SUV-2", CarType.SUV),
            new Car("VAN-1", CarType.VAN)
    );

    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    /**
     * Tries to create a reservation. Thread-safe to prevent overbooking.
     */
    public synchronized Optional<Reservation> createReservation(CarType type, LocalDateTime start, int days) {
        if (days <= 0 || start == null) {
            return Optional.empty();
        }

        List<Car> availableCarsOfType = inventory.stream()
                .filter(car -> car.getType() == type)
                .filter(car -> isCarAvailable(car.getId(), start, days))
                .toList();

        if (availableCarsOfType.isEmpty()) {
            return Optional.empty();
        }

        Car assignedCar = availableCarsOfType.get(0);
        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .carId(assignedCar.getId())
                .carType(type)
                .startTime(start)
                .durationDays(days)
                .build();

        reservations.put(reservation.getId(), reservation);
        return Optional.of(reservation);
    }

    private boolean isCarAvailable(String carId, LocalDateTime start, int days) {
        return reservations.values().stream()
                .filter(res -> res.getCarId().equals(carId))
                .noneMatch(res -> res.overlapsWith(start, days));
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }
}