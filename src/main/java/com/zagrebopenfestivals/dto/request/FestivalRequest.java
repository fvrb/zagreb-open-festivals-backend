package com.zagrebopenfestivals.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FestivalRequest(

        @NotBlank(message = "Naziv je obavezan")
        String name,

        String description,

        @NotBlank(message = "Lokacija je obavezna")
        String location,

        @NotNull(message = "Datum je obavezan")
        LocalDate date,

        String imageUrl
) {
}
