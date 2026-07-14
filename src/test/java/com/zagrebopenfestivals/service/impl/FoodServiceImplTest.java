package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.request.FoodRequest;
import com.zagrebopenfestivals.dto.response.FoodResponse;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.entity.Food;
import com.zagrebopenfestivals.mapper.FoodMapper;
import com.zagrebopenfestivals.repository.FestivalRepository;
import com.zagrebopenfestivals.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FoodServiceImplTest {

    @Mock
    private FoodRepository foodRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private FoodMapper foodMapper;

    @InjectMocks
    private FoodServiceImpl foodService;

    @Test
    void createPersistsFoodAndMapsResponse() {
        Long festivalId = 42L;
        Festival festival = Festival.builder().id(festivalId).name("Summer Fest").build();
        FoodRequest request = new FoodRequest("Burger", new BigDecimal("8.50"));
        Food savedFood = Food.builder().id(7L).name("Burger").price(new BigDecimal("8.50")).festival(festival).build();
        FoodResponse expectedResponse = new FoodResponse(7L, "Burger", new BigDecimal("8.50"));

        when(festivalRepository.findById(festivalId)).thenReturn(Optional.of(festival));
        when(foodRepository.save(any(Food.class))).thenReturn(savedFood);
        when(foodMapper.toResponse(savedFood)).thenReturn(expectedResponse);

        FoodResponse response = foodService.create(festivalId, request);

        assertThat(response).isEqualTo(expectedResponse);
        verify(festivalRepository).findById(festivalId);
        verify(foodRepository).save(any(Food.class));
        verify(foodMapper).toResponse(savedFood);
    }
}
