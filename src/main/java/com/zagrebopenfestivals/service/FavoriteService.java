package com.zagrebopenfestivals.service;

import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;

import java.util.List;

public interface FavoriteService {

    List<FestivalSummaryResponse> getMyFavorites(String username);

    void addFavorite(String username, Long festivalId);

    void removeFavorite(String username, Long festivalId);
}