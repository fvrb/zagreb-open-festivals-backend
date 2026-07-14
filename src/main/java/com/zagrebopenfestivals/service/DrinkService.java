package com.zagrebopenfestivals.service;

import com.zagrebopenfestivals.dto.request.DrinkRequest;
import com.zagrebopenfestivals.dto.response.DrinkResponse;

import java.util.List;

public interface DrinkService {

    List<DrinkResponse> getAllByFestival(Long festivalId);

    DrinkResponse create(Long festivalId, DrinkRequest request);

    DrinkResponse update(Long id, DrinkRequest request);

    void delete(Long id);
}