package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.request.FoodRequest;
import com.zagrebopenfestivals.dto.response.FoodResponse;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.entity.Food;
import com.zagrebopenfestivals.exception.ResourceNotFoundException;
import com.zagrebopenfestivals.mapper.FoodMapper;
import com.zagrebopenfestivals.repository.FestivalRepository;
import com.zagrebopenfestivals.repository.FoodRepository;
import com.zagrebopenfestivals.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Ovo je referentna, potpuno implementirana CRUD usluga za Food -
 * po ovom uzoru napravite DrinkService (studentski zadatak).
 */
@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodRepository foodRepository;
    private final FestivalRepository festivalRepository;
    private final FoodMapper foodMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FoodResponse> getAllByFestival(Long festivalId) {
        return foodRepository.findAllByFestivalId(festivalId).stream()
                .map(foodMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public FoodResponse create(Long festivalId, FoodRequest request) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> ResourceNotFoundException.of("Festival", festivalId));

        Food food = Food.builder()
                .name(request.name())
                .price(request.price())
                .festival(festival)
                .build();

        Food saved = foodRepository.save(food);
        return foodMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public FoodResponse update(Long id, FoodRequest request) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Food", id));

        food.setName(request.name());
        food.setPrice(request.price());

        return foodMapper.toResponse(food);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!foodRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Food", id);
        }
        foodRepository.deleteById(id);
    }
}