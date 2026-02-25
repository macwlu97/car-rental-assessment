package com.carrental.car_rental_assessment.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RentalControllerItTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RentalService rentalService;

    @Test
    @DisplayName("POST /api/rentals/reserve - should return 200 and reservation data on success")
    void shouldReturnOkWhenReservationCreated() throws Exception {
        // given
        var request = Map.of(
                "type", "SUV",
                "startTime", "2025-10-10T10:00:00",
                "durationDays", 5
        );

        Reservation mockReservation = Reservation.builder()
                .id("res-123")
                .carId("SUV-1")
                .carType(CarType.SUV)
                .startTime(LocalDateTime.parse("2025-10-10T10:00:00"))
                .durationDays(5)
                .build();

        when(rentalService.createReservation(eq(CarType.SUV), any(), anyInt()))
                .thenReturn(Optional.of(mockReservation));

        // when & then
        mockMvc.perform(post("/api/rentals/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("res-123"))
                .andExpect(jsonPath("$.carType").value("SUV"));
    }

    @Test
    @DisplayName("POST /api/rentals/reserve - should return 400 when reservation fails")
    void shouldReturnBadRequestWhenReservationFails() throws Exception {
        // given
        var request = Map.of(
                "type", "VAN",
                "startTime", "2025-10-10T10:00:00",
                "durationDays", 1
        );

        when(rentalService.createReservation(any(), any(), anyInt()))
                .thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(post("/api/rentals/reserve")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}}