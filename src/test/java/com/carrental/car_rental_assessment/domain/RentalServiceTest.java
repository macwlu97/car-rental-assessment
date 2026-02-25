package com.carrental.car_rental_assessment.domain;

import com.carrental.car_rental_assessment.domain.model.CarType;
import com.carrental.car_rental_assessment.domain.service.RentalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class RentalServiceTest {

    private final RentalService rentalService = new RentalService();

    @Test
    @DisplayName("Should reserve a car when one is available")
    void shouldReserveCarWhenAvailable() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        int duration = 3;

        // when
        var result = rentalService.createReservation(CarType.SEDAN, start, duration);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getCarType()).isEqualTo(CarType.SEDAN);
    }

    @Test
    @DisplayName("Should fail to reserve when all cars of type are taken for given dates")
    void shouldFailWhenNoCarsAvailable() {
        // given
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        // Reserving all available Sedans (assuming there are 2 in the system).
        rentalService.createReservation(CarType.SEDAN, start, 1);
        rentalService.createReservation(CarType.SEDAN, start, 1);

        // when
        var result = rentalService.createReservation(CarType.SEDAN, start, 1);

        // then
        assertThat(result).isEmpty();
    }
}