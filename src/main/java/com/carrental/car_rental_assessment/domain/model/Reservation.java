package com.carrental.car_rental_assessment.domain.model;

import java.time.LocalDateTime;

public record Reservation(
        String id,
        String carId,
        CarType carType,
        LocalDateTime startTime,
        int durationDays
) {
    public LocalDateTime getEndTime() {
        return startTime.plusDays(durationDays);
    }

    public boolean overlapsWith(LocalDateTime start, int days) {
        LocalDateTime end = start.plusDays(days);
        return (start.isBefore(this.getEndTime()) && end.isAfter(this.startTime));
    }

    public static ReservationBuilder builder() {
        return new ReservationBuilder();
    }

    public static class ReservationBuilder {
        private String id;
        private String carId;
        private CarType carType;
        private LocalDateTime startTime;
        private int durationDays;

        public ReservationBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ReservationBuilder carId(String carId) {
            this.carId = carId;
            return this;
        }

        public ReservationBuilder carType(CarType carType) {
            this.carType = carType;
            return this;
        }

        public ReservationBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ReservationBuilder durationDays(int durationDays) {
            this.durationDays = durationDays;
            return this;
        }

        public Reservation build() {
            return new Reservation(id, carId, carType, startTime, durationDays);
        }
    }
}