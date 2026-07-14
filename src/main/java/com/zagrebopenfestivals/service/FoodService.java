package com.zagrebopenfestivals.service;

import com.zagrebopenfestivals.dto.request.FoodRequest;
import com.zagrebopenfestivals.dto.response.FoodResponse;

import java.util.List;

public interface FoodService {

    List<FoodResponse> getAllByFestival(Long festivalId);

    FoodResponse create(Long festivalId, FoodRequest request);

    FoodResponse update(Long id, FoodRequest request);

    void delete(Long id);
}