package com.zagrebopenfestivals.service;

import com.zagrebopenfestivals.dto.request.FestivalRequest;
import com.zagrebopenfestivals.dto.response.FestivalDetailResponse;
import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;

import java.util.List;

public interface FestivalService {

    List<FestivalSummaryResponse> getAll(String search);

    FestivalDetailResponse getById(Long id);

    FestivalDetailResponse create(FestivalRequest request);

    FestivalDetailResponse update(Long id, FestivalRequest request);

    void delete(Long id);
}
