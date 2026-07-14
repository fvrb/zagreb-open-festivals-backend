package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.entity.Favorite;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.entity.User;
import com.zagrebopenfestivals.exception.DuplicateResourceException;
import com.zagrebopenfestivals.exception.ResourceNotFoundException;
import com.zagrebopenfestivals.mapper.FestivalMapper;
import com.zagrebopenfestivals.repository.FavoriteRepository;
import com.zagrebopenfestivals.repository.FestivalRepository;
import com.zagrebopenfestivals.repository.UserRepository;
import com.zagrebopenfestivals.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO (studentski zadatak): implementirati logiku za omiljene festivale.
 *
 * Entitet {@code Favorite} i {@code FavoriteRepository} su već gotovi (N:M veza
 * User <-> Festival preko zasebne "join" tablice).
 *
 * Koraci:
 *  1. getMyFavorites(username) - dohvatiti sve Favorite zapise za korisnika i
 *     mapirati ih u listu FestivalSummaryResponse (koristeći FestivalMapper)
 *  2. addFavorite(username, festivalId) - provjeriti da festival postoji i da
 *     kombinacija (user, festival) već ne postoji (FavoriteRepository.existsByUserIdAndFestivalId),
 *     zatim kreirati i spremiti novi Favorite
 *  3. removeFavorite(username, festivalId) - pronaći Favorite
 *     (FavoriteRepository.findByUserIdAndFestivalId) i obrisati ga
 *  4. Dodati odgovarajuće @Transactional / @Transactional(readOnly = true) anotacije
 *  5. Baciti ResourceNotFoundException ako festival ili favorit ne postoji
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final FestivalRepository festivalRepository;
    private final UserRepository userRepository;
    private final FestivalMapper festivalMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FestivalSummaryResponse> getMyFavorites(String username) {
        User user = getUser(username);
        return favoriteRepository.findAllByUserId(user.getId()).stream()
                .map(Favorite::getFestival)
                .map(festivalMapper::toSummaryResponse)
                .toList();
    }

    @Override
    @Transactional
    public void addFavorite(String username, Long festivalId) {
        User user = getUser(username);
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> ResourceNotFoundException.of("Festival", festivalId));

        if (favoriteRepository.existsByUserIdAndFestivalId(user.getId(), festivalId)) {
            throw new DuplicateResourceException("Festival je već u omiljenima");
        }

        Favorite favorite = Favorite.builder()
                .user(user)
                .festival(festival)
                .build();
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeFavorite(String username, Long festivalId) {
        User user = getUser(username);
        Favorite favorite = favoriteRepository.findByUserIdAndFestivalId(user.getId(), festivalId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorit ne postoji za festival s ID-em " + festivalId));
        favoriteRepository.delete(favorite);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik ne postoji: " + username));
    }
}
