package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteControllerTest {

    @Mock
    private FavoriteService favoriteService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FavoriteController favoriteController;

    @Test
    void getMyFavoritesReturnsFestivalSummaries() {
        when(authentication.getName()).thenReturn("alice");
        when(favoriteService.getMyFavorites("alice")).thenReturn(List.of(
                new FestivalSummaryResponse(
                        1L,
                        "Zagreb Summer",
                        "Zagreb",
                        LocalDate.of(2026, 7, 12),
                        "https://example.com/festival.jpg",
                        "Open air festival"
                )
        ));

        List<FestivalSummaryResponse> result = favoriteController.getMyFavorites(authentication);

        assertThat(result).hasSize(1);
        verify(favoriteService).getMyFavorites("alice");
    }

    @Test
    void addFavoriteReturnsCreated() {
        when(authentication.getName()).thenReturn("alice");

        ResponseEntity<Void> response = favoriteController.addFavorite(authentication, 42L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(favoriteService).addFavorite("alice", 42L);
    }

    @Test
    void removeFavoriteReturnsNoContent() {
        when(authentication.getName()).thenReturn("alice");

        ResponseEntity<Void> response = favoriteController.removeFavorite(authentication, 42L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(favoriteService).removeFavorite("alice", 42L);
    }
}
