package com.zagrebopenfestivals.dto.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

public record FoodRequest(
        String name,
        BigDecimal price
) {
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Naziv je obavezan");
        }
        if (price == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cijena je obavezna");
        }
        if (price.signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cijena ne smije biti negativna");
        }
    }
}
