package com.zagrebopenfestivals.controller;

import com.zagrebopenfestivals.dto.request.FoodRequest;
import com.zagrebopenfestivals.dto.response.FoodResponse;
import com.zagrebopenfestivals.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Potpuno gotov referentni CRUD controller - po ovom uzoru napravite DrinkController
 * (studentski zadatak), vidi DrinkController.java za detaljne upute.
 */
@RestController
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/festivals/{festivalId}/foods")
    public List<FoodResponse> getAllByFestival(@PathVariable Long festivalId) {
        return foodService.getAllByFestival(festivalId);
    }

    @PostMapping("/festivals/{festivalId}/foods")
    @ResponseStatus(HttpStatus.CREATED)
    public FoodResponse create(@PathVariable Long festivalId, @RequestBody FoodRequest request) {
        request.validate();
        return foodService.create(festivalId, request);
    }

    @PutMapping("/foods/{id}")
    public FoodResponse update(@PathVariable Long id, @RequestBody FoodRequest request) {
        request.validate();
        return foodService.update(id, request);
    }

    @DeleteMapping("/foods/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
