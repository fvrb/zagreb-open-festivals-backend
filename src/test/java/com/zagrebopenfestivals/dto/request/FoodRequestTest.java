package com.zagrebopenfestivals.dto.request;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FoodRequestTest {

    @Test
    void validateAcceptsValidRequest() {
        FoodRequest request = new FoodRequest("Burger", new BigDecimal("10.00"));

        assertThatCode(request::validate).doesNotThrowAnyException();
    }

    @Test
    void validateRejectsBlankName() {
        FoodRequest request = new FoodRequest("   ", new BigDecimal("10.00"));

        assertThatThrownBy(request::validate)
                .isInstanceOfSatisfying(ResponseStatusException.class, ex -> {
                    org.assertj.core.api.Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    org.assertj.core.api.Assertions.assertThat(ex.getReason()).isEqualTo("Naziv je obavezan");
                });
    }

    @Test
    void validateRejectsNegativePrice() {
        FoodRequest request = new FoodRequest("Burger", new BigDecimal("-1.00"));

        assertThatThrownBy(request::validate)
                .isInstanceOfSatisfying(ResponseStatusException.class, ex -> {
                    org.assertj.core.api.Assertions.assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    org.assertj.core.api.Assertions.assertThat(ex.getReason()).isEqualTo("Cijena ne smije biti negativna");
                });
    }
}
