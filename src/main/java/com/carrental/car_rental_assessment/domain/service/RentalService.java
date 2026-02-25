package com.carrental.car_rental_assessment.domain.service;

import com.carrental.car_rental_assessment.domain.model.Car;
import com.carrental.car_rental_assessment.domain.model.CarType;
import com.carrental.car_rental_assessment.domain.model.Reservation;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RentalService {

    private final List<Car> inventory = Arrays.asList(
            new Car("SED-1", CarType.SEDAN),
            new Car("SED-2", CarType.SEDAN),
            new Car("SUV-1", CarType.SUV),
            new Car("SUV-2", CarType.SUV),
            new Car("VAN-1", CarType.VAN)
    );

    private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

    public synchronized Optional<Reservation> createReservation(CarType type, LocalDateTime start, int days) {
        if (start == null || days <= 0 || type == null) {
            return Optional.empty();
        }

        List<Car> availableCars = inventory.stream()
                .filter(car -> car.type() == type)
                .filter(car -> isCarAvailable(car.id(), start, days))
                .toList();

        if (availableCars.isEmpty()) {
            return Optional.empty();
        }

        Car assignedCar = availableCars.get(0);

        Reservation newReservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .carId(assignedCar.id())
                .carType(type)
                .startTime(start)
                .durationDays(days)
                .build();

        reservations.put(newReservation.id(), newReservation);
        return Optional.of(newReservation);
    }

    private boolean isCarAvailable(String carId, LocalDateTime start, int days) {
        return reservations.values().stream()
                .filter(res -> res.carId().equals(carId))
                .noneMatch(res -> res.overlapsWith(start, days));
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations.values());
    }
}