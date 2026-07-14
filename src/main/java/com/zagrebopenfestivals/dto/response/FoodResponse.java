package com.zagrebopenfestivals.dto.response;

import java.math.BigDecimal;

public record FoodResponse(
        Long id,
        String name,
        BigDecimal price
) {
}
