package com.zagrebopenfestivals.mapper;

import com.zagrebopenfestivals.dto.response.DrinkResponse;
import com.zagrebopenfestivals.entity.Drink;
import org.springframework.stereotype.Component;

@Component
public class DrinkMapper {

    public DrinkResponse toResponse(Drink drink) {
        return new DrinkResponse(drink.getId(), drink.getName(), drink.getPrice());
    }
}
