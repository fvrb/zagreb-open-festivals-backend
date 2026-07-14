package com.zagrebopenfestivals.dto.response;

import java.time.LocalDate;
import java.util.List;

/**
 * Koristi se za stranicu s detaljima festivala - uključuje punu listu hrane i pića.
 */
public record FestivalDetailResponse(
        Long id,
        String name,
        String description,
        String location,
        LocalDate date,
        String imageUrl,
        List<FoodResponse> foods,
        List<DrinkResponse> drinks
) {
}
