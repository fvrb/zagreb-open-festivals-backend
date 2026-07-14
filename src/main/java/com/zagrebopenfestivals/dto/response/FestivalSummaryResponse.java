package com.zagrebopenfestivals.dto.response;

import java.time.LocalDate;

/**
 * Koristi se za listu festivala (početna stranica) - namjerno lagan,
 * bez food/drink liste, da se ne vuče nepotrebna količina podataka.
 */
public record FestivalSummaryResponse(
        Long id,
        String name,
        String location,
        LocalDate date,
        String imageUrl,
        String shortDescription
) {
}
