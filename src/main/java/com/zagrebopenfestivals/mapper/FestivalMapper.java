package com.zagrebopenfestivals.mapper;

import com.zagrebopenfestivals.dto.response.DrinkResponse;
import com.zagrebopenfestivals.dto.response.FestivalDetailResponse;
import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.dto.response.FoodResponse;
import com.zagrebopenfestivals.entity.Drink;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.entity.Food;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FestivalMapper {

    public FestivalSummaryResponse toSummaryResponse(Festival festival) {
        return new FestivalSummaryResponse(
                festival.getId(),
                festival.getName(),
                festival.getLocation(),
                festival.getDate(),
                festival.getImageUrl(),
                toShortDescription(festival.getDescription())
        );
    }

    /**
     * POZOR: ova metoda čita festival.getFoods() i festival.getDrinks() - to su LAZY
     * kolekcije. Ako se ova metoda pozove izvan aktivne Hibernate transakcije/sesije,
     * puca LazyInitializationException (jer je spring.jpa.open-in-view postavljen na false).
     * Poziva se isključivo iz servisne metode koja MORA biti transakcijska.
     */
    public FestivalDetailResponse toDetailResponse(Festival festival) {
        List<FoodResponse> foods = festival.getFoods().stream()
                .map(this::toFoodResponse)
                .toList();

        List<DrinkResponse> drinks = festival.getDrinks().stream()
                .map(this::toDrinkResponse)
                .toList();

        return new FestivalDetailResponse(
                festival.getId(),
                festival.getName(),
                festival.getDescription(),
                festival.getLocation(),
                festival.getDate(),
                festival.getImageUrl(),
                foods,
                drinks
        );
    }

    private FoodResponse toFoodResponse(Food food) {
        return new FoodResponse(food.getId(), food.getName(), food.getPrice());
    }

    private DrinkResponse toDrinkResponse(Drink drink) {
        return new DrinkResponse(drink.getId(), drink.getName(), drink.getPrice());
    }

    private String toShortDescription(String description) {
        if (description == null) {
            return null;
        }
        return description.length() > 150 ? description.substring(0, 150) + "..." : description;
    }
}
