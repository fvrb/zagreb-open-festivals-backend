package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.request.DrinkRequest;
import com.zagrebopenfestivals.dto.response.DrinkResponse;
import com.zagrebopenfestivals.service.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;

import java.util.List;

/**
 * TODO (studentski zadatak): implementirati REST endpointe za Drink,
 * potpuno analogno {@link FoodController} (koji je gotov primjer):
 *
 *   GET    /festivals/{festivalId}/drinks   -> lista pića za festival
 *   POST   /festivals/{festivalId}/drinks   -> kreiranje novog pića (ADMIN)
 *   PUT    /drinks/{id}                     -> uređivanje pića (ADMIN)
 *   DELETE /drinks/{id}                     -> brisanje pića (ADMIN)
 *
 * Ne zaboravite:
 *  - implementirati odgovarajuće metode u DrinkService (vidi TODO tamo)
 *  - dodati @Valid na request body
 *  - vratiti ispravne HTTP statuse (201 za create, 204 za delete)
 *  - dodati pravila pristupa u SecurityConfig (GET javno, write samo ADMIN)
 */
@RestController
@RequiredArgsConstructor
public class DrinkController {

    private final DrinkService drinkService;

    @GetMapping("/festivals/{festivalId}/drinks")
    public List<DrinkResponse> getAllByFestival(@PathVariable Long festivalId) {
        return drinkService.getAllByFestival(festivalId);
    }

    @PostMapping("/festivals/{festivalId}/drinks")
    @ResponseStatus(HttpStatus.CREATED)
    public DrinkResponse create(@PathVariable Long festivalId, @Valid @RequestBody DrinkRequest request) {
        return drinkService.create(festivalId, request);
    }

    @PutMapping("/drinks/{id}")
    public DrinkResponse update(@PathVariable Long id, @Valid @RequestBody DrinkRequest request) {
        return drinkService.update(id, request);
    }

    @DeleteMapping("/drinks/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        drinkService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
