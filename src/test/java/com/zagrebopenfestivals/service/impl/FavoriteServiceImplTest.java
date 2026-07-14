package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.entity.Favorite;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.entity.User;
import com.zagrebopenfestivals.mapper.FestivalMapper;
import com.zagrebopenfestivals.repository.FavoriteRepository;
import com.zagrebopenfestivals.repository.FestivalRepository;
import com.zagrebopenfestivals.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceImplTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FestivalMapper festivalMapper;

    @InjectMocks
    private FavoriteServiceImpl favoriteService;

    @Test
    void getMyFavoritesMapsUserFavoritesToSummaries() {
        User user = User.builder().id(1L).username("alice").build();
        Festival festival = Festival.builder().id(42L).name("Summer Fest").location("Zagreb")
                .date(LocalDate.of(2026, 7, 12)).imageUrl("https://example.com/festival.jpg").build();
        FestivalSummaryResponse expected = new FestivalSummaryResponse(
                42L,
                "Summer Fest",
                "Zagreb",
                LocalDate.of(2026, 7, 12),
                "https://example.com/festival.jpg",
                "Open air festival"
        );

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(favoriteRepository.findAllByUserId(1L)).thenReturn(List.of(Favorite.builder().festival(festival).build()));
        when(festivalMapper.toSummaryResponse(festival)).thenReturn(expected);

        List<FestivalSummaryResponse> result = favoriteService.getMyFavorites("alice");

        assertThat(result).containsExactly(expected);
        verify(userRepository).findByUsername("alice");
        verify(favoriteRepository).findAllByUserId(1L);
        verify(festivalMapper).toSummaryResponse(festival);
    }

    @Test
    void addFavoriteCreatesFavoriteWhenItDoesNotExist() {
        User user = User.builder().id(1L).username("alice").build();
        Festival festival = Festival.builder().id(42L).name("Summer Fest").build();

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(festivalRepository.findById(42L)).thenReturn(Optional.of(festival));
        when(favoriteRepository.existsByUserIdAndFestivalId(1L, 42L)).thenReturn(false);

        favoriteService.addFavorite("alice", 42L);

        ArgumentCaptor<Favorite> captor = ArgumentCaptor.forClass(Favorite.class);
        verify(favoriteRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isSameAs(user);
        assertThat(captor.getValue().getFestival()).isSameAs(festival);
    }

    @Test
    void removeFavoriteDeletesExistingFavorite() {
        User user = User.builder().id(1L).username("alice").build();
        Favorite favorite = Favorite.builder().id(7L).user(user).festival(Festival.builder().id(42L).build()).build();

        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(favoriteRepository.findByUserIdAndFestivalId(1L, 42L)).thenReturn(Optional.of(favorite));

        favoriteService.removeFavorite("alice", 42L);

        verify(favoriteRepository).delete(favorite);
    }
}
