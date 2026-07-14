package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.response.FestivalSummaryResponse;
import com.zagrebopenfestivals.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO (studentski zadatak): implementirati REST endpointe za omiljene festivale:
 *
 *   GET    /favorites/me              -> lista mojih omiljenih festivala (USER)
 *   POST   /favorites/{festivalId}    -> dodaj festival u omiljene (USER)
 *   DELETE /favorites/{festivalId}    -> makni festival iz omiljenih (USER)
 *
 * Korisničko ime prijavljenog korisnika dobivate iz Spring Security konteksta,
 * npr. kroz parametar metode: {@code Authentication authentication} pa
 * {@code authentication.getName()}.
 *
 * Ne zaboravite:
 *  - implementirati odgovarajuće metode u FavoriteService (vidi TODO tamo)
 *  - dodati pravilo u SecurityConfig da su /favorites/** dostupni samo prijavljenom USER-u
 *  - vratiti ispravne HTTP statuse (201 za dodavanje, 204 za brisanje)
 */
@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping("/me")
    public List<FestivalSummaryResponse> getMyFavorites(Authentication authentication) {
        return favoriteService.getMyFavorites(authentication.getName());
    }

    @PostMapping("/{festivalId}")
    public ResponseEntity<Void> addFavorite(Authentication authentication, @PathVariable Long festivalId) {
        favoriteService.addFavorite(authentication.getName(), festivalId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{festivalId}")
    public ResponseEntity<Void> removeFavorite(Authentication authentication, @PathVariable Long festivalId) {
        favoriteService.removeFavorite(authentication.getName(), festivalId);
        return ResponseEntity.noContent().build();
    }
}
