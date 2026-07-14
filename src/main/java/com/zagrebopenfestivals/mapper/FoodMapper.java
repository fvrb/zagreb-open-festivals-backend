package com.zagrebopenfestivals.mapper;

import com.zagrebopenfestivals.dto.response.FoodResponse;
import com.zagrebopenfestivals.entity.Food;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {

    public FoodResponse toResponse(Food food) {
        return new FoodResponse(food.getId(), food.getName(), food.getPrice());
    }
}
