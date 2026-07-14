package com.zagrebopenfestivals.service.impl;

import com.zagrebopenfestivals.dto.request.DrinkRequest;
import com.zagrebopenfestivals.dto.response.DrinkResponse;
import com.zagrebopenfestivals.entity.Drink;
import com.zagrebopenfestivals.entity.Festival;
import com.zagrebopenfestivals.exception.ResourceNotFoundException;
import com.zagrebopenfestivals.mapper.DrinkMapper;
import com.zagrebopenfestivals.repository.DrinkRepository;
import com.zagrebopenfestivals.repository.FestivalRepository;
import com.zagrebopenfestivals.service.DrinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO (studentski zadatak): implementirati CRUD logiku za Drink,
 * po uzoru na {@link FoodServiceImpl} (koji je potpuno gotov primjer).
 *
 * Repozitoriji (DrinkRepository, FestivalRepository) i DTO-i (DrinkRequest, DrinkResponse)
 * su već pripremljeni - potrebno je samo popuniti metode ispod.
 *
 * Koraci:
 *  1. getAllByFestival(festivalId) - vratiti listu pića za dani festival
 *  2. create(festivalId, request) - kreirati novo piće vezano uz festival
 *  3. update(id, request) - urediti postojeće piće
 *  4. delete(id) - obrisati piće
 *  5. Ne zaboraviti @Transactional / @Transactional(readOnly = true) na metodama
 *  6. Napraviti DrinkMapper (entitet -> DrinkResponse), analogno FoodMapper-u
 */
@Service
@RequiredArgsConstructor
public class DrinkServiceImpl implements DrinkService {

    private final DrinkRepository drinkRepository;
    private final FestivalRepository festivalRepository;
    private final DrinkMapper drinkMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DrinkResponse> getAllByFestival(Long festivalId) {
        return drinkRepository.findAllByFestivalId(festivalId).stream()
                .map(drinkMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public DrinkResponse create(Long festivalId, DrinkRequest request) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> ResourceNotFoundException.of("Festival", festivalId));

        Drink drink = Drink.builder()
                .name(request.name())
                .price(request.price())
                .festival(festival)
                .build();

        Drink saved = drinkRepository.save(drink);
        return drinkMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DrinkResponse update(Long id, DrinkRequest request) {
        Drink drink = drinkRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Drink", id));

        drink.setName(request.name());
        drink.setPrice(request.price());

        return drinkMapper.toResponse(drink);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!drinkRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Drink", id);
        }
        drinkRepository.deleteById(id);
    }
}
