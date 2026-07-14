package com.zagrebopenfestivals.dto.response;

import java.math.BigDecimal;

/**
 * DTO je gotov - popuniti kroz DrinkMapper kad se implementira DrinkService (studentski zadatak).
 */
public record DrinkResponse(
        Long id,
        String name,
        BigDecimal price
) {
}
